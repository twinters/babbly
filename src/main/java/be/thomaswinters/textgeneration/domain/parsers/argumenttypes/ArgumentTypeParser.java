package be.thomaswinters.textgeneration.domain.parsers.argumenttypes;

import be.thomaswinters.textgeneration.domain.functionheader.arguments.IArgumentType;

public abstract class ArgumentTypeParser {
    private final IArgumentType type;

    public ArgumentTypeParser(IArgumentType type) {
        this.type = type;
    }

    public IArgumentType getType() {
        return type;
    }

    public abstract Object parse(String string);
}
