package fileio;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Random;

/**
 * Reads a file and processes it appropriately
 */
public abstract class FileReader
{
    protected String file;
    protected String heading;

    /**
     * Reads a file line by line and processes it
     * @param file File being processed
     * @throws FileReadException
     * @throws FileFormatException
     */
    public void read(String file) throws FileReadException, FileFormatException
    {
        try (FileInputStream fis = new FileInputStream(file))
        {
            this.file = file;

            InputStreamReader rdr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(rdr);

            String line;

            heading = br.readLine();
            preprocess();
            line = br.readLine();
            while (line != null)
            {
                if(!line.equals(""))
                    processLine(line);

                line = br.readLine();
            }
        }
        catch (IOException e)
        {
            throw new FileReadException("File error: " + e.getMessage(), e);
        }
    }

    /**
     * Actions to be performed prior to reading the contents of the fiole
     * @throws FileFormatException
     */
    protected abstract void preprocess() throws FileFormatException;

    /**
     * Processes a line of text from the file
     * @param line line to be processed
     * @throws FileFormatException
     */
    protected abstract void processLine(String line) throws FileFormatException;
}
