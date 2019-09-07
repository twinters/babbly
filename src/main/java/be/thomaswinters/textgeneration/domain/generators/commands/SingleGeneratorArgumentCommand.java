package be.thomaswinters.textgeneration.domain.generators.commands;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.TextGeneratorDecorator;

import java.util.stream.Stream;

public abstract class SingleGeneratorArgumentCommand extends TextGeneratorDecorator implements ITextGeneratorCommand {

    public SingleGeneratorArgumentCommand(ITextGenerator generator) {
        super(generator);
    }

    @Override
    public String generate(ITextGeneratorContext parameters) {
        return apply(super.generate(parameters), parameters);
    }

    @Override
    public Stream<String> getAllGenerations(ITextGeneratorContext parameters) {
        return getGenerator().getAllGenerations(parameters).map(e -> apply(e, parameters));
    }

    @Override
    public String toCode() { // TODO fix
        return "\\" + getName() + "(" + getGenerator() + ")";
    }

    /**
     * Applies what it has to do
     */
    public abstract String apply(String generatedString, ITextGeneratorContext parameters);

    public abstract String getName();

}