/*
 *    Copyright  2020 Denis Kokorin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

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
