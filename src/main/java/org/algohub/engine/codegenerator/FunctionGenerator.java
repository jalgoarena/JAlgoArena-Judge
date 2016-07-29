package org.algohub.engine.codegenerator;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.TypeNode;

/**
 * Generate function declaration for display.
 */
class FunctionGenerator {

    private FunctionGenerator() {
        // static class
    }

    private static String generateParameterDeclaration(final TypeNode type,
                                                       final String parameterName) {
        final StringBuilder result = new StringBuilder();
        final String typeDeclaration = generateTypeDeclaration(type);

        result.append(typeDeclaration).append(' ').append(parameterName);
        return result.toString();
    }

    static String generateTypeDeclaration(final TypeNode type) {
        if (!type.isContainer()) {
            return TypeMap.JAVA_TYPE_MAP.get(type.getValue());
        }
        final boolean isArray =
                type.getValue() == IntermediateType.ARRAY;

        if (isArray) {
            return generateTypeDeclaration(type.getElementType()) + "[]";
        } else {
            final String containerTypeStr = TypeMap.JAVA_TYPE_MAP.get(type.getValue());
            if (type.getKeyType() != null) {
                return containerTypeStr + "<" + generateTypeDeclaration(type.getKeyType()) + "," + generateTypeDeclaration(type.getElementType())
                        + ">";
            } else {
                return containerTypeStr + "<" + generateTypeDeclaration(type.getElementType()) + ">";
            }
        }
    }

    static String generateFunction(final Function function,
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
