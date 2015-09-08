package com.rivetlogic.migration.impl.scanner;

import com.rivetlogic.migration.api.scanner.Scanner;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>ContentScanner</p>
 */
public abstract class ContentScanner implements Scanner {

    final static Logger LOGGER = Logger.getLogger(ContentScanner.class);

    public StringBuffer scan(StringBuffer content) {
        Pattern pattern = getPattern();
        if (pattern != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Running the content through the pattern: " + pattern);
            }
            StringBuffer result = new StringBuffer();
            Matcher matcher = pattern.matcher(content);
            // scan the content and find all content matching the provided target pattern
            // and replace the URL within by attaching sandbox id parameter
            while (matcher.find()) {
                String match = matcher.group();
                String target = matcher.group(getTargetGroup());
                int index = match.indexOf(target);
                String front = match.substring(0, index);
                String end = match.substring(index + target.length());
                String replacement = transform(front, target, end, match);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Match found from " + match + " to " + replacement);
                }
                matcher.appendReplacement(result, replacement);
            }
            matcher.appendTail(result);
            return result;
        } else {
            return content;
        }
    }

    /**
     * <p>Method that will handle the transformation when there's a match</p>
     *
     * @param front a {@link java.lang.String} object
     * @param target a {@link java.lang.String} object
     * @param end a {@link java.lang.String} object
     * @param match a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    protected abstract String transform(String front, String target, String end, String match);

    /**
     * <p>get a pattern</p>
     *
     * @return a {@link java.util.regex.Pattern} object
     */
    protected abstract Pattern getPattern();

    /**
     * <p>get a target group in a match</p>
     *
     * @return an int
     */
    protected abstract int getTargetGroup();

}
