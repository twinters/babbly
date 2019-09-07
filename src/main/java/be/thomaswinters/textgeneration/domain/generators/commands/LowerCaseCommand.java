package be.thomaswinters.textgeneration.domain.generators.commands;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

public class LowerCaseCommand extends SingleGeneratorArgumentCommand {

    public LowerCaseCommand(ITextGenerator generator) {
        super(generator);
    }

    @Override
    public String apply(String string, ITextGeneratorContext parameters) {
        return string.toLowerCase();
    }

    @Override
    public String getName() {
        return "lower";
    }

}
