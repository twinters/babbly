package be.thomaswinters.textgeneration.domain.generators.collection;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class ACollectionTextGenerator<E extends ITextGenerator> implements ITextGenerator {

    private final ImmutableList<E> generators;

    public ACollectionTextGenerator(Collection<? extends E> generators) {
        if (generators.contains(null)) {
            throw new NullPointerException("Given collection contains a null value");
        }
        if (generators.size() == 0) {
            throw new IllegalArgumentException("Collection of generators with size 0.");
        }
        this.generators = ImmutableList.copyOf(generators);
    }

    public ImmutableList<E> getGenerators() {
        return generators;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((generators == null) ? 0 : generators.hashCode());
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
        @SuppressWarnings("unchecked")
        ACollectionTextGenerator<E> other = (ACollectionTextGenerator<E>) obj;
        if (generators == null) {
            if (other.generators != null)
                return false;
        } else {
            // if (!generators.equals(other.generators))
            // return false;
            if (generators.size() != other.generators.size()) {
                return false;
            }
            for (int i = 0; i < generators.size(); i++) {
                E own = generators.get(i);
                E otherGen = other.generators.get(i);
                if (!own.equals(otherGen)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return toCode();
    }

    @Override
    public String toCode() {
        return getGenerators().stream()
                .map(e -> e.getPriority() <= getPriority() ? ("(" + e.toCode() + ")") : e.toCode())
                .collect(Collectors.joining(getDivisionCharacter().toString()));
    }

    public abstract String getDivisionCharacter();

    @Override
    public Collection<ITextGenerator> getChildren() {
        return ImmutableSet.copyOf(generators);
    }
}
