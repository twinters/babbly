package be.thomaswinters.textgeneration.domain.generators.weighted;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.TextGeneratorDecorator;

public class GenerationsWeightGenerator extends TextGeneratorDecorator implements IWeightedGenerator {

    public GenerationsWeightGenerator(ITextGenerator generator) {
        super(generator);
    }

    /**
     * This method should return the amount of possible generations it can generate,
     * or a number it pretends it can generate. This is needed by disjunctions that
     * want to give everything equal chances of being generated.
     *
     * @return
     */
    @Override
    public long getWeight(ITextGeneratorContext parameters) {
        long result = getGenerator().getAmountOfPossibleGenerations(parameters);
        return result;
    }

    @Override
    public String toCode() {
        return "x: " + getGenerator().toCode();
    }

    @Override
    public boolean canHaveInfiniteLoop() {
        return true;
    }

}
