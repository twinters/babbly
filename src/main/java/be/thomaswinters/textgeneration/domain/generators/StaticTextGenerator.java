package be.thomaswinters.textgeneration.domain.generators;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Always returns the same string
 *
 * @author Thomas Winters
 */
public class StaticTextGenerator implements ITextGenerator {
    private static final int PRIORITY = 100;
    public final String toReturn;

    public StaticTextGenerator(String toReturn) {
        if (toReturn == null) {
            throw new NullPointerException();
        }
        this.toReturn = toReturn;
    }

    @Override
    public String generate(ITextGeneratorContext parameters) {
        return toReturn;
    }

    @Override
    public String toCode() {
        return toReturn; // TODO zie dat speciale characters niet verloren gaan
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((toReturn == null) ? 0 : toReturn.hashCode());
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
        StaticTextGenerator other = (StaticTextGenerator) obj;
        if (toReturn == null) {
            if (other.toReturn != null)
                return false;
        } else if (!toReturn.equals(other.toReturn))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return toCode();
    }

    @Override
    public Collection<ITextGenerator> getChildren() {
        return ImmutableSet.of();
    }


    @Override
    public long getAmountOfPossibleGenerations(ITextGeneratorContext parameters) {
        return 1;
    }

    @Override
    public Stream<String> getAllGenerations(ITextGeneratorContext parameters) {
        return Arrays.asList(toReturn).stream();
    }
}
