package be.thomaswinters.textgeneration.domain.factories.command;

import be.thomaswinters.textgeneration.domain.functionheader.FunctionHeader;
import be.thomaswinters.textgeneration.domain.functionheader.arguments.Argument;
import be.thomaswinters.textgeneration.domain.functionheader.arguments.ArgumentTypes;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.commands.ITextGeneratorCommand;
import com.google.common.base.Function;

import java.util.Arrays;
import java.util.List;

public class SingleTextGeneratorArgumentCommandFactory extends CommandFactory {
    private static final FunctionHeader HEADER = new FunctionHeader(
            Arrays.asList(new Argument(ArgumentTypes.TEXT_GENERATOR_TYPE)));

    private final Function<ITextGenerator, ITextGeneratorCommand> creator;

    public SingleTextGeneratorArgumentCommandFactory(String commandName,
                                                     Function<ITextGenerator, ITextGeneratorCommand> creator) {
        super(commandName, HEADER);
        this.creator = creator;
    }

    public ITextGeneratorCommand create(ITextGenerator generator) {
        return creator.apply(generator);
    }

    @Override
    protected ITextGeneratorCommand createFunction(List<Object> arguments) {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException(
                    "Must have exactly one argument. Can not apply "
                            + arguments);
        }
        if (!(arguments.get(0) instanceof ITextGenerator)) {
            throw new IllegalArgumentException(
                    "Must give a textgenerator as argument");
        }

        return create((ITextGenerator) arguments.get(0));

    }
}
