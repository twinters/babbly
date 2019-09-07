package be.thomaswinters.textgeneration.domain.generators.commands;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.declarationrequirement.DisjunctionRequirement;
import be.thomaswinters.textgeneration.domain.declarationrequirement.EmptyRequirement;
import be.thomaswinters.textgeneration.domain.declarationrequirement.IDeclarationRequirement;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

import java.util.stream.Stream;

public class IfDefinedCommand extends SingleGeneratorArgumentCommand {
    public IfDefinedCommand(ITextGenerator generator) {
        super(generator);
    }

    /**
     * Generates the empty string if the generator is not allowed to generate due to missing named declarations
     */
    @Override
    public String generate(ITextGeneratorContext context) {
        if (getGenerator().isAllowedToGenerate(context)) {
            return getGenerator().generate(context);
        } else {
            return "";
        }
    }


    @Override
    public long getAmountOfPossibleGenerations(ITextGeneratorContext parameters) {
        if (getGenerator().isAllowedToGenerate(parameters)) {
            return getGenerator().getAmountOfPossibleGenerations(parameters);
        } else {
            return 1;
        }
    }

    @Override
    public Stream<String> getAllGenerations(ITextGeneratorContext context) {
        if (getGenerator().isAllowedToGenerate(context)) {
            return Stream.of(getGenerator().generate(context));
        } else {
            return Stream.of("");
        }
    }

    @Override
    public String apply(String string, ITextGeneratorContext parameters) {
        throw new UnsupportedOperationException("Apply is not supported for ifdefined commands");
    }

    @Override
    public IDeclarationRequirement getRequirements() {
        return new DisjunctionRequirement(getGenerator().getRequirements(), new EmptyRequirement());
    }

    @Override
    public String getName() {
        return "ifdefined";
    }

}
