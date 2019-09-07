package be.thomaswinters.textgeneration.generators;

import be.thomaswinters.TestHelpers;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static be.thomaswinters.textgeneration.domain.generators.TextGeneratorPool.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConjunctionTextGeneratorTest {

    @Test
    public void Generate_Statics_Generated() {
        ITextGenerator generator = getConjunction(getStatic("Hello"),
                getStatic(" "), getStatic("World"));
        assertEquals("Hello World", generator.generate());
    }

    @Test
    public void ToCode_Statics_Calculated() {
        ITextGenerator generator = getConjunction(getStatic("Hello"),
                getStatic(" "), getStatic("World"));
        assertEquals("Hello World", generator.toCode());
    }

    @Test
    public void GetAllGenerations_Simple_Calculated() {
        ITextGenerator generator = getConjunction(getStatic("Hello"),
                getStatic(" "), getStatic("World"));
        TestHelpers.assertEqualContentSingleElement("Hello World", generator.getAllGenerations()
                .collect(Collectors.toSet()));
    }

    @Test
    public void GetAllGenerations_SimpleNestedConjunction_Calculated() {
        ITextGenerator generator = getConjunction(getStatic("Hello"),
                getConjunction(getStatic(" "), getStatic("World")));
        TestHelpers.assertEqualContentSingleElement("Hello World", generator.getAllGenerations()
                .collect(Collectors.toSet()));
    }

    @Test
    public void GetAllGenerations_SimpleDisjunction_Calculated() {
        ITextGenerator generator = getConjunction(getEqualDisjunction("s", "r"));
        TestHelpers.assertEqualContent(Arrays.asList("s", "r"), generator
                .getAllGenerations().collect(Collectors.toSet()));
    }

    @Test
    public void GetAllGenerations_SimpleDisjunctionWithStaticEnd_Calculated() {
        ITextGenerator generator = getConjunction(getEqualDisjunction("s", "r"),
                getStatic("a"));
        TestHelpers.assertEqualContent(Arrays.asList("sa", "ra"), generator
                .getAllGenerations().collect(Collectors.toSet()));
    }

    @Test
    public void GetAllGenerations_SimpleDisjunctionWithStaticBegin_Calculated() {
        ITextGenerator generator = getConjunction(getStatic("a"),
                getEqualDisjunction("s", "r"));
        TestHelpers.assertEqualContent(Arrays.asList("as", "ar"), generator
                .getAllGenerations().collect(Collectors.toSet()));
    }

    @Test
    public void GetAllGenerations_Disjunctions_Calculated() {
        ITextGenerator generator = getConjunction(
                getEqualDisjunction("s", "r", "t"), getEqualDisjunction("a", "e", "o"),
                getEqualDisjunction("l", "t"));
        TestHelpers.assertEqualContent(Arrays.asList("sal", "sat", "sel", "set", "sol",
                "sot", "ral", "rat", "rel", "ret", "rol", "rot", "tal", "tat",
                "tel", "tet", "tol", "tot"), generator.getAllGenerations()
                .collect(Collectors.toSet()));
    }
}
