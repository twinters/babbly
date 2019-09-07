package be.thomaswinters.textgeneration.parsers;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.RepeaterTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.databases.DeclarationFileTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.databases.WordsFileTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.named.NamedGeneratorRegister;
import be.thomaswinters.textgeneration.domain.parsers.DeclarationsFileParser;
import be.thomaswinters.textgeneration.domain.parsers.TextGeneratorParser;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static be.thomaswinters.textgeneration.domain.generators.TextGeneratorPool.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TextGeneratorParserTest {

    private static File tmpWordDatabaseFile;
    private static final ITextGenerator STATIC_1 = getStatic("abc");
    private static final ITextGenerator STATIC_2 = getStatic("def");
    private static final ITextGenerator STATIC_3 = getStatic("ghi");
    private static final ITextGenerator STATIC_4 = getStatic("jkl");
    private static final ITextGenerator STATIC_5 = getStatic("mno");
    private static final ITextGenerator STATIC_6 = getStatic("pqr");
    private static final ITextGenerator STATIC_7 = getStatic("stu");
    // private static final ITextGenerator STATIC_8 = getStatic("vwx");

    //	private NamedTextGeneratorDeclaration namedDecl1;
    private NamedGeneratorRegister namedRegister;

    private ITextGenerator named1;
    private ITextGenerator locked1;
    private ITextGenerator locked2;

    /*-********************************************-*
     *  Setup
     *-********************************************-*/

    private static final TextGeneratorParser textParser = new TextGeneratorParser();

    @BeforeAll
    public static void setup() throws IOException {
        int index = 0;
        do {
            String name = "tmp" + index + ".words";
            tmpWordDatabaseFile = new File(name);
            index++;
        } while (tmpWordDatabaseFile.exists());

        tmpWordDatabaseFile.createNewFile();
        tmpWordDatabaseFile.deleteOnExit();
    }

    @BeforeEach
    public void setupTest() {
        namedRegister = new NamedGeneratorRegister();
        String declName = "NAMED";
//		namedDecl1 = namedRegister.createGenerator(declName,
//				getEqualDisjunction(getStatic("123"), getStatic("456"), getStatic("789")));
        named1 = getNamedGeneration(declName);
        locked1 = getLockedGeneration(declName, "1");
        locked2 = getLockedGeneration(declName, "2");
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  STATIC
     *-********************************************-*/

    @Test
    public void StaticText_Normal_Parsed() throws IOException {
        ITextGenerator desiredResult = getStatic("Hello ");
        assertEqualWhenParsed(desiredResult);
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Conjunction
     *-********************************************-*/

    @Test
    public void Conjunction_SimpleTwoWords_Parsed() throws IOException {
        ITextGenerator desiredResult = getStatic("Hello world");
        assertEqualWhenParsed(desiredResult);
    }

    @Test
    public void Conjunction_ThreeWords_Parsed() throws IOException {
        ITextGenerator desiredResult = STATIC_1;
        assertEqualWhenParsed(desiredResult);
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Disjunction
     *-********************************************-*/

    @Test
    public void Disjunction_ThreeWords_Parsed() throws IOException {
        ITextGenerator desiredResult = getEqualDisjunction(STATIC_1, STATIC_2, STATIC_3);
        assertEqualWhenParsed(desiredResult);
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  DisjunctionConjunction
     *-********************************************-*/

    @Test
    public void DisjunctionConjunction_StaticsFirst_Parsed() throws IOException {

        // Nuclear|Apple|(Hello +World+!)
        ITextGenerator desiredResult = getEqualDisjunction(STATIC_1, STATIC_2, STATIC_3);
        assertEqualWhenParsed(desiredResult);
    }

    @Test
    public void DisjunctionConjunction_StaticsLast_Parsed() throws IOException {

        ITextGenerator desiredResult = getEqualDisjunction(STATIC_1, STATIC_4, STATIC_5);
        assertEqualWhenParsed(desiredResult);
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  ConjunctionDisjunction
     *-********************************************-*/

    @Test
    public void ConjunctionDisjunction_StaticsFirst_Parsed() throws IOException {

        // Nuclear+Apple+(Hello |World|!)
        ITextGenerator desiredResult = getConjunction(STATIC_1, getEqualDisjunction(STATIC_3, STATIC_4, STATIC_5));
        assertEqualWhenParsed(desiredResult);
    }

    @Test
    public void ConjunctionDisjunction_StaticsLast_Parsed() throws IOException {

        // (Hello |World|!)+Nuclear+Apple
        ITextGenerator desiredResult = getConjunction(getEqualDisjunction(STATIC_1, STATIC_2, STATIC_3), STATIC_4);
        assertEqualWhenParsed(desiredResult);
    }

    @Test
    public void ConjunctionDisjunction_Generic1_Parsed() throws IOException {
        ITextGenerator desiredResult = getConjunction(STATIC_1, getEqualDisjunction(STATIC_1, STATIC_2, STATIC_3),
                STATIC_4);
        assertEqualWhenParsed(desiredResult);
    }

    @Test
    public void ConjunctionDisjunction_DisjunctionInDisjunction_Parsed() throws IOException {
        ITextGenerator desiredResult = getEqualDisjunction(STATIC_1, STATIC_2,
                getEqualDisjunction(STATIC_1, STATIC_2, STATIC_3), STATIC_4, STATIC_4);
        assertEqualWhenParsed(desiredResult);
    }

    @Test
    public void CombinationDataseConjunctionDisjunction_NoNames_Parsed() throws IOException {
        DeclarationFileTextGenerator desiredResult = new DeclarationFileTextGenerator(getConjunction(STATIC_1,
                STATIC_2, getEqualDisjunction(STATIC_3, STATIC_4, STATIC_5), STATIC_6, STATIC_7), namedRegister);
        assertEqualCombinationDatabaseWhenParsed(desiredResult);
    }

    @Test
    public void CombinationDataseConjunctionDisjunction_WithNamed_Parsed() throws IOException {
        DeclarationFileTextGenerator desiredResult = new DeclarationFileTextGenerator(
                getConjunction(STATIC_1, named1, getEqualDisjunction(STATIC_1, named1, STATIC_3), STATIC_4, named1),
                namedRegister);
        assertEqualCombinationDatabaseWhenParsed(desiredResult);
    }

    @Test
    public void CombinationDataseConjunctionDisjunction_WithLocked_Parsed() throws IOException {
        DeclarationFileTextGenerator desiredResult = new DeclarationFileTextGenerator(
                getConjunction(locked1, named1, getEqualDisjunction(STATIC_1, named1, STATIC_3), locked1, locked2),
                namedRegister);
        assertEqualCombinationDatabaseWhenParsed(desiredResult);
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  WordDatabase
     *-********************************************-*/

    @Test
    public void WordDatabase_Normal_Parsed() throws IOException {
        Files.write("Nuclear\nPower\nPlant", tmpWordDatabaseFile, Charsets.UTF_8);
        ITextGenerator desiredResult = getWordsFile(tmpWordDatabaseFile);

        assertEqualWhenParsed(desiredResult);
    }

    @Test
    public void WordDatabase_InConjunction_Parsed() throws IOException {
        Files.write("Nuclear\nPower\nPlant", tmpWordDatabaseFile, Charsets.UTF_8);
        ITextGenerator desiredResult = getConjunction(STATIC_1, getWordsFile(tmpWordDatabaseFile));

        assertEqualWhenParsed(desiredResult);
    }

    @Test
    public void WordDatabase_InDisjunction_Parsed() throws IOException {
        Files.write("Nuclear\nPower\nPlant", tmpWordDatabaseFile, Charsets.UTF_8);
        ITextGenerator desiredResult = getEqualDisjunction(STATIC_1, STATIC_2,
                new WordsFileTextGenerator(tmpWordDatabaseFile));

        assertEqualWhenParsed(desiredResult);
    }

    /*-********************************************-*/

//	@Test
//	public void Repeater_InCombinationDatabase_Parsed() throws IOException {
//		ITextGenerator desiredResult = new RepeaterTextGenerator(
//				new CombinationDatabaseTextGenerator(getEqualDisjunction(STATIC_1, named1, STATIC_4), namedRegister), 1,
//				3);
//
//		assertEqualWhenParsed(desiredResult);
//	}

    @Test
    public void Repeater_WithDisjunction_Parsed() throws IOException {
        ITextGenerator desiredResult = new RepeaterTextGenerator(getEqualDisjunction(STATIC_1, STATIC_3, STATIC_4), 3,
                50);

        assertEqualWhenParsed(desiredResult);
    }

    @Test
    public void Repeater_Parsed() throws IOException {
        Files.write("Nuclear\nPower\nPlant", tmpWordDatabaseFile, Charsets.UTF_8);
        ITextGenerator desiredResult = getConjunction(STATIC_1, getWordsFile(tmpWordDatabaseFile));

        assertEqualWhenParsed(desiredResult);
    }

    @Test
    public void Repeater_Conjunction_Parsed() throws IOException {
        ITextGenerator desiredResult = new RepeaterTextGenerator(STATIC_1, 3, 50);

        assertEqualWhenParsed(desiredResult);
    }

    @Test
    public void Repeater_OutsideConjunction_Parsed() throws IOException {
        ITextGenerator desiredResult = getConjunction(STATIC_3, new RepeaterTextGenerator(STATIC_4, 0, 3));

        assertEqualWhenParsed(desiredResult);
    }

    public static void assertEqualWhenParsed(ITextGenerator generator) {
        try {
            ITextGenerator parsed = textParser.parseGenerator(generator.toCode());
            assertEquals(generator, parsed);
        } catch (IOException e) {
            fail("An IOException occurred: " + e.getMessage());
        }
    }

    public static void assertEqualCombinationDatabaseWhenParsed(DeclarationFileTextGenerator generator) {
        try {
            Files.write(generator.toCode(), tmpWordDatabaseFile, Charsets.UTF_8);

            ITextGenerator parsed = (new DeclarationsFileParser(tmpWordDatabaseFile)).getGenerator();
            assertEquals(generator.toCode(), parsed.toCode());
        } catch (IOException e) {
            fail("An IOException occurred: " + e.getMessage());
        }
    }
}
