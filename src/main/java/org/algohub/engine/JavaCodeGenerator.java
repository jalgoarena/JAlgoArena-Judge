package org.algohub.engine;

import com.google.common.collect.ImmutableMap;

import org.algohub.engine.judge.Function;
import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.TypeNode;

/**
 * Generate compilable and runnable Java code.
 */
final class JavaCodeGenerator {

    private static final String CUSTOM_IMPORT = "import java.util.*;\n" +
            "import org.algohub.engine.type.*;\n\n";

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
     * Generate an empty function with comments.
     *
     * @param function Function prototype
     * @return source code of a empty function
     */
    static String generateEmptyFunction(final Function function) {
        return CUSTOM_IMPORT + "public class Solution {\n"
                + generateFunction(function, 1) + "}\n";
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

    private static String generateParameterDeclaration(final TypeNode type,
                                                       final String parameterName) {
        final StringBuilder result = new StringBuilder();
        final String typeDeclaration = generateTypeDeclaration(type);

        result.append(typeDeclaration).append(' ').append(parameterName);
        return result.toString();
    }

    private static String generateFunction(final Function function,
                                           final int indent) {
        final StringBuilder result = new StringBuilder();

        functionComment(function, indent, result);
        functionBody(function, indent, result);
        deleteUnnecessaryLastComma(indent, result);

        return result.toString();
    }

    private static void deleteUnnecessaryLastComma(int indent, StringBuilder result) {
        result.delete(result.length() - 2, result.length());
        result.append(") {\n");
        appendIndentation(result, "// Write your code here\n", indent + 1);
        appendIndentation(result, "}\n", indent);
    }

    private static void functionBody(Function function, int indent, StringBuilder result) {
        appendIndentation(result, "public ", indent);
        result.append(generateTypeDeclaration(function.getReturnStatement().getType()));
        result.append(" ").append(function.getName()).append("(");
        for (final Function.Parameter p : function.getParameters()) {
            result.append(generateParameterDeclaration(p.getType(), p.getName()))
                    .append(", ");
        }
    }

    private static void functionComment(Function function, int indent, StringBuilder result) {
        appendIndentation(result, "/**\n", indent);
        for (final Function.Parameter p : function.getParameters()) {
            appendIndentation(result, " * @param " + p.getName() + " " + p.getComment() + "\n", indent);
        }
        appendIndentation(result, " * @return " + function.getReturnStatement().getComment() + "\n", indent);
        appendIndentation(result, " */\n", indent);
    }

    private static void appendIndentation(final StringBuilder sb, final String content,
                                          final int indent) {
        for (int i = 0; i < indent; ++i) {
            sb.append("    ");
        }
        sb.append(content);
    }
}
