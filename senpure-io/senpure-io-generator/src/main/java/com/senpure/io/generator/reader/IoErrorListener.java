package com.senpure.io.generator.reader;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IoErrorListener
 *
 * @author senpure
 * @time 2019-06-06 17:18:55
 */
public class IoErrorListener extends BaseErrorListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String filePath;
    private final StringBuilder errorBuilder = new StringBuilder();

    private final boolean loggerEnable;

    public IoErrorListener(String filePath) {
        this.filePath = filePath;
        loggerEnable = true;
    }

    public IoErrorListener(String filePath, boolean loggerEnable) {
        this.filePath = filePath;
        this.loggerEnable = loggerEnable;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);

        if (loggerEnable) {
            logger.error("{}: line {} : {} {}", filePath, line, charPositionInLine, msg);
        }

        if (errorBuilder.length() > 0) {
            errorBuilder.append("\n");
        }
        errorBuilder.append(filePath).append(":line ").append(line).append(" : ").append(charPositionInLine).append(" ").append(msg);
    }

    public boolean isSyntaxError() {
        return errorBuilder.length() > 0;
    }

    public String getSyntaxErrorMessage() {
        return errorBuilder.toString();
    }
}
