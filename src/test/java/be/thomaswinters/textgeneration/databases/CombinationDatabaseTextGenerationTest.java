package be.thomaswinters.textgeneration.databases;

import be.thomaswinters.TestHelpers;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.DisjunctionTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.databases.DeclarationsFileTextGeneratorProxy;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CombinationDatabaseTextGenerationTest {

    public static final long SEED = 123456789123456789l;

    /*-********************************************-*
     *  Setup
     *-********************************************-*/

    private File testFile1;
    private File testFile2;

    @BeforeEach
    public void setup() throws IOException {
        DisjunctionTextGenerator.setSeed(SEED);

        testFile1 = new File("tmp1.decl");
        testFile1.createNewFile();
        testFile1.deleteOnExit();

        testFile2 = new File("tmp2.decl");
        testFile2.createNewFile();
        testFile2.deleteOnExit();

    }

    /*-********************************************-*/

    @Test
    public void Disjunction_Parsed_NoException() throws IOException {
        write("main = {\n (a|b)\n\n" + "c|d\n}", testFile1);
        ITextGenerator cdb = new DeclarationsFileTextGeneratorProxy(testFile1);
        Collection<String> generations = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            generations.add(cdb.generate());
        }

        TestHelpers.assertEqualContent(Arrays.asList("a", "b", "c", "d"), generations);
    }

    @Test
    public void Loop_NoException() throws IOException {
        write("main = ha\\decl(" + testFile2.getPath() + ")||ha!", testFile1);
        write("main = hi\\decl(" + testFile1.getPath() + ")||hi!", testFile2);
        ITextGenerator cdb = new DeclarationsFileTextGeneratorProxy(testFile1);
        for (int i = 0; i < 100; i++) {
            String generated = cdb.generate();
            assertTrue(generated.matches("^ha((hi)|hiha)*!$"));
        }
    }

    /*-********************************************-*
     *  Helper methods
     *-********************************************-*/

    private void write(String text, File file) throws IOException {
        Files.write(text, file, Charsets.UTF_8);
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Comments
     *-********************************************-*/

    @Test
    public void Comment_Normal_NoException() throws IOException {
        write("main = {\n apple //this is a comment\n\n ba(ni|na)na \n} // this is also a comment", testFile1);

        ITextGenerator cdb = new DeclarationsFileTextGeneratorProxy(testFile1);
        Collection<String> generations = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            generations.add(cdb.generate());
        }

        TestHelpers.assertEqualContent(Arrays.asList("apple", "banana", "banina"), generations);
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  ToString
     *-********************************************-*/
    @Test
    public void ToString_Normal_Calculated() throws IOException {
        ITextGenerator cdb = new DeclarationsFileTextGeneratorProxy(testFile1);
        assertEquals("\\decl(tmp1.decl)", cdb.toString());
    }

    /*-********************************************-*/

}
