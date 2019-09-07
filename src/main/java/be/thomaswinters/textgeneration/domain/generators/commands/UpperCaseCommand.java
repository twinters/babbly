package be.thomaswinters.textgeneration.domain.generators.commands;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

public class UpperCaseCommand extends SingleGeneratorArgumentCommand {

    public UpperCaseCommand(ITextGenerator generator) {
        super(generator);
    }

    @Override
    public String apply(String string, ITextGeneratorContext parameters) {
        return string.toUpperCase();
    }

    @Override
    public String getName() {
        return "upper";
    }

}
