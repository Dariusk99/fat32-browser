import java.io.IOException;

public class BootSector {

    private int bytesPerSector;
    private int sectorsPerCluster;
    private int reservedSectors;
    private int numberOfFATs;
    private int fatSize;
    private int rootCluster;

    //Boot sector parser
    public BootSector(Disk disk) throws IOException {
        byte[] data = disk.readBytes(0, 512);
        // In java byte is -128 to 127. FAT need 0 to 255
        // "& 0xFF" converts byte to integer representation 0-255
        this.bytesPerSector = (data[11] & 0xFF) | ((data[12] & 0xFF) << 8);
        this.sectorsPerCluster = data[13] & 0xFF;
        this.reservedSectors = (data[14] & 0xFF) | ((data[15] & 0xFF) << 8);
        this.numberOfFATs = data[16] & 0xFF;
        this.fatSize = (data[36] & 0xFF)
                | ((data[37] & 0xFF) << 8)
                | ((data[38] & 0xFF) << 16)
                | ((data[39] & 0xFF) << 24);

        this.rootCluster = (data[44] & 0xFF)
                | ((data[45] & 0xFF) << 8)
                | ((data[46] & 0xFF) << 16)
                | ((data[47] & 0xFF) << 24);
    }

    public int getBytesPerSector() {
        return bytesPerSector;
    }

    public int getSectorsPerCluster() {
        return sectorsPerCluster;
    }

    public int getReservedSectors() {
        return reservedSectors;
    }

    public int getNumberOfFATs() {
        return numberOfFATs;
    }

    public int getFatSize() {
        return fatSize;
    }

    public int getRootCluster() {
        return rootCluster;
    }
}
