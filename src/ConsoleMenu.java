import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private Scanner scanner = new Scanner(System.in);
    private boolean running;
    private MenuState state = MenuState.MAIN;
    private Fat32 fat;
    private BootSector bootSector;
    private int currentCluster;

    public ConsoleMenu(Fat32 fat) {
        this.running = true;
        this.fat = fat;
        this.bootSector = fat.getBootSector();
        this.currentCluster = bootSector.getRootCluster();
    }

    public void showMenu() throws IOException {
        while (running) {
            switch (state) {
                case MAIN -> mainMenu();
                case FILES -> filesMenu(currentCluster);
                case DISK_INFO -> diskInfoMenu();
            }
        }
    }

    public void mainMenu() {
        System.out.println("---------------");
        System.out.println("1. Files");
        System.out.println("2. Disk");
        System.out.println("3. Exit");
        System.out.println("---------------");

        switch (scanner.nextLine()) {
            case "1" -> {
                currentCluster = bootSector.getRootCluster();
                state = MenuState.FILES;
            }
            case "2" -> state = MenuState.DISK_INFO;
            case "3" -> running = false;
            default -> System.out.println("Invalid option");
        }
    }

    public void filesMenu(int cluster) throws IOException {

        System.out.println("-DIRECTORY-");

        List<DirectoryEntry> files =
                fat.openDirectory(cluster);

        for (int i = 0; i < files.size(); i++) {

            DirectoryEntry f = files.get(i);

            String type = f.isDirectory() ? "[DIR]" : "[FILE]";

            System.out.println(
                    (i + 1) + ". " + type + " " + f.getName()
            );
        }

        System.out.println(files.size() + 1 + ". Back");
        System.out.println("---------------");

        int selected = Integer.parseInt(scanner.nextLine());

        if (selected > 0 && selected <= files.size()) {

            DirectoryEntry file = files.get(selected - 1);

            if (file.isDirectory()) {
                currentCluster = file.getFirstCluster();

                System.out.println("Opened: " + file.getName());
                state = MenuState.FILES;

            } else {

                String content = fat.openFile(file);

                System.out.println("---------------");
                System.out.println(content);
                System.out.println("---------------");
            }
        } else if (selected == files.size() + 1) state = MenuState.MAIN;

    }

    public void diskInfoMenu() {
        System.out.println("---DISK INFO---");
        System.out.println("Bytes per sector: " + bootSector.getBytesPerSector());
        System.out.println("Sectors per cluster: " + bootSector.getSectorsPerCluster());
        System.out.println("Reserved sectors: " + bootSector.getReservedSectors());
        System.out.println("Number of FATs: " + bootSector.getNumberOfFATs());
        System.out.println("Fat size: " + bootSector.getFatSize());
        System.out.println("Root cluster: " + bootSector.getRootCluster());
        System.out.println("1. Back");
        System.out.println("---------------");

        switch (scanner.nextLine()) {
            case "1" -> state = MenuState.MAIN;
            default -> System.out.println("Invalid option");
        }
    }
}
