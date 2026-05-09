// Class for fat32 directory entry (file or directory info)
public class DirectoryEntry {

    private final String name;
    private final int firstCluster;
    private final int size;
    private final boolean directory;

    public DirectoryEntry(String name, int firstCluster, int size, boolean directory) {
        this.name = name;
        this.firstCluster = firstCluster;
        this.size = size;
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public int getFirstCluster() {
        return firstCluster;
    }

    public int getSize() {
        return size;
    }

    public boolean isDirectory() {
        return directory;
    }
}
