package be.thomaswinters.textgeneration.domain.generators.named;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface INamedGeneratorRegister {
    ITextGenerator getGenerator(String name);

    boolean hasGenerator(String name);

    Collection<String> getAllNames();

    /**
     * Returns a list of the names of all declarations that are not missing any
     * named declarations inside
     */
    default Collection<String> getCompleteDeclarationNames() {
        Set<String> completeDeclarations = new HashSet<>();
        int previousSize = -1;
        while (completeDeclarations.size() > previousSize) {
            previousSize = completeDeclarations.size();

            Set<String> newNames = getAllNames().stream()
                    // Filter out already complete declarations
                    .filter(e -> !completeDeclarations.contains(e))
                    // Get those that can be fulfilled
                    .filter(e -> hasGenerator(e)
                            && getGenerator(e).getRequirements().canBeFulfilledBy(completeDeclarations))
                    .collect(Collectors.toSet());

            completeDeclarations.addAll(newNames);
        }

        return completeDeclarations;
    }

}
