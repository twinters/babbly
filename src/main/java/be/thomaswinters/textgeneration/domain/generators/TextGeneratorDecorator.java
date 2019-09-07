package be.thomaswinters.textgeneration.domain.generators;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.declarationrequirement.IDeclarationRequirement;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.stream.Stream;

public abstract class TextGeneratorDecorator implements ITextGenerator {
    private final ITextGenerator generator;

    public TextGeneratorDecorator(ITextGenerator generator) {
        this.generator = generator;
    }

    public ITextGenerator getGenerator() {
        return generator;
    }

    /**
     * Override these if you want to modulate the generation.
     */
    @Override
    public String generate(ITextGeneratorContext parameters) {
        return generator.generate(parameters);
    }

    @Override
    public Collection<ITextGenerator> getChildren() {
        return ImmutableSet.of(generator);
    }

    @Override
    public long getAmountOfPossibleGenerations(ITextGeneratorContext parameters) {
        return getGenerator().getAmountOfPossibleGenerations(parameters);
    }

    @Override
    public String toString() {
        return toCode();
    }

    @Override
    public Stream<String> getAllGenerations(ITextGeneratorContext parameters) {
        return generator.getAllGenerations(parameters);
    }

    @Override
    public IDeclarationRequirement getRequirements() {
        return getGenerator().getRequirements();
    }
}
