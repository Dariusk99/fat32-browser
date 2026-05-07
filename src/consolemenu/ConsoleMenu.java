package consolemenu;

import java.util.Scanner;

public class ConsoleMenu {

    private Scanner scanner = new Scanner(System.in);
    private boolean running;
    private MenuState state = MenuState.MAIN;

    public ConsoleMenu() {
        this.running = true;
    }

    public void showMenu() {
        while (running) {
            switch (state) {
                case MAIN -> mainMenu();
                case FILES -> filesMenu();
                case DISK_INFO -> diskInfoMenu();
            }
        }
    }

    public void mainMenu() {
        System.out.println("---------------");
        System.out.println("FAT32-Browser");
        System.out.println("1. Files");
        System.out.println("2. Disk info");
        System.out.println("3. Exit");
        System.out.println("---------------");

        switch (scanner.nextLine()) {
            case "1" -> state = MenuState.FILES;
            case "2" -> state = MenuState.DISK_INFO;
            case "3" -> running = false;
            default -> System.out.println("Invalid option");
        }
    }

    public void filesMenu() {
        System.out.println("---------------");
        System.out.println("1. List files");
        System.out.println("2. Back");
        System.out.println("---------------");

        switch (scanner.nextLine()) {
            case "1" -> System.out.println("Files:");
            case "2" -> state = MenuState.MAIN;
            default -> System.out.println("Invalid option");
        }
    }

    public void diskInfoMenu() {
        System.out.println("---------------");
        System.out.println("1. Disk info");
        System.out.println("2. Back");
        System.out.println("---------------");

        switch (scanner.nextLine()) {
            case "1" -> System.out.println("Disk info:");
            case "2" -> state = MenuState.MAIN;
            default -> System.out.println("Invalid option");
        }
    }
}
