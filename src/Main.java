import consolemenu.ConsoleMenu;

public class Main {

    public static void main(String[] args) {
        try {
            Disk disk = new Disk("fat32.img");

            ConsoleMenu menu = new ConsoleMenu();
            menu.showMenu();
        } catch (Exception e) {
            System.out.println("ERROR:");
            e.printStackTrace();
        }
    }
}