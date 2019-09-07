package be.thomaswinters.textgeneration.domain.parsers.argumenttypes;

import be.thomaswinters.textgeneration.domain.functionheader.arguments.ArgumentTypes;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.TextGeneratorPool;

public class LockedGeneratorTypeParser extends ArgumentTypeParser {

    public LockedGeneratorTypeParser() {
        super(ArgumentTypes.LOCKED_GENERATOR_TYPE);
    }

    @Override
    public ITextGenerator parse(String string) {
        if (string.charAt(0) != '['
                || string.charAt(string.length() - 1) != ']') {
            throw new RuntimeException(
                    "Following locked generator is not properly enclosed: "
                            + string);
        }

        if (!string.contains(":")) {
            throw new RuntimeException(
                    "Following locked generator did not specify an identifier: "
                            + string);
        }

        String[] splitted = string.substring(1, string.length() - 1).split(":",
                2);

        String name = splitted[0];
        String identifier = splitted[1];

        return TextGeneratorPool.getLockedGeneration(name, identifier);
    }
}
