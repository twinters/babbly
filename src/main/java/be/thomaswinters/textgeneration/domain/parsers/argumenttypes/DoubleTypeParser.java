package be.thomaswinters.textgeneration.domain.parsers.argumenttypes;

import be.thomaswinters.textgeneration.domain.functionheader.arguments.ArgumentTypes;

public class DoubleTypeParser extends ArgumentTypeParser {

    public DoubleTypeParser() {
        super(ArgumentTypes.DOUBLE_TYPE);
    }

    @Override
    public Double parse(String string) {
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
            throw new IllegalArgumentException("Not a number: " + string);
        }
    }

}
