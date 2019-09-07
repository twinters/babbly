package be.thomaswinters.textgeneration.domain.parsers.util;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class TextGeneratorBuffer {
    private List<ITextGenerator> generators = new ArrayList<>();
    private Function<Collection<ITextGenerator>, ITextGenerator> wrapper;

    public TextGeneratorBuffer(Function<Collection<ITextGenerator>, ITextGenerator> wrapper) {
        this.wrapper = wrapper;
    }

    public void append(ITextGenerator generator) {
        generators.add(generator);
    }

    public void modifyLast(Function<ITextGenerator, ITextGenerator> mapper) {
        if (generators.isEmpty()) {
            throw new IllegalStateException("Can't modify empty buffer");
        }
        int lastIndex = generators.size() - 1;
        generators.set(lastIndex, mapper.apply(generators.get(lastIndex)));
    }

    public boolean isEmpty() {
        return generators.isEmpty();
    }

    public ITextGenerator poll() {
        ITextGenerator result;
        if (generators.size() == 1) {
            result = generators.get(0);
        } else {
            result = wrapper.apply(generators);
        }
        generators = new ArrayList<>();
        return result;
    }

    @Override
    public String toString() {
        return "GENERATORBUFFER: <" + generators + ">";
    }

}
