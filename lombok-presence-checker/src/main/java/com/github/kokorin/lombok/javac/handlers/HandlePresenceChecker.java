package com.github.kokorin.lombok.javac.handlers;

import com.github.kokorin.lombok.PresenceChecker;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import lombok.AccessLevel;
import lombok.ConfigurationKeys;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.core.handlers.HandlerUtil;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import java.util.Collection;

import static lombok.core.handlers.HandlerUtil.handleFlagUsage;
import static lombok.javac.Javac.CTC_BOOLEAN;
import static lombok.javac.handlers.JavacHandlerUtil.*;
import static com.github.kokorin.lombok.javac.handlers.JavacHandlerUtil.*;

/**
 * Handles the {@code PresenceChecker} annotation for javac.
 */
@HandlerPriority(128) // we must run AFTER HandleSetter which is at 0 (default value)
public class HandlePresenceChecker extends JavacAnnotationHandler<PresenceChecker> {
    @Override
    public void handle(AnnotationValues<PresenceChecker> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
        Collection<JavacNode> fields = annotationNode.upFromAnnotationToFields();
        deleteAnnotationIfNeccessary(annotationNode, PresenceChecker.class);

        JavacNode node = annotationNode.up();

        if (node == null) {
            return;
        }

        switch (node.getKind()) {
            case FIELD:
                createPresenceCheckerForFields(fields);
                break;
            case TYPE:
                generatePresenceCheckerForType(node);
                break;
        }

    }

    private void createPresenceCheckerForFields(Collection<JavacNode> fieldNodes) {
        for (JavacNode fieldNode : fieldNodes) {
            createPresenceCheckerForField(fieldNode);
        }
    }

    private void generatePresenceCheckerForType(JavacNode typeNode) {
        JCTree.JCClassDecl typeDecl = null;
        if (typeNode.get() instanceof JCTree.JCClassDecl) typeDecl = (JCTree.JCClassDecl) typeNode.get();
        long modifiers = typeDecl == null ? 0 : typeDecl.mods.flags;
        boolean notAClass = (modifiers & (Flags.INTERFACE | Flags.ANNOTATION | Flags.ENUM)) != 0;

        if (typeDecl == null || notAClass) {
            typeNode.addError("@PresenceChecker is only supported on a class or a field.");
            return;
        }

        for (JavacNode field : typeNode.down()) {
            if (fieldQualifiesForPresenceCheckerGeneration(field)) {
                generatePresenceCheckerForField(field);
            }
        }
    }

    private void generatePresenceCheckerForField(JavacNode fieldNode) {
        if (hasAnnotation(PresenceChecker.class, fieldNode)) {
            //The annotation will make it happen, so we can skip it.
            return;
        }

        createPresenceCheckerForField(fieldNode);
    }

    private void createPresenceCheckerForField(JavacNode fieldNode) {
        if (fieldNode.getKind() != AST.Kind.FIELD) {
            fieldNode.addError("@PresenceChecker is only supported on a class or a field.");
            return;
        }

        JCTree.JCVariableDecl fieldDecl = (JCTree.JCVariableDecl) fieldNode.get();

        if ((fieldDecl.mods.flags & Flags.FINAL) != 0) {
            fieldNode.addWarning("Not generating presence checker for this field: Presence checkers cannot be generated for final fields.");
            return;
        }

        if ((fieldDecl.mods.flags & Flags.STATIC) != 0) {
            fieldNode.addWarning("Not generating presence checker for this field: Presence checkers cannot be generated for static fields.");
            return;
        }

        JavacTreeMaker treeMaker = fieldNode.getTreeMaker();

        JavacNode setterMethod = findSetterForField(fieldNode);
        if (setterMethod == null) {
            fieldNode.addError("Setter is required for PresenceChecker. Either add it manually or via @Setter annotation");
            return;
        }

        JCTree.JCVariableDecl presenceCheckerFieldDecl = createPresenceCheckerField(fieldNode, treeMaker);
        JavacNode presenceCheckerField = injectFieldAndMarkGenerated(fieldNode.up(), presenceCheckerFieldDecl);

        JCTree.JCMethodDecl presenceCheckerMethod = createPresenceCheckerMethod(presenceCheckerField, treeMaker);
        injectMethod(fieldNode.up(), presenceCheckerMethod);

        JCTree.JCMethodDecl setterMethodDecl = (JCTree.JCMethodDecl) setterMethod.get();

        updateSetterMethod(setterMethodDecl, presenceCheckerField, treeMaker);
    }

    private JCTree.JCMethodDecl createPresenceCheckerMethod(JavacNode presenceCheckerField, JavacTreeMaker treeMaker) {
        long access = toJavacModifier(AccessLevel.PUBLIC);
        return createPresenceCheckerMethod(access, presenceCheckerField, treeMaker, presenceCheckerField.get());
    }

    private JCTree.JCMethodDecl createPresenceCheckerMethod(long access, JavacNode presenceCheckerField, JavacTreeMaker treeMaker, JCTree source) {
        JCTree.JCVariableDecl fieldNode = (JCTree.JCVariableDecl) presenceCheckerField.get();

        JCTree.JCExpression methodType = treeMaker.TypeIdent(CTC_BOOLEAN);
        Name methodName = presenceCheckerField.toName(presenceCheckerField.getName());

        List<JCTree.JCStatement> statements;
        JCTree toClearOfMarkers = null;
        if (!inNetbeansEditor(presenceCheckerField)) {
            toClearOfMarkers = fieldNode.init;
        }

        statements = createPresenceCheckerBody(treeMaker, presenceCheckerField);

        JCTree.JCBlock methodBody = treeMaker.Block(0, statements);

        List<JCTree.JCTypeParameter> methodGenericParams = List.nil();
        List<JCTree.JCVariableDecl> parameters = List.nil();
        List<JCTree.JCExpression> throwsClauses = List.nil();
        JCTree.JCExpression annotationMethodDefaultValue = null;

        JCTree.JCMethodDecl decl = recursiveSetGeneratedBy(
                treeMaker.MethodDef(
                        treeMaker.Modifiers(access, List.<JCTree.JCAnnotation>nil()),
                        methodName,
                        methodType,
                        methodGenericParams,
                        parameters,
                        throwsClauses,
                        methodBody,
                        annotationMethodDefaultValue
                ),
                source,
                presenceCheckerField.getContext()
        );

        if (toClearOfMarkers != null) recursiveSetGeneratedBy(toClearOfMarkers, null, null);

        return decl;
    }

    private JCTree.JCVariableDecl createPresenceCheckerField(JavacNode fieldNode, JavacTreeMaker maker) {
        Name valueName = fieldNode.toName(toHasName(fieldNode));

        return maker.VarDef(maker.Modifiers(Flags.PRIVATE), valueName, maker.TypeIdent(CTC_BOOLEAN), null);
    }

    private List<JCTree.JCStatement> createPresenceCheckerBody(JavacTreeMaker treeMaker, JavacNode field) {
        return List.<JCTree.JCStatement>of(treeMaker.Return(createFieldAccessor(treeMaker, field, HandlerUtil.FieldAccess.ALWAYS_FIELD)));
    }

    private void updateSetterMethod(JCTree.JCMethodDecl setterMethod, JavacNode presenceCheckerField, JavacTreeMaker treeMaker) {
        JCTree.JCExpression fieldRef = createFieldAccessor(treeMaker, presenceCheckerField, HandlerUtil.FieldAccess.ALWAYS_FIELD);
        JCTree.JCAssign assign = treeMaker.Assign(fieldRef, treeMaker.Literal(true));
        JCTree.JCStatement setPresenceStatement = treeMaker.Exec(assign);

        JCTree.JCBlock body = setterMethod.getBody();

        List<JCTree.JCStatement> updatedStatements = List.of(setPresenceStatement).prependList(body.stats);

        setterMethod.body = treeMaker.Block(body.flags, updatedStatements);
    }

    private JavacNode findSetterForField(JavacNode fieldNode) {
        String setterName = toSetterName(fieldNode);

        if (setterName != null) {
            for (JavacNode node : fieldNode.up().down()) {
                if (node.getKind() == AST.Kind.METHOD && setterName.equals(node.getName())) {
                    return node;
                }
            }
        }
        return null;
    }

    public static boolean fieldQualifiesForPresenceCheckerGeneration(JavacNode field) {
        if (field.getKind() != AST.Kind.FIELD) return false;
        JCTree.JCVariableDecl fieldDecl = (JCTree.JCVariableDecl) field.get();
        //Skip fields that start with $
        if (fieldDecl.name.toString().startsWith("$")) return false;
        //Skip static fields.
        if ((fieldDecl.mods.flags & Flags.STATIC) != 0) return false;
        //Skip final fields.
        if ((fieldDecl.mods.flags & Flags.FINAL) != 0) return false;
        return true;
    }
}
