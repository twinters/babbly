package be.thomaswinters.textgeneration.domain.generators;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.declarationrequirement.IDeclarationRequirement;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class TextGeneratorProxy implements ITextGenerator {

    private Optional<ITextGenerator> loadedGenerator;

    public TextGeneratorProxy() {
        loadedGenerator = Optional.empty();
    }

    protected ITextGenerator getGenerator() {
        if (!loadedGenerator.isPresent()) {
            loadedGenerator = loadGenerator();
            if (!loadedGenerator.isPresent()) {
                throw new RuntimeException("An error occured while loading in proxy " + this);
            }
        }
        return loadedGenerator.get();
    }

    @Override
    public String generate(ITextGeneratorContext parameters) {
        return getGenerator().generate(parameters);
    }

    @Override
    public int getPriority() {
        return getGenerator().getPriority();
    }

    protected abstract Optional<ITextGenerator> loadGenerator();

    @Override
    public Collection<ITextGenerator> getChildren() {
        return ImmutableSet.of(getGenerator());
    }

    @Override
    public long getAmountOfPossibleGenerations(ITextGeneratorContext parameters) {
        return getGenerator().getAmountOfPossibleGenerations(parameters);
    }

    @Override
    public Stream<String> getAllGenerations(ITextGeneratorContext parameters) {
        return getGenerator().getAllGenerations(parameters);
    }

    @Override
    public IDeclarationRequirement getRequirements() {
        return getGenerator().getRequirements();
    }

}