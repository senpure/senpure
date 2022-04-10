package com.senpure.grammar.antlr;
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
public class GrammarErrorListener extends BaseErrorListener {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String filePath;
    private StringBuilder errorBuilder = new StringBuilder();

    public GrammarErrorListener(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);

        logger.error("{}: line {} : {} {}", filePath, line, charPositionInLine, msg);
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
