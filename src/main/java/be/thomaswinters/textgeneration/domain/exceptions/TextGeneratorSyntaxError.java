package be.thomaswinters.textgeneration.domain.exceptions;

import be.thomaswinters.textgeneration.domain.parsers.util.StaticStringReader;

public class TextGeneratorSyntaxError extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -5142639172691282943L;
    private static String DEFAULT_MESSAGE = "There was a syntax error in the following Text Generator:\n";
    private final StaticStringReader textGeneratorString;

    public TextGeneratorSyntaxError(StaticStringReader generatorStringReader,
                                    String errorMessage) {
        super(
                DEFAULT_MESSAGE
                        + "Generator:"
                        + generatorStringReader.getWholeString() + "\n"
                        + ((errorMessage != null && !errorMessage.equals("")) ? "Error: "
                        + errorMessage
                        : "No error specified."));
        this.textGeneratorString = generatorStringReader;
    }

    public String getTextGeneratorString() {
        return textGeneratorString.getWholeString();
    }
}
