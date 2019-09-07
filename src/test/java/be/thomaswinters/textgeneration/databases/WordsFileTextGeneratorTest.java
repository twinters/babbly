package be.thomaswinters.textgeneration.databases;

import be.thomaswinters.TestHelpers;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.StaticTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.ConjunctionTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.DisjunctionTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.databases.WordsFileTextGenerator;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class WordsFileTextGeneratorTest {

    public static final long SEED = 123456789123456789l;
    private File testFile;

    @BeforeEach
    public void setup() throws IOException {
        DisjunctionTextGenerator.setSeed(SEED);
        testFile = new File("tmp.wdb");
        testFile.createNewFile();
        testFile.deleteOnExit();
        Files.write("nuclear\napple\nwarfare", testFile, Charsets.UTF_8);
    }

    @Test
    public void Generate_Statics_Generated() throws IOException {
        ITextGenerator generator = new WordsFileTextGenerator(testFile);

        DisjunctionTextGenerator.setSeed(SEED);
        Set<String> generatedStrings = new HashSet<String>();
        for (int i = 0; i < 10; i++) {
            generatedStrings.add(generator.generate());
        }

        TestHelpers.assertIn("nuclear", generatedStrings);
        TestHelpers.assertIn("apple", generatedStrings);
        TestHelpers.assertIn("warfare", generatedStrings);
    }

    @Test
    public void Generate_Conjunction_Generated() throws IOException {
        ITextGenerator generator = new ConjunctionTextGenerator(
                new StaticTextGenerator("Hello, "),
                new WordsFileTextGenerator(testFile),
                new StaticTextGenerator("!"));

        DisjunctionTextGenerator.setSeed(SEED);
        Set<String> generatedStrings = new HashSet<String>();
        for (int i = 0; i < 10; i++) {
            generatedStrings.add(generator.generate());
        }

        TestHelpers.assertIn("Hello, nuclear!", generatedStrings);
        TestHelpers.assertIn("Hello, apple!", generatedStrings);
        TestHelpers.assertIn("Hello, warfare!", generatedStrings);
    }

    @Test
    public void ToCode_Statics_Calculated() throws IOException {
        ITextGenerator generator = new WordsFileTextGenerator(testFile);
        assertEquals("\\words(tmp.wdb)", generator.toCode());
    }
}
