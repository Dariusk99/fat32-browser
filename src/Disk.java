import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

// Virtual disk class
public class Disk implements AutoCloseable {

    private RandomAccessFile file;

    public Disk(String filePath) throws FileNotFoundException {
        // Load virtual disk file with read only mode
        file = new RandomAccessFile(filePath, "r");
    }

    // Read disk bytes
    public byte[] readBytes(long offset, int size) throws IOException {
        byte[] data = new byte[size];
        file.seek(offset);
        file.readFully(data);
        return data;
    }

    // Close file with AutoCloseable interface
    @Override
    public void close() throws IOException {
        file.close();
    }
}
