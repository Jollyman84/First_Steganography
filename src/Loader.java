import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class Loader {
    private JFileChooser jChooser;
    private JFrame jFrame;
    private String path;

    public Loader() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }

        jFrame = new JFrame();
        jChooser = new JFileChooser(System.getProperty("user.home") + "/Pictures/");
        jChooser.setFileFilter(new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png"));
        jChooser.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/2,
                Toolkit.getDefaultToolkit().getScreenSize().height/2));
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public String openDialog() {
        jChooser.setDialogTitle("Load Image");
        jChooser.showOpenDialog(jFrame);
        return path = jChooser.getSelectedFile().getAbsolutePath();
    }

    public String saveDialog() {
        jChooser.setDialogTitle("Save Image");
        jChooser.setSelectedFile(new File(""));
        jChooser.showSaveDialog(jFrame);
        String saveFile = jChooser.getSelectedFile().getAbsolutePath();
        return saveFile.split("\\.")[0] + ".png";
    }

    public String getPath() {
        return path;
    }
}
