package be.thomaswinters.textgeneration.domain.generators;

import be.thomaswinters.random.Picker;
import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;

public class RepeaterTextGenerator extends TextGeneratorDecorator {

    private static final int PRIORITY = 150;

    private final int min;
    private final int max;

    public RepeaterTextGenerator(ITextGenerator generator, int min, int max) {
        super(generator);
        this.min = min;
        this.max = max;
    }

    @Override
    public String generate(ITextGeneratorContext parameters) {
        StringBuilder b = new StringBuilder();
        int amountOfTimes = Picker.betweenInclusive(min, max);
        for (int i = 0; i < amountOfTimes; i++) {
            b.append(getGenerator().generate(parameters));
        }
        return b.toString();
    }

    @Override
    public String toCode() {
        return "(" + getGenerator().toCode() + ")" + "{" + min + "," + max + "}";
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public long getAmountOfPossibleGenerations(ITextGeneratorContext parameters) {
        return (max - min + 1) * getGenerator().getAmountOfPossibleGenerations(parameters);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + max;
        result = prime * result + min;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RepeaterTextGenerator other = (RepeaterTextGenerator) obj;
        if (!getGenerator().equals(other.getGenerator()))
            return false;
        if (max != other.max)
            return false;
        return min == other.min;
    }

}
