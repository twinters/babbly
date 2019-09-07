package be.thomaswinters.textgeneration.domain.generators;

import be.thomaswinters.textgeneration.domain.parsers.TextGeneratorParser;

import java.io.IOException;
import java.util.Optional;

public class TextGeneratorTextProxy extends TextGeneratorProxy {

    private final String unparsed;
    private final TextGeneratorParser parser;

    public TextGeneratorTextProxy(String unparsed, TextGeneratorParser parser) {
        this.unparsed = unparsed;
        this.parser = parser;
    }

    /*-********************************************-*
     *  Text generator obligations
     *-********************************************-*/

    @Override
    protected Optional<ITextGenerator> loadGenerator() {
        try {
            return Optional.of(parser.parseGenerator(unparsed));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toCode() {
        return unparsed;
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Object overridden
     *-********************************************-*/
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((unparsed == null) ? 0 : unparsed.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TextGeneratorTextProxy other = (TextGeneratorTextProxy) obj;
        if (unparsed == null) {
            if (other.unparsed != null)
                return false;
        } else if (!unparsed.equals(other.unparsed))
            return false;
        return true;
    }

    /*-********************************************-*/

}
