public class Main {

    public static void main(String[] args) {

        try {
            Disk disk = new Disk("fat32.img");

        } catch (Exception e) {
            System.out.println("ERROR:");
            e.printStackTrace();
        }
    }
}