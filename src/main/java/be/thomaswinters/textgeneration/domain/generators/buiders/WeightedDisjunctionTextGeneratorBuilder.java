package be.thomaswinters.textgeneration.domain.generators.buiders;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.WeightedDisjunctionTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.weighted.GenerationsWeightGenerator;
import be.thomaswinters.textgeneration.domain.generators.weighted.IWeightedGenerator;
import be.thomaswinters.textgeneration.domain.generators.weighted.WeightedGenerator;

import java.util.ArrayList;
import java.util.List;

public class WeightedDisjunctionTextGeneratorBuilder {

    private final List<IWeightedGenerator> generators = new ArrayList<>();

    public void addGenerator(ITextGenerator generator, int weight) {
        generators.add(new WeightedGenerator(generator, weight));
    }

    public void addGenerationWeightGenerator(ITextGenerator generator) {
        generators.add(new GenerationsWeightGenerator(generator));
    }

    public ITextGenerator build() {
        return new WeightedDisjunctionTextGenerator(generators);
    }

}
