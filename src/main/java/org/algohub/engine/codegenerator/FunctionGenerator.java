package org.algohub.engine.codegenerator;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.TypeNode;

import static org.algohub.engine.codegenerator.Indentation.append;

/**
 * Generate function declaration for display.
 */
@SuppressWarnings({"PMD.InsufficientStringBufferDeclaration", "PMD.ConsecutiveLiteralAppends",
        "PMD.InefficientStringBuffering", "PMD.UseUtilityClass", "PMD.ConsecutiveAppendsShouldReuse"})
public class FunctionGenerator {

    public static String generateParameterDeclaration(final TypeNode type,
                                                      final String parameterName) {
        final StringBuilder result = new StringBuilder();
        final String typeDeclaration = generateTypeDeclaration(type);

        result.append(typeDeclaration).append(' ').append(parameterName);
        return result.toString();
    }

    public static String generateTypeDeclaration(final TypeNode type) {
        if (!type.isContainer()) {
            return TypeMap.JAVA_TYPE_MAP.get(type.getValue());
        }
        final boolean isArray =
                type.getValue() == IntermediateType.ARRAY;

        if (isArray) {
            return generateTypeDeclaration(type.getElementType().get()) + "[]";
        } else {
            final String containerTypeStr = TypeMap.JAVA_TYPE_MAP.get(type.getValue());
            if (type.getKeyType().isPresent()) {
                return containerTypeStr + "<" + generateTypeDeclaration(type.getKeyType().get()) + "," + generateTypeDeclaration(type.getElementType().get())
                        + ">";
            } else {
                return containerTypeStr + "<" + generateTypeDeclaration(type.getElementType().get()) + ">";
            }
        }
    }

    public static String generateFunction(final Function function,
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
        append(result, "// Write your code here\n", indent + 1);
        append(result, "}\n", indent);
    }

    private static void functionBody(Function function, int indent, StringBuilder result) {
        append(result, "public ", indent);
        result.append(generateTypeDeclaration(function.getReturn_().getType()));
        result.append(" " + function.getName() + "(");
        for (final Function.Parameter p : function.getParameters()) {
            result.append(generateParameterDeclaration(p.getType(), p.getName()))
                    .append(", ");
        }
    }

    private static void functionComment(Function function, int indent, StringBuilder result) {
        append(result, "/**\n", indent);
        for (final Function.Parameter p : function.getParameters()) {
            append(result, " * @param " + p.getName() + " " + p.getComment() + "\n", indent);
        }
        append(result, " * @return " + function.getReturn_().getComment() + "\n", indent);
        append(result, " */\n", indent);
    }
}
