package be.thomaswinters.textgeneration.generators;

import be.thomaswinters.TestHelpers;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.DisjunctionTextGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static be.thomaswinters.textgeneration.domain.generators.TextGeneratorPool.getEqualDisjunction;
import static be.thomaswinters.textgeneration.domain.generators.TextGeneratorPool.getStatic;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DisjunctionTextGeneratorTest {

    public static final long SEED = 123456789123456789l;

    @BeforeEach
    public void setup() {
        DisjunctionTextGenerator.setSeed(SEED);
    }

    @Test
    public void Generate_Statics_Generated() {
        ITextGenerator generator = getEqualDisjunction(getStatic("Test1"),
                getStatic("Test2"), getStatic("Test3"));

        DisjunctionTextGenerator.setSeed(SEED);
        Set<String> generatedStrings = new HashSet<String>();
        for (int i = 0; i < 10; i++) {
            generatedStrings.add(generator.generate());
        }

        TestHelpers.assertIn("Test1", generatedStrings);
        TestHelpers.assertIn("Test2", generatedStrings);
        TestHelpers.assertIn("Test3", generatedStrings);
    }

    @Test
    public void ToCode_Statics_Calculated() {
        ITextGenerator generator = getEqualDisjunction(getStatic("Nuclear"),
                getStatic("Apple"), getStatic("Time"));
        assertEquals("Nuclear||Apple||Time", generator.toCode());
    }

    @Test
    public void GetAllGenerations_Simple_Calculated() {
        ITextGenerator generator = getEqualDisjunction(getStatic("abc"),
                getStatic("def"), getStatic("ghi"));
        TestHelpers.assertEqualContent(Arrays.asList("abc", "def", "ghi"), generator
                .getAllGenerations().collect(Collectors.toSet()));
    }

    @Test
    public void GetAllGenerations_NestedDisjunction_Calculated() {
        ITextGenerator generator = getEqualDisjunction(getEqualDisjunction(
                getStatic("abc"), getStatic("def"),
                getEqualDisjunction(getStatic("ghi"), getStatic("jkl")),
                getStatic("mno")));
        TestHelpers.assertEqualContent(Arrays.asList("abc", "def", "ghi", "jkl", "mno"), generator
                .getAllGenerations().collect(Collectors.toSet()));
    }
}
