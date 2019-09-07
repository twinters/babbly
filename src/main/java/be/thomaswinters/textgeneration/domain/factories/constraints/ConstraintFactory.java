package be.thomaswinters.textgeneration.domain.factories.constraints;

import be.thomaswinters.textgeneration.domain.constraints.LockConstraint;
import be.thomaswinters.textgeneration.domain.factories.FunctionFactory;
import be.thomaswinters.textgeneration.domain.functionheader.FunctionHeader;

import java.util.Collection;

public abstract class ConstraintFactory extends FunctionFactory<LockConstraint, Collection<LockConstraint>> {

    public ConstraintFactory(String name, FunctionHeader header) {
        super(name, header);
    }
}
