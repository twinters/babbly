package be.thomaswinters.textgeneration.domain.factories.command;

import be.thomaswinters.textgeneration.domain.factories.FunctionFactory;
import be.thomaswinters.textgeneration.domain.functionheader.FunctionHeader;
import be.thomaswinters.textgeneration.domain.generators.commands.ITextGeneratorCommand;

public abstract class CommandFactory extends FunctionFactory<ITextGeneratorCommand, ITextGeneratorCommand> {

    public CommandFactory(String name, FunctionHeader header) {
        super(name, header);
    }

}
