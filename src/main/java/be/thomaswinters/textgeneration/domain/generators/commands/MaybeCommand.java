package be.thomaswinters.textgeneration.domain.generators.commands;

import be.thomaswinters.random.Picker;
import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.declarationrequirement.DisjunctionRequirement;
import be.thomaswinters.textgeneration.domain.declarationrequirement.EmptyRequirement;
import be.thomaswinters.textgeneration.domain.declarationrequirement.IDeclarationRequirement;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

import java.util.Collections;
import java.util.stream.Stream;

public class MaybeCommand extends SingleGeneratorArgumentCommand {

    private final double chance;

    public MaybeCommand(ITextGenerator generator, double chance) {
        super(generator);
        this.chance = chance;
    }

    /**
     * Generates the empty string if the generator is not allowed to generate due to missing named declarations
     */
    @Override
    public String generate(ITextGeneratorContext context) {
        if (getGenerator().isAllowedToGenerate(context)) {
            return apply(super.generate(context), context);
        } else {
            return "";
        }
    }

    @Override
    public String apply(String string, ITextGeneratorContext parameters) {
        return Picker.chanceEvent(string, e -> e, "", chance);
    }

    @Override
    public Stream<String> getAllGenerations(ITextGeneratorContext parameters) {
        return Stream.concat(getGenerator().getAllGenerations(parameters), Collections.singletonList("").stream());
    }

    @Override
    public IDeclarationRequirement getRequirements() {
        return new DisjunctionRequirement(getGenerator().getRequirements(), new EmptyRequirement());
    }

    @Override
    public String getName() {
        return "maybe";
    }

    @Override
    public String toCode() {
        return "(" + getGenerator() + "){"+chance+"}";
    }

}
