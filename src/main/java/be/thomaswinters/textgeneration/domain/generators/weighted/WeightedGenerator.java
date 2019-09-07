package be.thomaswinters.textgeneration.domain.generators.weighted;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.TextGeneratorDecorator;

public class WeightedGenerator extends TextGeneratorDecorator implements IWeightedGenerator {

    private final long weight;

    public WeightedGenerator(ITextGenerator generator, long weight) {
        super(generator);
        this.weight = weight;
    }

    @Override
    public long getWeight(ITextGeneratorContext parameters) {
        return weight;
    }

    @Override
    public String toCode() {
        return weight + ": " + getGenerator().toCode();
    }

    @Override
    public boolean canHaveInfiniteLoop() {
        return false;
    }

}
