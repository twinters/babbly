package be.thomaswinters.textgeneration.domain.generators;

import be.thomaswinters.textgeneration.domain.generators.collection.ConjunctionTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.DisjunctionTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.databases.DeclarationsFileTextGeneratorProxy;
import be.thomaswinters.textgeneration.domain.generators.databases.WordsFileTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.locked.LockedGenerator;
import be.thomaswinters.textgeneration.domain.generators.locked.LockedGeneratorPool;
import be.thomaswinters.textgeneration.domain.generators.named.NamedTextGenerator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TextGeneratorPool {

    private static final Map<String, ITextGenerator> staticPool = new ConcurrentHashMap<>();
    private static final Map<Collection<? extends ITextGenerator>, ITextGenerator> conjunctionPool = new ConcurrentHashMap<>();
    private static final Map<Collection<? extends ITextGenerator>, ITextGenerator> disjunctionPool = new ConcurrentHashMap<>();
    private static final Map<File, ITextGenerator> wordDatabasePool = new ConcurrentHashMap<>();
    private static final Map<File, ITextGenerator> combinationDatabasePool = new ConcurrentHashMap<>();
    private static final Map<String, ITextGenerator> namedPool = new ConcurrentHashMap<>();

    public static ITextGenerator getStatic(String text) {
        if (!staticPool.containsKey(text)) {
            staticPool.put(text, new StaticTextGenerator(text));
        }
        return staticPool.get(text);
    }

    /*-********************************************-*
     *  Conjunctions
     *-********************************************-*/

    public static ITextGenerator getConjunction(Collection<? extends ITextGenerator> generators) {
        if (!conjunctionPool.containsKey(generators)) {
            conjunctionPool.put(generators, new ConjunctionTextGenerator(generators));
        }
        return conjunctionPool.get(generators);
    }

    public static ITextGenerator getConjunction(ITextGenerator... generators) {
        return getConjunction(Arrays.asList(generators));
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Disjunctions
     *-********************************************-*/

    public static ITextGenerator getEqualDisjunction(Collection<? extends ITextGenerator> generators) {
        if (!disjunctionPool.containsKey(generators)) {

            ITextGenerator disj;
            if (generators.size() == 1) {
                disj = generators.iterator().next();
            } else {
                disj = new DisjunctionTextGenerator(generators);
            }
            disjunctionPool.put(generators, disj);
        }
        return disjunctionPool.get(generators);
    }

    public static ITextGenerator getEqualDisjunction(ITextGenerator... generators) {
        return getEqualDisjunction(Arrays.asList(generators));
    }

    public static ITextGenerator getEqualDisjunction(String... generators) {
        return getEqualDisjunction(
                Arrays.asList(generators).stream().map(e -> getStatic(e)).collect(Collectors.toList()));
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Databases
     *-********************************************-*/
    public static ITextGenerator getWordsFile(File file) throws IOException {
        if (!wordDatabasePool.containsKey(file)) {
            wordDatabasePool.put(file, new WordsFileTextGenerator(file));
        }
        return wordDatabasePool.get(file);
    }

    public static ITextGenerator getDeclarationFile(File file) throws IOException {
        if (!combinationDatabasePool.containsKey(file)) {
            combinationDatabasePool.put(file, new DeclarationsFileTextGeneratorProxy(file));
        }
        return combinationDatabasePool.get(file);
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Locked generation
     *-********************************************-*/

    public static LockedGenerator getLockedGeneration(String name, String identifier) {
        return LockedGeneratorPool.get(name, identifier);
    }

    public static ITextGenerator getNamedGeneration(String name) {
        if (!namedPool.containsKey(name)) {
            namedPool.put(name, new NamedTextGenerator(name));
        }
        return namedPool.get(name);
    }

    /*-********************************************-*/
}
