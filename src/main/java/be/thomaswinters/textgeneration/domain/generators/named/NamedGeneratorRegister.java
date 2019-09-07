package be.thomaswinters.textgeneration.domain.generators.named;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.StaticTextGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NamedGeneratorRegister implements INamedGeneratorRegister {
    private final Map<String, NamedGeneratorDeclaration> generators = new HashMap<>();

    public NamedGeneratorDeclaration createGenerator(String name, ITextGenerator generator) {
        if (generators.containsKey(name)) {
            if (!generators.get(name).getGenerator().equals(generator)) {
                throw new IllegalStateException("Generator with name " + name + " already exists.");
            }
            return generators.get(name);
        }
        NamedGeneratorDeclaration result = new NamedGeneratorDeclaration(name, generator);
        generators.put(name, result);
        return result;
    }

    public NamedGeneratorDeclaration createGenerator(String name, String staticGenerator) {
        return createGenerator(name, new StaticTextGenerator(staticGenerator));
    }

    public Collection<NamedGeneratorDeclaration> getAllDeclarations() {
        return generators.values();
    }

    @Override
    public Collection<String> getAllNames() {
        return generators.keySet();
    }

    @Override
    public ITextGenerator getGenerator(String name) {
        if (!generators.containsKey(name)) {
            throw new IllegalStateException("No declaration has the name '" + name + "'");
        }
        return generators.get(name).getGenerator();
    }

    @Override
    public boolean hasGenerator(String name) {
        return generators.containsKey(name);
    }

    @Override
    public String toString() {
        return generators.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue().getGenerator().toCode())
                .collect(Collectors.joining("\n"));
    }
}
