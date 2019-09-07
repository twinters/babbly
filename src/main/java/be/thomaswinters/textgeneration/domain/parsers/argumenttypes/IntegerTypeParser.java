package be.thomaswinters.textgeneration.domain.parsers.argumenttypes;

import be.thomaswinters.textgeneration.domain.functionheader.arguments.ArgumentTypes;

public class IntegerTypeParser extends ArgumentTypeParser {

    public IntegerTypeParser() {
        super(ArgumentTypes.INTEGER_TYPE);
    }

    @Override
    public Integer parse(String string) {
        if (!string.matches("[0-9]+")) {
            throw new IllegalArgumentException("Not a number: " + string);
        }
        return Integer.parseInt(string);
    }

}
