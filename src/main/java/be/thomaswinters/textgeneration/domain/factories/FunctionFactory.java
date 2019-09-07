package be.thomaswinters.textgeneration.domain.factories;

import be.thomaswinters.textgeneration.domain.functionheader.FunctionHeader;

import java.util.List;

public abstract class FunctionFactory<F, R> {

    protected final String name;
    protected final FunctionHeader header;

    public FunctionFactory(String name, FunctionHeader header) {
        this.name = name;
        this.header = header;
    }

    public FunctionHeader getHeader() {
        return header;
    }

    public String getName() {
        return name;
    }

    public R create(List<Object> arguments) {
        if (!getHeader().canBeAppliedBy(arguments)) {
            throw new IllegalArgumentException(getHeader()
                    + " can not be applied by " + arguments);
        }
        return createFunction(arguments);

    }

    protected abstract R createFunction(List<Object> arguments);

}
