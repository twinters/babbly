package be.thomaswinters.textgeneration.locked;

import be.thomaswinters.TestHelpers;
import be.thomaswinters.textgeneration.domain.constraints.AliterateConstraint;
import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.context.TextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.DisjunctionTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.databases.DeclarationFileTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.locked.LockedGenerator;
import be.thomaswinters.textgeneration.domain.generators.named.NamedGeneratorDeclaration;
import be.thomaswinters.textgeneration.domain.generators.named.NamedGeneratorRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static be.thomaswinters.textgeneration.domain.generators.TextGeneratorPool.*;

public class LockedGeneratorTest {

    public static final long SEED = 123456789123456789l;

    @BeforeEach
    public void setup() {
        DisjunctionTextGenerator.setSeed(SEED);
    }

    @Test
    public void LockInConjunction_ThreeTimesSameWord_Generated() {

        String declName = "name1";
        // lock1 = ha|hi|ho
        NamedGeneratorDeclaration decl = new NamedGeneratorDeclaration(declName,
                getEqualDisjunction(getStatic("ha"), getStatic("hi"), getStatic("ho")));

        // [lock1:1]+[lock1:1]+[lock1:1]
        ITextGenerator generator = getConjunction(getLockedGeneration(declName, "1"),
                getLockedGeneration(declName, "1"), getLockedGeneration(declName, "1"));

        // Register
        NamedGeneratorRegister register = new NamedGeneratorRegister();
        register.createGenerator(decl.getName(), decl.getGenerator());
        ITextGeneratorContext context = new TextGeneratorContext(register, false);

        for (int i = 0; i < 500; i++) {
            TestHelpers.assertIn(generator.generate(context), Arrays.asList("hahaha", "hihihi", "hohoho"));
        }

    }

    @Test
    public void AliterateLock_InParameters_Generated() {

        // lock1 = apple|columbus|funny|freckle
        String declName = "lock";
        NamedGeneratorDeclaration decl = new NamedGeneratorDeclaration(declName, getEqualDisjunction(
                getStatic("apple"), getStatic("cat"), getStatic("columbus"), getStatic("funny"), getStatic("freckle")));

        // [lock1:1]+[lock1:2]
        LockedGenerator lock1 = getLockedGeneration(declName, "1");
        LockedGenerator lock2 = getLockedGeneration(declName, "2");
        ITextGenerator generator = getConjunction(lock1, lock2);

        Collection<String> generations = new HashSet<>();

        // Register
        NamedGeneratorRegister register = new NamedGeneratorRegister();
        register.createGenerator(decl.getName(), decl.getGenerator());

        for (int i = 0; i < 500; i++) {

            ITextGeneratorContext parameters = new TextGeneratorContext(
                    Arrays.asList(new AliterateConstraint(lock1, lock2)), register, false);

            generations.add(generator.generate(parameters));
        }
        TestHelpers.assertEqualContent(Arrays.asList("funnyfreckle", "frecklefunny", "catcolumbus", "columbuscat"), generations);

    }

    @Test
    public void AliterateLock_InCombinationDatabase_Generated() {

        // lock1 = apple|columbus|funny|freckle
        NamedGeneratorRegister register = new NamedGeneratorRegister();
        String declName = "lock";
        register.createGenerator(declName, getEqualDisjunction(getStatic("apple"),
                getStatic("cat"), getStatic("columbus"), getStatic("funny"), getStatic("freckle")));

        // [lock1:1]+[lock1:2]
        LockedGenerator lock1 = getLockedGeneration(declName, "1");
        LockedGenerator lock2 = getLockedGeneration(declName, "2");
        ITextGenerator combinationDatabase = new DeclarationFileTextGenerator(getConjunction(lock1, lock2),
                register, Arrays.asList(new AliterateConstraint(lock1, lock2)));

        Collection<String> generations = new HashSet<>();

        for (int i = 0; i < 500; i++) {
            generations.add(combinationDatabase.generate());
        }
        TestHelpers.assertEqualContent(Arrays.asList("funnyfreckle", "frecklefunny", "catcolumbus", "columbuscat"), generations);

    }
}
