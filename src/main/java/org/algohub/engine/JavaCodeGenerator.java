package org.algohub.engine;

import com.google.common.collect.ImmutableMap;

import org.algohub.engine.judge.Function;
import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.TypeNode;

final class JavaCodeGenerator {

    private static final String CUSTOM_IMPORT = "import java.util.*;\n" +
            "import org.algohub.engine.type.*;\n\n";

    private static final ImmutableMap<IntermediateType, String> INTERMEDIATE_TYPE_TO_JAVA_TYPE_MAP =
            ImmutableMap.<IntermediateType, String>builder().put(IntermediateType.BOOL, "boolean")
                    .put(IntermediateType.STRING, "String")
                    .put(IntermediateType.DOUBLE, "double")
                    .put(IntermediateType.INT, "int")
                    .put(IntermediateType.LONG, "long")
                    .put(IntermediateType.ARRAY, "[]")
                    .put(IntermediateType.LIST, "ArrayList")
                    .put(IntermediateType.SET, "HashSet")
                    .put(IntermediateType.MAP, "HashMap")
                    .put(IntermediateType.LINKED_LIST_NODE, "LinkedListNode")
                    .build();

    private static final ImmutableMap<IntermediateType, String> INTERMEDIATE_TYPE_TO_JAVA_BOXING_CLASS_MAP =
            ImmutableMap.<IntermediateType, String>builder().put(IntermediateType.BOOL, "Boolean")
                    .put(IntermediateType.STRING, "String")
                    .put(IntermediateType.DOUBLE, "Double")
                    .put(IntermediateType.INT, "Integer")
                    .put(IntermediateType.LONG, "Long").build();

    private JavaCodeGenerator() {
    }

    /**
     * Generate an empty function with comments.
     *
     * @param function Function prototype
     * @return source code of a empty function
     */
    static String generateEmptyFunction(final Function function) {
        return String.format(
                "%spublic class Solution {\n%s}\n",
                CUSTOM_IMPORT,
                generateFunction(function)
        );
    }

    /**
     * Convert a TypeNode to a Java type declaration.
     * <p>
     * <p> post order, recursive.</p>
     */
    private static String generateJavaTypeDeclaration(final TypeNode type,
                                                      final IntermediateType parentType) {
        if (!type.isContainer()) {
            if (parentType == IntermediateType.ARRAY) {
                return INTERMEDIATE_TYPE_TO_JAVA_TYPE_MAP.get(type.getValue());
            } else {
                return INTERMEDIATE_TYPE_TO_JAVA_BOXING_CLASS_MAP.get(type.getValue());
            }
        }

        if (type.getValue() == IntermediateType.ARRAY) {
            return javaArrayDeclaration(type);
        } else {
            final String containerTypeStr = INTERMEDIATE_TYPE_TO_JAVA_TYPE_MAP.get(type.getValue());
            if (type.getKeyType() != null) {
                return javaMapDeclaration(type, containerTypeStr);
            } else {
                return javaGenericTypeDeclaration(type, containerTypeStr);
            }
        }
    }

    private static String javaArrayDeclaration(TypeNode type) {
        return String.format(
                "%s[]",
                generateJavaTypeDeclaration(type.getElementType(), type.getValue())
        );
    }

    private static String javaGenericTypeDeclaration(TypeNode type, String containerTypeStr) {
        return String.format(
                "%s<%s>",
                containerTypeStr,
                generateJavaTypeDeclaration(type.getElementType(), type.getValue())
        );
    }

    private static String javaMapDeclaration(TypeNode type, String containerTypeStr) {
        return String.format(
                "%s<%s,%s>",
                containerTypeStr,
                generateJavaTypeDeclaration(type.getKeyType(), type.getValue()),
                generateJavaTypeDeclaration(type.getElementType(), type.getValue())
        );
    }

    /**
     * Generate a type declaration.
     * The parent type should be ARRAY by default
     * <p>
     * <p>post order, recursive.</p>
     *
     * @param type the type
     * @return type declaration
     */
    static String generateJavaTypeDeclaration(final TypeNode type) {
        return generateJavaTypeDeclaration(type, IntermediateType.ARRAY);
    }

    private static String generateParameterDeclaration(final TypeNode type, final String parameterName) {
        final String typeDeclaration = generateJavaTypeDeclaration(type);
        return String.format("%s %s", typeDeclaration, parameterName);
    }

    private static String generateFunction(final Function function) {
        final StringBuilder result = new StringBuilder();

        functionComment(function, result);
        functionBody(function, result);
        deleteUnnecessaryLastComma(result);

        return result.toString();
    }

    private static void deleteUnnecessaryLastComma(StringBuilder result) {
        result.delete(result.length() - 2, result.length());
        result.append(") {\n");
        appendIndentation(result, "    // Write your code here\n");
        appendIndentation(result, "}\n");
    }

    private static void functionBody(Function function, StringBuilder result) {
        appendIndentation(result, "public ");
        result.append(generateJavaTypeDeclaration(function.getReturnStatement().getType()));
        result.append(" ").append(function.getName()).append("(");

        for (final Function.Parameter p : function.getParameters()) {
            result.append(generateParameterDeclaration(p.getType(), p.getName()))
                    .append(", ");
        }
    }

    private static void functionComment(Function function, StringBuilder result) {
        appendIndentation(result, "/**\n");

        for (final Function.Parameter p : function.getParameters()) {
            appendIndentation(result, " * @param " + p.getName() + " " + p.getComment() + "\n");
        }

        appendIndentation(result, " * @return " + function.getReturnStatement().getComment() + "\n");
        appendIndentation(result, " */\n");
    }

    private static void appendIndentation(final StringBuilder sourceCode, final String sourceCodeLine) {
        sourceCode.append("    ");
        sourceCode.append(sourceCodeLine);
    }
}
