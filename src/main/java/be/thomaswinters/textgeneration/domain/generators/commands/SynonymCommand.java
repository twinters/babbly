package be.thomaswinters.textgeneration.domain.generators.commands;

import be.thomaswinters.random.Picker;
import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class SynonymCommand extends SingleGeneratorArgumentCommand {

    private static final IDictionary dictionary;

    static {
        File file = new File("res/dictionary");

        // construct the dictionary object and open it
        dictionary = new Dictionary(file);
        try {
            dictionary.open();
        } catch (IOException e) {
            throw new IllegalStateException("Missing dictionary files");
        }
    }

    public SynonymCommand(ITextGenerator generator) {
        super(generator);
    }

    @Override
    public String apply(String string, ITextGeneratorContext parameters) {
        // look up first sense of the word "dog "
        IWord word = lookForWord(string);

        if (word == null) {
            return string;
        }

        List<IWord> synonyms = new ArrayList<>(word.getSynset().getWords());

        if (synonyms.size() > 1) {
            synonyms.remove(word);

            String synonym = Picker.pick(synonyms).getLemma().replace("_", " ");

            return synonym;
        }
        return string;
    }

    private IWord lookForWord(String generated) {
        IIndexWord idxWord = dictionary.getIndexWord(generated, POS.NOUN);
        if (idxWord == null) {
            idxWord = dictionary.getIndexWord(generated, POS.ADJECTIVE);
        }
        if (idxWord == null) {
            idxWord = dictionary.getIndexWord(generated, POS.VERB);
        }
        if (idxWord == null) {
            idxWord = dictionary.getIndexWord(generated, POS.ADVERB);
        }

        if (idxWord == null) {
            return null;
        }

        IWordID wordID = idxWord.getWordIDs().get(0);

        IWord word = dictionary.getWord(wordID);

        return word;
    }

    @Override
    public String getName() {
        return "synonym";
    }

}
