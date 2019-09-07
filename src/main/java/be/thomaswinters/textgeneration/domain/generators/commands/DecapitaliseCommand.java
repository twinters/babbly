package be.thomaswinters.textgeneration.domain.generators.commands;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

public class DecapitaliseCommand extends SingleGeneratorArgumentCommand {

    public DecapitaliseCommand(ITextGenerator generator) {
        super(generator);
    }

    @Override
    public String apply(String string, ITextGeneratorContext parameters) {
        if (string.length() > 0) {
            return string.substring(0, 1).toLowerCase() + string.substring(1);
        }
        return string;
    }

    @Override
    public String getName() {
        return "decapitalise";
    }

}
