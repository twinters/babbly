package be.thomaswinters.textgeneration.domain.generators.databases;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.TextGeneratorProxy;

import java.io.File;

public abstract class AFileTextGenerationProxy extends TextGeneratorProxy
        implements ITextGenerator {

    static final int PRIORITY = 90;

    private final File databaseFile;

    protected AFileTextGenerationProxy(File databaseFile) {
        this.databaseFile = databaseFile;
    }

    @Override
    public String toCode() {
        return "\\" + getCodeName().toString().replace("\\", "/") + "("
                + getDatabaseFile() + ")";
    }

    protected File getDatabaseFile() {
        return databaseFile;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    protected abstract String getCodeName();

    /*-********************************************-*
     *  Object overridden
     *-********************************************-*/

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((databaseFile == null) ? 0 : databaseFile.hashCode());
        result = prime * result + super.hashCode();
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
        AFileTextGenerationProxy other = (AFileTextGenerationProxy) obj;
        if (databaseFile == null) {
            if (other.databaseFile != null)
                return false;
        } else if (!databaseFile.equals(other.databaseFile))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return toCode();
    }

    /*-********************************************-*/
}
