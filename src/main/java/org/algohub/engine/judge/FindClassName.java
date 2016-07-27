package org.algohub.engine.judge;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class FindClassName {

    private static final Pattern[] PATTERNS_FOR_FINDING_CLASS_NAME =
            new Pattern[]{Pattern.compile("public\\s+class\\s+(\\w+)\\s+"),
                    Pattern.compile("final\\s+public\\s+class\\s+(\\w+)\\s+"),
                    Pattern.compile("public\\s+final\\s+class\\s+(\\w+)\\s+"),};

    Optional<String> in(final String javaCode) {
        for (final Pattern pattern : PATTERNS_FOR_FINDING_CLASS_NAME) {
            final Matcher matcher = pattern.matcher(javaCode);
            if (matcher.find()) {
                return Optional.of(matcher.group(1));
            }
        }
        return Optional.empty();
    }
}
