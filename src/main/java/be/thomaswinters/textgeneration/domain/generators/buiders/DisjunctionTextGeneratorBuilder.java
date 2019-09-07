package be.thomaswinters.textgeneration.domain.generators.buiders;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.DisjunctionTextGenerator;

import java.util.ArrayList;
import java.util.List;

public class DisjunctionTextGeneratorBuilder {

    private final List<ITextGenerator> generators = new ArrayList<>();

    public void addGenerator(ITextGenerator generator) {
        generators.add(generator);
    }

    public DisjunctionTextGenerator build() {
        return new DisjunctionTextGenerator(generators);
    }

}
