package com.github.kokorin.lombok.javac.handlers;

import com.sun.tools.javac.tree.JCTree;
import lombok.core.handlers.HandlerUtil;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.PackagePrivateBreaker;

public class JavacHandlerUtil {

    /**
     * @return the likely has name for the stated field. (e.g. private String foo; to hasFoo).
     */
    public static String toHasName(JavacNode field) {
        return HandlerUtil.buildAccessorName("has", field.getName());
    }

    static JCTree.JCExpression createFieldAccessor(JavacTreeMaker maker, JavacNode field, HandlerUtil.FieldAccess fieldAccess) {
        return PackagePrivateBreaker.createFieldAccessor(maker, field, fieldAccess);
    }
}
