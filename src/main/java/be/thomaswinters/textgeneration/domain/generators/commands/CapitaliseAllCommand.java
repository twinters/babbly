package be.thomaswinters.textgeneration.domain.generators.commands;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import org.apache.commons.lang3.text.WordUtils;

public class CapitaliseAllCommand extends SingleGeneratorArgumentCommand {

    public CapitaliseAllCommand(ITextGenerator generator) {
        super(generator);
    }

    @Override
    public String apply(String string, ITextGeneratorContext parameters) {
        String capitalized = WordUtils.capitalize(string);
        return capitalized;
    }

    @Override
    public String getName() {
        return "capitaliseAll";
    }

}
