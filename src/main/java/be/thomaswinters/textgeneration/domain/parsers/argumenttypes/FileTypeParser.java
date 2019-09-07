package be.thomaswinters.textgeneration.domain.parsers.argumenttypes;

import be.thomaswinters.textgeneration.domain.functionheader.arguments.ArgumentTypes;

import java.io.File;
import java.util.Optional;

public class FileTypeParser extends ArgumentTypeParser {

    private final Optional<File> relativeLocation;

    public FileTypeParser(Optional<File> relativeLocation) {
        super(ArgumentTypes.FILE_TYPE);
        this.relativeLocation = relativeLocation;
    }

    @Override
    public File parse(String filePath) {
        File file = new File(filePath);
        if (file.isAbsolute() || !relativeLocation.isPresent()) {
            return file;
        } else {

            File actualRelativeLocation = relativeLocation.get();
            while (filePath.startsWith("../")) {
                actualRelativeLocation = actualRelativeLocation.getParentFile();
                filePath = filePath.substring(3);
            }

            File relativeFile = new File(actualRelativeLocation, filePath);
            return relativeFile;
        }
    }

}
