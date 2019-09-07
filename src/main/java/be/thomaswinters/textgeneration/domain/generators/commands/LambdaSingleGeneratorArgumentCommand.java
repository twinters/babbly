package be.thomaswinters.textgeneration.domain.generators.commands;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

import java.util.function.Function;

public class LambdaSingleGeneratorArgumentCommand extends SingleGeneratorArgumentCommand {
    private final Function<String, String> mapper;
    private final String name;

    public LambdaSingleGeneratorArgumentCommand(ITextGenerator generator, Function<String, String> mapper, String name) {
        super(generator);
        this.mapper = mapper;
        this.name = name;
    }

    @Override
    public String apply(String generatedString, ITextGeneratorContext parameters) {
        return mapper.apply(generatedString);
    }

    @Override
    public String getName() {
        return name;
    }
}
