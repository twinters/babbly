package be.thomaswinters.textgeneration.domain.generators.collection;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.declarationrequirement.ConjunctionRequirement;
import be.thomaswinters.textgeneration.domain.declarationrequirement.IDeclarationRequirement;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConjunctionTextGenerator extends ACollectionTextGenerator<ITextGenerator> {

    private static final int PRIORITY = 70;
    public static final String DIVISION_CHARACTER = "";

    /*-********************************************-*
     *  Constructor & factory method
     *-********************************************-*/
    public ConjunctionTextGenerator(Collection<? extends ITextGenerator> generators) {
        super(generators);
    }

    public ConjunctionTextGenerator(ITextGenerator... generators) {
        this(Arrays.asList(generators));
    }

    /*-********************************************-*/

    @Override
    public String generate(ITextGeneratorContext parameters) {
        return getGenerators().stream()
                .map(e -> e.generate(parameters))
                .collect(Collectors.joining());
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public String getDivisionCharacter() {
        return DIVISION_CHARACTER;
    }

    @Override
    public long getAmountOfPossibleGenerations(ITextGeneratorContext parameters) {
        return getGenerators().stream().mapToLong(e -> e.getAmountOfPossibleGenerations(parameters)).reduce(1,
                (left, right) -> left * right);
    }

    @Override
    public Stream<String> getAllGenerations(ITextGeneratorContext parameters) {
        return getGenerators().stream().map(e -> e.getAllGenerations(parameters)).reduce(Stream.of(""),
                ConjunctionTextGenerator::getAllCombinations);
    }

    private static Stream<String> getAllCombinations(Stream<String> stream1, Stream<String> stream2) {
        List<String> stream2List = stream2.collect(Collectors.toList());
        return stream1.flatMap(e1 -> stream2List.stream().map(e2 -> e1 + e2));
    }

    @Override
    public IDeclarationRequirement getRequirements() {
        return new ConjunctionRequirement(
                getChildren().stream().map(ITextGenerator::getRequirements).collect(Collectors.toSet()));
    }
}
