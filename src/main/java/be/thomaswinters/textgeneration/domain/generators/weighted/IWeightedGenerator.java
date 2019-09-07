package be.thomaswinters.textgeneration.domain.generators.weighted;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

public interface IWeightedGenerator extends ITextGenerator {
    long getWeight(ITextGeneratorContext parameters);

    boolean canHaveInfiniteLoop();
}
