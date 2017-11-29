package fileio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * Writes system contents to a file
 */
public abstract class FileWriter<T>
{
    protected String file;
    private Collection<T> collection;

    public FileWriter(Collection<T> collection)
    {
        this.collection = collection;
    }

    /**
     * Writes the contents of a collection to a file
     * @param file file being written to
     * @throws FileWriteException
     */
    public void write(String file) throws FileWriteException
    {
        try(FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);
           )
        {
            pw.write(heading() + "\n");
            for(T t : collection)
            {
                pw.write(writeLine(t) + "\n");
            }
        }
        catch(IOException e)
        {
            throw new FileWriteException("File error: " + e.getMessage(), e);
        }
    }

    /**
     * Determines the first line of the file
     * @return heading
     */
    protected abstract String heading();

    /**
     * Creates a string describing an object so it can be saved in the file
     * @param t object being saved
     * @return String describing the object
     */
    protected abstract String writeLine(T t);
}
