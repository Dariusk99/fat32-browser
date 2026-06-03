public class Main {

    public static void main(String[] args) {
        try {
            Disk disk = new Disk("../fat32.img");
            Fat32 fat = new Fat32(disk);

            ConsoleMenu consoleMenu = new ConsoleMenu(fat);
            consoleMenu.showMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}