package be.thomaswinters.textgeneration.domain.generators.collection;


import be.thomaswinters.random.Picker;
import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.weighted.GenerationsWeightGenerator;
import be.thomaswinters.textgeneration.domain.generators.weighted.IWeightedGenerator;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WeightedDisjunctionTextGenerator extends ADisjunctionTextGenerator<IWeightedGenerator> {
    private static final int PRIORITY = 60;
    public static final String DIVISION_CHARACTER = "|";

    /*-********************************************-*
     *  Constructor & factory methods
     *-********************************************-*/
    // private boolean useEqualWeights;

    public WeightedDisjunctionTextGenerator(List<? extends IWeightedGenerator> generators) {
        super(generators);
    }

    public static WeightedDisjunctionTextGenerator fromGenerations(Collection<ITextGenerator> generators) {
        return new WeightedDisjunctionTextGenerator(
                generators.stream().map(e -> new GenerationsWeightGenerator(e)).collect(Collectors.toList()));
    }

    /*-********************************************-*/

    @Override
    public ITextGenerator pickGenerator(List<IWeightedGenerator> generators, ITextGeneratorContext parameters) {
        if (hasWeightCalculationInfiniteLoop()) {
            throw new IllegalStateException(
                    "Can't use weighted disjunction ('|') when having makeInfinite loops. Please use '||' for equal disjunction instead.");
        }

        long totalSize = generators.stream()
                .mapToLong(e -> e.getWeight(parameters))
                .sum();

        if (totalSize == 0) {
            throw new IllegalStateException("Can't generate due to not having any elements in "+this+": " + generators);
        }

        long chosenIndex = Picker.nextLong(totalSize);

        long currentIndex = 0;
        for (IWeightedGenerator entry : generators) {
            currentIndex += entry.getWeight(parameters);
            if (chosenIndex < currentIndex) {
                return entry;
            }
        }
        throw new IllegalStateException("Not able to pick a random element from " + this);
    }

    public boolean hasWeightCalculationInfiniteLoop() {
        return getGenerators().stream()
                .filter(IWeightedGenerator::canHaveInfiniteLoop)
                .anyMatch(ITextGenerator::hasInfiniteLoop);
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
    public String toCode() {
        return "{\n" + getGenerators()
                .stream()
                .map(ITextGenerator::toCode)
                .collect(Collectors.joining("\n")) + "\n}";
    }

}
