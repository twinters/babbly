package be.thomaswinters.textgeneration.domain.generators.named;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

public class NamedGeneratorDeclaration {

    private final String name;
    private final ITextGenerator generator;

    public NamedGeneratorDeclaration(String name, ITextGenerator generator) {
        this.name = name;
        this.generator = generator;
    }

    public String getName() {
        return name;
    }

    public ITextGenerator getGenerator() {
        return generator;
    }

    public String toCode() {
        return getName() + "=" + generator.toCode();
    }
}
