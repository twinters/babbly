package be.thomaswinters.textgeneration.domain.generators.collection;

import be.thomaswinters.random.Picker;
import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.StaticTextGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DisjunctionTextGenerator extends ADisjunctionTextGenerator<ITextGenerator> {
    private static final int PRIORITY = 50;
    public static final String DIVISION_CHARACTER = "||";

    /*-********************************************-*
     *  Constructor & factory methods
     *-********************************************-*/

    public DisjunctionTextGenerator(Collection<? extends ITextGenerator> generators) {
        super(generators);
    }

    public DisjunctionTextGenerator(ITextGenerator... generators) {
        this(Arrays.asList(generators));
    }

    public static DisjunctionTextGenerator fromStrings(Collection<String> words) {
        return new DisjunctionTextGenerator(
                words.stream().map(e -> new StaticTextGenerator(e)).collect(Collectors.toList()));
    }

    /*-********************************************-*/

    @Override
    protected ITextGenerator pickGenerator(List<ITextGenerator> generators, ITextGeneratorContext parameters) {
        return Picker.pick(generators);
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public String getDivisionCharacter() {
        return DIVISION_CHARACTER;
    }
}
