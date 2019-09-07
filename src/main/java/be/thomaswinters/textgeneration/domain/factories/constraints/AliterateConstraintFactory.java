package be.thomaswinters.textgeneration.domain.factories.constraints;

import be.thomaswinters.textgeneration.domain.constraints.AliterateConstraint;
import be.thomaswinters.textgeneration.domain.constraints.LockConstraint;
import be.thomaswinters.textgeneration.domain.functionheader.FunctionHeader;
import be.thomaswinters.textgeneration.domain.functionheader.arguments.Argument;
import be.thomaswinters.textgeneration.domain.functionheader.arguments.ArgumentTypes;
import be.thomaswinters.textgeneration.domain.generators.locked.LockedGenerator;
import be.thomaswinters.textgeneration.domain.util.ConstraintUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

public class AliterateConstraintFactory extends ConstraintFactory {

    private static final String NAME = "aliterate";
    private static final FunctionHeader HEADER = new FunctionHeader(
            Arrays.asList(
                    new Argument(ArgumentTypes.INTEGER_TYPE),
                    new Argument(ArgumentTypes.LOCKED_GENERATOR_TYPE),
                    new Argument(ArgumentTypes.LOCKED_GENERATOR_TYPE, false, true)));

    public AliterateConstraintFactory() {
        super(NAME, HEADER);
    }

    @Override
    protected Collection<LockConstraint> createFunction(List<Object> arguments) {

        int numberOfCharacters = (Integer) arguments.get(0);

        BiFunction<LockedGenerator, LockedGenerator, LockConstraint> combiner = (
                a, b) -> new AliterateConstraint(a, b, numberOfCharacters);

        return ConstraintUtil.combine(
                ConstraintUtil.convertList(arguments, 1, arguments.size(),
                        LockedGenerator.class), combiner);
    }
}
