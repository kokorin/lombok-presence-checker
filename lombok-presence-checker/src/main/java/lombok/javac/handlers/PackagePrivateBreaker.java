package lombok.javac.handlers;

import com.sun.tools.javac.tree.JCTree;
import lombok.core.handlers.HandlerUtil;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

public class PackagePrivateBreaker {
    public static JCTree.JCExpression createFieldAccessor(JavacTreeMaker maker,
                                                          JavacNode field,
                                                          HandlerUtil.FieldAccess fieldAccess) {
        return JavacHandlerUtil.createFieldAccessor(maker, field, fieldAccess);
    }

    public static JCTree.JCExpression createFieldAccessor(JavacTreeMaker maker,
                                                          JavacNode field,
                                                          HandlerUtil.FieldAccess fieldAccess,
                                                          JCTree.JCExpression receiver) {
        return JavacHandlerUtil.createFieldAccessor(maker, field, fieldAccess, receiver);
    }
}
