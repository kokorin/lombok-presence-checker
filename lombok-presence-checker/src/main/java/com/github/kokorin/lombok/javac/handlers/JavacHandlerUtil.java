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
