package org.algohub.engine.judge;

class CreateFriendlyMessage {

    String from(final String errorMessage) {
        final StringBuilder sb = new StringBuilder();
        final String[] lines = errorMessage.split("\n");
        for (final String line : lines) {
            final int pos = line.indexOf(".java:");
            if (pos > 0) {
                // get the line number
                final int pos2 = line.indexOf(':', pos + ".java:".length());
                final int lineNumber;
                {
                    final String numberStr = line.substring(pos + ".java:".length(), pos2);
                    lineNumber = Integer.valueOf(numberStr);
                }
                final String friendlyMessage = "Line:" + lineNumber + line.substring(pos2);
                sb.append(friendlyMessage).append('\n');
            } else {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }
}
