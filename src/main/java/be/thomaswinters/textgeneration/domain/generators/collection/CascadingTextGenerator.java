package be.thomaswinters.textgeneration.domain.generators.collection;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.StaticTextGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Will use the first one if the requirements to do so are fulfilled, will generate using the next one otherwise
 */
public class CascadingTextGenerator extends ADisjunctionTextGenerator<ITextGenerator> {
    private static final int PRIORITY = 40;
    public static final String DIVISION_CHARACTER = "/";

    /*-********************************************-*
     *  Constructor & factory methods
     *-********************************************-*/

    public CascadingTextGenerator(Collection<? extends ITextGenerator> generators) {
        super(generators);
    }

    public CascadingTextGenerator(ITextGenerator... generators) {
        this(Arrays.asList(generators));
    }

    public static CascadingTextGenerator fromStrings(Collection<String> words) {
        return new CascadingTextGenerator(
                words.stream().map(StaticTextGenerator::new).collect(Collectors.toList()));
    }

    /*-********************************************-*/

    private ITextGenerator getPrimaryGenerator(ITextGeneratorContext context) {
        List<ITextGenerator> generators = getPossibleGenerators(context);
        if (generators.isEmpty()) {
            throw new IllegalArgumentException("No valid generator exists in the cascade " + this);
        }
        return generators.get(0);
    }


    @Override
    protected ITextGenerator pickGenerator(List<ITextGenerator> generators, ITextGeneratorContext context) {
        return getPrimaryGenerator(context);
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public String getDivisionCharacter() {
        return DIVISION_CHARACTER;
    }

    @Override
    public long getAmountOfPossibleGenerations(ITextGeneratorContext context) {
        try {
            return getPrimaryGenerator(context).getAmountOfPossibleGenerations(context);
        } catch (StackOverflowError e) {
            throw new IllegalStateException("Loop in amount of possible generations code!", e);
        }
    }

    /*-********************************************-*/

    @Override
    public Stream<String> getAllGenerations(ITextGeneratorContext context) {
        return getPrimaryGenerator(context).getAllGenerations();
    }
}
