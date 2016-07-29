package org.algohub.engine.judge;

class CreateFriendlyMessage {

    String from(final String errorMessage) {
        final StringBuilder sb = new StringBuilder();
        final String[] lines = errorMessage.split("\n");
        for (final String line : lines) {
            final int pos = line.indexOf(".java:");
            if (pos > 0) {
                appendFriendlyMessage(sb, line, pos);
            } else {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    private void appendFriendlyMessage(StringBuilder sb, String line, int pos) {
        final int pos2 = line.indexOf(':', pos + ".java:".length());
        final String friendlyMessage = String.format(
                "Line:%d%s",
                getLineNumber(line, pos, pos2),
                line.substring(pos2)
        );
        sb.append(friendlyMessage).append('\n');
    }

    private int getLineNumber(String line, int pos, int pos2) {
        final int lineNumber;
        {
            final String numberStr = line.substring(pos + ".java:".length(), pos2);
            lineNumber = Integer.valueOf(numberStr);
        }
        return lineNumber;
    }
}
