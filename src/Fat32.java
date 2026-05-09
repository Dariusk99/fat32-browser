import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Fat32 {

    private final Disk disk;

    private BootSector bootSector;

    public Fat32(Disk disk) throws IOException {
        this.disk = disk;
        this.bootSector = new BootSector(disk);
    }

    // Open file using cluster chain
    public String openFile(DirectoryEntry file) throws IOException {
        // Start cluster
        int cluster = file.getFirstCluster();

        int size = file.getSize();

        // Buffer to collect data from multiple clusters
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Loop until reach last cluster of file
        while (cluster < 0x0FFFFFF8) {

            // Read raw data
            byte[] data = getCluster(cluster);

            // Add cluster data to final file buffer
            output.write(data);

            // Get next cluster
            cluster = getNextCluster(cluster);
        }

        // Convert all data to byte array
        byte[] collectedBytes = output.toByteArray();

        // Convert bytes to string and trim to filesize
        return new String(collectedBytes, 0, size);
    }

    // Read FAT to find next cluster of file
    public int getNextCluster(int cluster) throws IOException {

        // Offset of table - FAT starts after reserved sectors
        long fatOffset = bootSector.getReservedSectors() * bootSector.getBytesPerSector();

        // FAT entry is 4 bytes
        long offset = fatOffset + cluster * 4;

        // Read FAT entry
        byte[] data = disk.readBytes(offset, 4);

        //Converting 4 bytes to 32-bit integer
        return (data[0] & 0xFF)
                | ((data[1] & 0xFF) << 8)
                | ((data[2] & 0xFF) << 16)
                | ((data[3] & 0xFF) << 24) & 0x0FFFFFFF;
    }

    //Open directory from cluster. Each directory contains info about files and sudbirectiories
    public List<DirectoryEntry> openDirectory(int cluster) throws IOException {
        byte[] data = getCluster(cluster);
        List<DirectoryEntry> files = new ArrayList<>();

        //Directory entry size is 32bytes
        for (int i = 0; i < data.length; i += 32) {

            // If end of directory then end
            if (data[i] == 0x00) break;
            //0xE5 = deleted flag, if deleted then skip file
            if (data[i] == (byte) 0xE5) continue;

            //File attributes like file type, directory info etc
            int attr = data[i + 11] & 0xFF;

            //Skip long names
            if (attr == 0x0F) continue;

            //Simple DOS style filename
            String name = new String(data, i, 11, StandardCharsets.US_ASCII).trim();

            //Fat32 store first cluster number splited into two parts high and low (20-21) and (26-27) (legacy design from fat16)
            int high = ((data[i + 20] & 0xFF) | ((data[i + 21] & 0xFF) << 8));
            int low  = ((data[i + 26] & 0xFF) | ((data[i + 27] & 0xFF) << 8));

            //Combine high and low parts into 32 bit cluster number
            int firstCluster = (high << 16) | low;

            //File size in bytes
            int size =
                    (data[i + 28] & 0xFF)
                            | ((data[i + 29] & 0xFF) << 8)
                            | ((data[i + 30] & 0xFF) << 16)
                            | ((data[i + 31] & 0xFF) << 24);

            //Check is file a directory
            boolean dir = (attr & 0x10) != 0;

            files.add(new DirectoryEntry(name, firstCluster, size, dir));
        }
        return files;
    }

    public byte[] getCluster(int cluster) throws IOException {
        int size = bootSector.getBytesPerSector() * bootSector.getSectorsPerCluster();
        return disk.readBytes(readOffsetByCluster(cluster), size);
    }

    private long getFirstRootSector() {
        return bootSector.getReservedSectors() + (bootSector.getNumberOfFATs() * bootSector.getFatSize());
    }

    private long readOffsetByCluster(int cluster) {
        long firstRootSector = getFirstRootSector();
        return (firstRootSector + (cluster - 2) * bootSector.getSectorsPerCluster()) * bootSector.getBytesPerSector();
    }

    public BootSector getBootSector() {
        return bootSector;
    }
}