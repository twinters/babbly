package be.thomaswinters.textgeneration.domain.parsers.argumenttypes;

import be.thomaswinters.textgeneration.domain.functionheader.arguments.ArgumentTypes;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.parsers.TextGeneratorParser;

import java.io.IOException;

public class TextGeneratorTypeParser extends ArgumentTypeParser {

    private final TextGeneratorParser parser;

    public TextGeneratorTypeParser(TextGeneratorParser parser) {
        super(ArgumentTypes.TEXT_GENERATOR_TYPE);
        this.parser = parser;
    }

    @Override
    public ITextGenerator parse(String string) {
        try {
            return parser.parseGenerator(string);
        } catch (IOException e) {
            throw new RuntimeException("An error occured: " + e.getMessage());
        }
    }
}
