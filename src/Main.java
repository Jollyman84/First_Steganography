import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Editor canvas = null;
        Loader loader = new Loader();

        try {
            canvas = new Editor(loader.openDialog());
        } catch (NullPointerException npe) {
            System.out.println("No image selected.");
            System.exit(23);
        }

        System.out.print("Input message: ");
        String message = sc.nextLine();
        if(message.length() > canvas.getBound())
            message = message.substring(0, canvas.getBound());

        // Encodes message into image and produces random key
        short key = canvas.inject(message);
        // Extract message from image if key is used
        System.out.println(canvas.extract(key));

        // Displays unaltered image
        canvas.display(false);
        // Displays image with message encoded into it
        canvas.display(true);

        // Saves image with message encoded into it
        try {
            canvas.save(loader.saveDialog());
        } catch (Exception ex) {
            System.out.println("No file selected.");
            System.exit(24);
        }
    }
}
