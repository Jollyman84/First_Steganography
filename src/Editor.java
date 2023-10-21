import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

public class Editor {
    private final int height, width;
    private final long bound;
    private BufferedImage picture, graphia;

    public Editor(String path) {
        // Loads image
        try {
            picture = ImageIO.read(new File(path));
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        height = picture.getHeight();
        width = picture.getWidth();
        bound = height * width;

        // Creates second image instance
        ColorModel model = picture.getColorModel();
        WritableRaster raster = picture.copyData(null);
        graphia = new BufferedImage(model, raster, model.isAlphaPremultiplied(), null);
    }

    // Embeds message in picture
    public short inject(String message) {
        Placement place = new Placement();
        int i, j;
        short key = place.keyGenerator(bound);
        long p = 0;

        for(char c: (message+'\0').toCharArray()) {
            // Determines the pixels to be altered
            p = (p + key) % bound;
            i = (int) (p % width);
            j = (int) (p / width);

            // Alters pixel color at (i,j)
            graphia.setRGB(i, j, fade(c, picture.getRGB(i,j)));
            // System.out.println("p = " + p + ", i = " + i + ", j = " + j);
            // System.out.println("p2 = " + picture.getRGB(i,j) + ", p2 = " + graphia.getRGB(i,j));
        }
        return key;
    }

    // Retrieves message using key
    public String extract(short key) {
        int i, j;
        StringBuilder message = new StringBuilder();
        char c;
        long p = 0;

        do {
            p = (p + key) % bound;
            i = (int) (p % width);
            j = (int) (p / width);
            c = fire(graphia.getRGB(i, j));
            message.append(c);
        } while(c != '\0');

        return message.toString().trim();
    }

    // Encodes char into int representing pixel color
    private int fade(char c, int lv) {
        // splits lv into its component rgb values
        int r = lv >> 16 & 0xff, g = (lv>>8) & 0xff, b = lv & 0xff;
        int t = ThreadLocalRandom.current().nextInt(0,16);

        // Random number and offset char value are added to rgb values
        g = (g & 0xf0) | t;
        r = (r & 0xf0) | (((c >> 4) + t) % 16);
        b = (b & 0xf0) | (((c & 0x0f) + t) % 16);

        return (r<<16 | g<<8 | b);
    }

    // Decodes char value within int representing pixel color
    private char fire(int lv) {
        int r = (lv>>16) & 0x0f, g = 16 - ((lv>>8) & 0x0f), b = lv & 0x0f;
        return (char) (((r+g)%16) << 4 | ((b+g)%16));
    }

    // Creates window displaying either the original image or the encoded image
    public void display(boolean which) {
        BufferedImage pic = which ? graphia : picture;
        String name = which ? "Graphia" : "Picture";

        // Scaled dimensions of image
        int high, wide;
        if(height >= width) {
            high = Toolkit.getDefaultToolkit().getScreenSize().height - 100;
            wide = (width * high) / height;
        } else {
            wide = Toolkit.getDefaultToolkit().getScreenSize().height - 100;
            high = (width * wide) / height;
        }

        Image myPic2 = pic.getScaledInstance(wide, high, Image.SCALE_DEFAULT);
        JLabel picLabel = new JLabel(new ImageIcon(myPic2));
        JPanel jPanel = new JPanel();
        JFrame frame = new JFrame();

        jPanel.add(picLabel);
        frame.setTitle(name);
        frame.setSize(new Dimension(wide + 20,high + 40));
        frame.add(jPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public void save(String name) throws IOException{
        File output = new File(name);
        ImageIO.write(graphia, "png", output);
    }

    public int getBound() {
        return bound > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) bound;
    }
}
