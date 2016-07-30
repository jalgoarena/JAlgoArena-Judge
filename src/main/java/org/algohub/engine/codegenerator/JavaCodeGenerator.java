package org.algohub.engine.codegenerator;

import com.google.common.collect.ImmutableMap;

import org.algohub.engine.judge.Function;
import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.TypeNode;

/**
 * Generate compilable and runnable Java code.
 */
public final class JavaCodeGenerator {

    /**
     * Map intermediate types to Java types.
     */
    private static final ImmutableMap<IntermediateType, String> JAVA_TYPE_MAP =
            ImmutableMap.<IntermediateType, String>builder().put(IntermediateType.BOOL, "boolean")
                    .put(IntermediateType.STRING, "String").put(IntermediateType.DOUBLE, "double")
                    .put(IntermediateType.INT, "int").put(IntermediateType.LONG, "long")

                    .put(IntermediateType.ARRAY, "[]")
                    .put(IntermediateType.LIST, "ArrayList").put(IntermediateType.SET, "HashSet")
                    .put(IntermediateType.MAP, "HashMap")

                    .put(IntermediateType.LINKED_LIST_NODE, "LinkedListNode")
                    .put(IntermediateType.BINARY_TREE_NODE, "BinaryTreeNode")

                    .build();

    /**
     * Map intermediate types to real java classes.
     */
    private static final ImmutableMap<IntermediateType, String> JAVA_CLASS_MAP =
            ImmutableMap.<IntermediateType, String>builder().put(IntermediateType.BOOL, "Boolean")
                    .put(IntermediateType.STRING, "String").put(IntermediateType.DOUBLE, "Double")
                    .put(IntermediateType.INT, "Integer").put(IntermediateType.LONG, "Long").build();

    private JavaCodeGenerator() {
    }

    /**
     * Convert a TypeNode to a Java type declaration.
     * <p>
     * <p> post order, recursive.</p>
     */
    private static String generateTypeDeclaration(final TypeNode type,
                                                  final IntermediateType parentType) {
        if (!type.isContainer()) {
            if (parentType == IntermediateType.ARRAY) {
                return JAVA_TYPE_MAP.get(type.getValue());
            } else {
                return JAVA_CLASS_MAP.get(type.getValue());
            }
        }
        if (type.getValue() == IntermediateType.ARRAY) {
            return generateTypeDeclaration(type.getElementType(), type.getValue()) + "[]";
        } else {
            final String containerTypeStr = JAVA_TYPE_MAP.get(type.getValue());
            if (type.getKeyType() != null) {
                return containerTypeStr + "<" + generateTypeDeclaration(type.getKeyType(),
                        type.getValue()) + "," + generateTypeDeclaration(type.getElementType(),
                        type.getValue()) + ">";
            } else {
                return containerTypeStr + "<" + generateTypeDeclaration(type.getElementType(),
                        type.getValue()) + ">";
            }
        }
    }

    /**
     * Generate a type declaration.
     * <p>
     * <p>post order, recursive.</p>
     *
     * @param type the type
     * @return type declaration
     */
    static String generateTypeDeclaration(final TypeNode type) {
        // the parent type should be ARRAY by default
        return generateTypeDeclaration(type, IntermediateType.ARRAY);
    }

    /**
     * Generate an empty function with comments.
     *
     * @param function Function prototype
     * @return source code of a empty function
     */
    public static String generateEmptyFunction(final Function function) {
        return "public class Solution {\n"
                + FunctionGenerator.generateFunction(function, 1) + "}\n";
    }
}
