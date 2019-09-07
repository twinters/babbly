package be.thomaswinters.textgeneration;

import be.thomaswinters.textgeneration.domain.generators.collection.DisjunctionTextGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class TextGeneratorTest {

    public static final long SEED = 123456789123456789l;

    private File testFile1;
    private File testFile2;

    @BeforeEach
    public void setup() throws IOException {
        DisjunctionTextGenerator.setSeed(SEED);

        testFile1 = File.createTempFile("tmp1.cdb", null);
        testFile1.createNewFile();
        testFile1.deleteOnExit();

        testFile2 = File.createTempFile("tmp2.cdb", null);
        testFile2.createNewFile();
        testFile2.deleteOnExit();

    }

    @Test
    public void Generate_LoopCombination_NoException() {

    }
}
