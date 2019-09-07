package be.thomaswinters.textgeneration.domain.generators.databases;

import be.thomaswinters.textgeneration.domain.constraints.LockConstraint;
import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.context.LinkedTextGeneratorContext;
import be.thomaswinters.textgeneration.domain.context.TextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.TextGeneratorDecorator;
import be.thomaswinters.textgeneration.domain.generators.named.NamedGeneratorRegister;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeclarationFileTextGenerator extends TextGeneratorDecorator {

    private final Collection<LockConstraint> constraints;
    private final NamedGeneratorRegister namedRegister;
    private final boolean ignoreGeneratorsUsingMissingDeclarations;

    public DeclarationFileTextGenerator(ITextGenerator generator, NamedGeneratorRegister namedRegister,
                                        Collection<? extends LockConstraint> constraints, boolean ignoreGeneratorsUsingMissingDeclarations) {
        super(generator);
        this.constraints = ImmutableSet.copyOf(constraints);
        this.namedRegister = namedRegister;
        this.ignoreGeneratorsUsingMissingDeclarations = ignoreGeneratorsUsingMissingDeclarations;
    }

    public DeclarationFileTextGenerator(ITextGenerator generator, NamedGeneratorRegister namedRegister,
                                        Collection<? extends LockConstraint> constraints) {
        this(generator, namedRegister, constraints, false);
    }

    public DeclarationFileTextGenerator(ITextGenerator generator, NamedGeneratorRegister namedRegister) {
        this(generator, namedRegister, ImmutableSet.of());
    }

    /**
     * Override these if you want to modulate the generation.
     */
    @Override
    public String generate(ITextGeneratorContext parameters) {
        ITextGeneratorContext ownParameters = getInitialContext(parameters);
        return getGenerator().generate(ownParameters);
    }

    public String generate(String namedGeneratorName, ITextGeneratorContext parameters) {
        if (!namedRegister.hasGenerator(namedGeneratorName)) {
            throw new IllegalStateException("No named generator with name " + namedGeneratorName + " present in " + this);
        }
        ITextGeneratorContext ownParameters = getInitialContext(parameters);
        return namedRegister.getGenerator(namedGeneratorName).generate(ownParameters);
    }

    @Override
    public Stream<String> getAllGenerations(ITextGeneratorContext parameters) {

        ITextGeneratorContext ownParameters = getInitialContext(parameters);
        return getGenerator().getAllGenerations(ownParameters);
    }

    public ITextGeneratorContext getInitialContext() {
        return new TextGeneratorContext(this.constraints, this.namedRegister,
                this.ignoreGeneratorsUsingMissingDeclarations);

    }

    public ITextGeneratorContext getInitialContext(ITextGeneratorContext givenContext) {
        return new LinkedTextGeneratorContext(getInitialContext(), givenContext);
    }

    public void addNamedDeclaration(String name, ITextGenerator generator) {
        this.namedRegister.createGenerator(name, generator);
    }

    @Override
    public String toCode() {
        StringBuilder builder = new StringBuilder();

        builder.append("//Variables\n");
        builder.append(
                namedRegister.getAllDeclarations().stream().map(e -> e.toCode()).collect(Collectors.joining("\n")));

        // builder.append("\n\n//Combinations\n");
        // builder.append("main = " + getGenerator().toCode());
        return builder.toString();
    }
}
