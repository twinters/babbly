package be.thomaswinters.textgeneration.domain.generators.named;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.declarationrequirement.IDeclarationRequirement;
import be.thomaswinters.textgeneration.domain.declarationrequirement.SingleNameRequirement;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.stream.Stream;

public class NamedTextGenerator implements ITextGenerator {

    public static final int PRIORITY = 60;

    private final String name;

    public NamedTextGenerator(String declaration) {
        this.name = declaration;
    }

    public String getDeclaration() {
        return name;
    }

    @Override
    public String generate(ITextGeneratorContext parameters) {
        return parameters
                .getGenerator(name)
                .generate(parameters);
    }

    @Override
    public String toCode() {
        return "<" + name + ">";
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public Collection<ITextGenerator> getChildren() {
        return ImmutableSet.of();
    }

    @Override
    public long getAmountOfPossibleGenerations(ITextGeneratorContext parameters) {
        if (parameters.ignoreGeneratorsUsingMissingDeclarations() && !parameters.hasGenerator(name)) {
            return 0;
        }
        return parameters.getGenerator(name).getAmountOfPossibleGenerations(parameters);
    }

    @Override
    public String toString() {
        return toCode();
    }

    @Override
    public Stream<String> getAllGenerations(ITextGeneratorContext parameters) {
        return parameters.getGenerator(name).getAllGenerations(parameters);
    }

    @Override
    public IDeclarationRequirement getRequirements() {
        return new SingleNameRequirement(name);
    }
}
