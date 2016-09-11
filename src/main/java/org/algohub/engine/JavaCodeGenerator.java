package org.algohub.engine;

import org.algohub.engine.judge.Function;

final class JavaCodeGenerator {

    private static final String CUSTOM_IMPORT = String.format("%s%n%s%n%n",
            "import java.util.*;",
            "import org.algohub.engine.type.*;"
    );

    private JavaCodeGenerator() {
        // static class
    }

    /**
     * Generate an empty function with comments.
     *
     * @param function Function prototype
     * @return source code of a empty function
     */
    static String generateEmptyFunction(final Function function) throws ClassNotFoundException {
        return String.format(
                "%spublic class Solution {%n%s}%n",
                CUSTOM_IMPORT,
                generateFunction(function)
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
    private static String generateJavaTypeDeclaration(final String type) throws ClassNotFoundException {
        return "void".equals(type) ? type : classOrPrimitiveName(type);
    }

    private static String classOrPrimitiveName(final String type)  throws ClassNotFoundException {
        String typeName = Class.forName(type).getSimpleName();

        switch (typeName) {
            case "Boolean":
                return boolean.class.getSimpleName();
            case "Long":
                return long.class.getSimpleName();
            case "Integer":
                return int.class.getSimpleName();
            case "Short":
                return short.class.getSimpleName();
            case "Double":
                return double.class.getSimpleName();
            case "Float":
                return float.class.getSimpleName();
            default:
                return typeName;
        }
    }

    private static String generateParameterDeclaration(final String type, final String parameterName) throws ClassNotFoundException {
        final String typeDeclaration = generateJavaTypeDeclaration(type);
        return String.format("%s %s", typeDeclaration, parameterName);
    }

    private static String generateFunction(final Function function) throws ClassNotFoundException {
        final StringBuilder result = new StringBuilder();

        functionComment(function, result);
        functionBody(function, result);
        deleteUnnecessaryLastComma(result);

        return result.toString();
    }

    private static void deleteUnnecessaryLastComma(StringBuilder result) {
        result.delete(result.length() - 2, result.length());
        result.append(String.format(") {%n"));
        appendIndentation(result, String.format("    // Write your code here%n"));
        appendIndentation(result, String.format("}%n"));
    }

    private static void functionBody(Function function, StringBuilder result) throws ClassNotFoundException {
        appendIndentation(result, "public ");
        result.append(generateJavaTypeDeclaration(function.getReturnStatement().getType()));
        result.append(" ").append(function.getName()).append("(");

        for (final Function.Parameter p : function.getParameters()) {
            result.append(generateParameterDeclaration(p.getType(), p.getName()))
                    .append(", ");
        }
    }

    private static void functionComment(Function function, StringBuilder result) {
        appendIndentation(result, String.format("/**%n"));

        for (final Function.Parameter p : function.getParameters()) {
            appendIndentation(result, String.format(" * @param %s %s%n", p.getName(), p.getComment()));
        }

        appendIndentation(result, String.format(" * @return %s%n", function.getReturnStatement().getComment()));
        appendIndentation(result, String.format(" */%n"));
    }

    private static void appendIndentation(final StringBuilder sourceCode, final String sourceCodeLine) {
        sourceCode.append("    ");
        sourceCode.append(sourceCodeLine);
    }
}
