package be.thomaswinters.textgeneration.domain.generators.collection;

import be.thomaswinters.random.Picker;
import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.declarationrequirement.DisjunctionRequirement;
import be.thomaswinters.textgeneration.domain.declarationrequirement.IDeclarationRequirement;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ADisjunctionTextGenerator<E extends ITextGenerator> extends ACollectionTextGenerator<E> {

    /*-********************************************-*
     *  Constructor & factory methods
     *-********************************************-*/

    public ADisjunctionTextGenerator(Collection<? extends E> generators) {
        super(generators);
    }
    /*-********************************************-*/

    @Override
    public String generate(ITextGeneratorContext context) {
        return pickGenerator(getPossibleGenerators(context), context)
                .generate(context);
    }

    protected List<E> getPossibleGenerators(ITextGeneratorContext context) {
        return getGenerators().stream().filter(e -> e.isAllowedToGenerate(context)).collect(Collectors.toList());

    }

    protected abstract ITextGenerator pickGenerator(List<E> generators, ITextGeneratorContext parameters);

    /*-********************************************-*
     *  Testing purposes
     *-********************************************-*/
    public static void setSeed(long number) {
        Picker.setSeed(number);
    }

    @Override
    public long getAmountOfPossibleGenerations(ITextGeneratorContext parameters) {
        try {
            return getGenerators().stream().mapToLong(e -> e.getAmountOfPossibleGenerations(parameters)).sum();
        } catch (StackOverflowError e) {
            return getGenerators().size();
        }
    }

    /*-********************************************-*/

    @Override
    public Stream<String> getAllGenerations(ITextGeneratorContext parameters) {
        return getGenerators().stream().flatMap(e -> e.getAllGenerations(parameters));
    }

    @Override
    public IDeclarationRequirement getRequirements() {
        return new DisjunctionRequirement(
                getChildren().stream().map(e -> e.getRequirements()).collect(Collectors.toSet()));
    }
}
