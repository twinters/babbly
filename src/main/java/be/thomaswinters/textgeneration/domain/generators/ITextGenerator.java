package be.thomaswinters.textgeneration.domain.generators;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.context.TextGeneratorContext;
import be.thomaswinters.textgeneration.domain.declarationrequirement.ConjunctionRequirement;
import be.thomaswinters.textgeneration.domain.declarationrequirement.IDeclarationRequirement;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ITextGenerator {

    default String generate() {
        return generate(new TextGeneratorContext());
    }

    String generate(ITextGeneratorContext context);

    String toCode();

    Collection<ITextGenerator> getChildren();

    default Collection<ITextGenerator> getAllDescendents() {
        Collection<ITextGenerator> result = new HashSet<>();
        getAllDescendents(result);
        return result;
    }

    default void getAllDescendents(Collection<ITextGenerator> visited) {
        for (ITextGenerator child : getChildren()) {
            if (!visited.contains(child)) {
                visited.add(child);
                child.getAllDescendents(visited);
            }
        }
    }

    default IDeclarationRequirement getRequirements() {
        return new ConjunctionRequirement(
                getChildren().stream().map(e -> e.getRequirements()).collect(Collectors.toSet()));
    }

    default boolean isAllowedToGenerate(ITextGeneratorContext context) {
        return !context.ignoreGeneratorsUsingMissingDeclarations()
                || getRequirements().canBeFulfilledBy(context.getCompleteDeclarationNames());
    }

    default boolean hasInfiniteLoop() {
        return getChildren().contains(this);
    }

    default Stream<String> getAllGenerations() {
        return getAllGenerations(new TextGeneratorContext());
    }

    Stream<String> getAllGenerations(ITextGeneratorContext parameters);

    long getAmountOfPossibleGenerations(ITextGeneratorContext parameters);

    default boolean containsDescendant(ITextGenerator child) {
        return this.equals(child) || getChildren().stream().anyMatch(e -> e.containsDescendant(this));
    }

    /**
     * Returns the priority when parsing
     */
    default int getPriority() {
        return 0;
    }
}
