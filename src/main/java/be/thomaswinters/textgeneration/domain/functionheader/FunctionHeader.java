package be.thomaswinters.textgeneration.domain.functionheader;

import be.thomaswinters.textgeneration.domain.functionheader.arguments.Argument;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Valueclass representing the header of a function/constraint/command. Might
 * contain optional arguments and a repeating argument at the end of the header.
 *
 * @author Thomas Winters
 */
public class FunctionHeader {
    private final ImmutableList<Argument> types;

    public FunctionHeader(List<? extends Argument> types)
            throws IllegalArgumentException {

        if (types == null) {
            throw new NullPointerException();
        }

        if (!canHaveAsTypes(types)) {
            throw new IllegalArgumentException("Can not have " + types
                    + " as a function header.");
        }

        this.types = ImmutableList.copyOf(types);
    }

    public FunctionHeader(Argument... arguments) {
        this(Arrays.asList(arguments));
    }

    public ImmutableList<Argument> getArguments() {
        return types;
    }

    private Argument getLastArgument() {
        return getArguments().get(getArguments().size() - 1);
    }

    public boolean canHaveInfiniteArguments() {
        return getLastArgument().isRepeatable();
    }

    public Argument get(int i) {
        if (i > getArguments().size() && !canHaveInfiniteArguments()) {
            throw new IllegalArgumentException("Index out of bounds: " + i
                    + ". Size: " + getArguments().size());
        }

        if (i < getArguments().size()) {
            return getArguments().get(i);
        }
        return getLastArgument();
    }

    public boolean canBeAppliedBy(List<Object> arguments) {
        if (arguments.size() > getArguments().size()
                && !canHaveInfiniteArguments()) {
            return false;
        }

        for (int i = 0; i < getArguments().size(); i++) {
            if (i >= arguments.size()) {
                return get(i).isOptional();
            } else if (!get(i).isInstance(arguments.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean canHaveAsTypes(List<? extends Argument> types) {
        if (!hasOnlyLastRepeating(types)) {
            return false;
        }
        return hasOnlyOptionalsAfterFirstOptional(types);
    }

    private static boolean hasOnlyOptionalsAfterFirstOptional(
            List<? extends Argument> types) {
        boolean hasDetectedOptional = false;
        for (Argument argumentType : types) {
            if (argumentType.isOptional()) {
                hasDetectedOptional = true;
            } else {
                if (hasDetectedOptional) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean hasOnlyLastRepeating(List<? extends Argument> types) {

        // Size - 1! only the last one can repeat
        for (int i = 0; i < types.size() - 1; i++) {
            if (types.get(i).isRepeatable()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "function("
                + getArguments().stream().map(Object::toString)
                .collect(Collectors.joining(",")) + ")";
    }
}
