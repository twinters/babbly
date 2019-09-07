package be.thomaswinters.textgeneration.domain.generators.commands;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

public interface ITextGeneratorCommand extends ITextGenerator {
    @Override
    default int getPriority() {
        return 80;
    }

}
