package be.thomaswinters.textgeneration.generators;

import be.thomaswinters.TestHelpers;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.RepeaterTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.DisjunctionTextGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static be.thomaswinters.textgeneration.domain.generators.TextGeneratorPool.getEqualDisjunction;
import static be.thomaswinters.textgeneration.domain.generators.TextGeneratorPool.getStatic;

public class RepeaterTextGeneratorTest {

    public static final long SEED = 123456789123456789l;

    @BeforeEach
    public void setup() {
        DisjunctionTextGenerator.setSeed(SEED);
    }

    @Test
    public void Generate_Statics_Generated() {
        ITextGenerator generator = new RepeaterTextGenerator(getEqualDisjunction(getStatic("a"), getStatic("b")), 1, 3);

        DisjunctionTextGenerator.setSeed(SEED);
        Set<String> generatedStrings = new HashSet<String>();
        for (int i = 0; i < 100; i++) {
            generatedStrings.add(generator.generate());
        }

        TestHelpers.assertIn("a", generatedStrings);
        TestHelpers.assertIn("b", generatedStrings);
        TestHelpers.assertIn("aa", generatedStrings);
        TestHelpers.assertIn("ab", generatedStrings);
        TestHelpers.assertIn("ba", generatedStrings);
        TestHelpers.assertIn("bb", generatedStrings);
        TestHelpers.assertIn("aaa", generatedStrings);
        TestHelpers.assertIn("aab", generatedStrings);
        TestHelpers.assertIn("aba", generatedStrings);
        TestHelpers.assertIn("abb", generatedStrings);
        ;
        TestHelpers.assertIn("baa", generatedStrings);
        TestHelpers.assertIn("bab", generatedStrings);
        TestHelpers.assertIn("bba", generatedStrings);
        TestHelpers.assertIn("bbb", generatedStrings);
        ;
    }

}
