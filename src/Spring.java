import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Spring {
    private static Editor canvas;
    private static Loader loader = new Loader();
    private static JButton button1, button2, button3, button4, button5;
    private static JLabel label1, label2;
    private static JTextArea text1;
    private static JFormattedTextField text2;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Steganography Machine");
        frame.setMinimumSize(new Dimension(680, 325));

        Container pane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        pane.setLayout(layout);

        // Component assignments
        label1 = new JLabel("Image Path", SwingConstants.LEFT);
        label2 = new JLabel("Key:", SwingConstants.LEFT);
        text1 = new JTextArea("Message", 8, 48);
        text2 = new JFormattedTextField((short) 1);
        button1 = new JButton("Search");
        button2 = new JButton("Encrypt");
        button3 = new JButton("Decrypt");
        button4 = new JButton("View");
        button5 = new JButton("Save");

        // Button1 and Label1 functionality
        button1.addActionListener((ActionEvent actionEvent) -> {
            try {
                canvas = new Editor(loader.openDialog());
                label1.setText(loader.getPath());
            } catch (NullPointerException npe) {
                System.out.println("No image selected.");
            }
        });

        // text1 functionality
        CompoundBorder border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        text1.setBorder(border);
        text1.setLineWrap(true);
        text1.setWrapStyleWord(true);

        // text2 functionality
        text2.setColumns(6);

        // Button2 functionality
        button2.addActionListener((ActionEvent actionEvent) -> {
            String message = text1.getText();
            try {
                if(message.length() > canvas.getBound())
                    message = message.substring(0, canvas.getBound());
                short key = canvas.inject(message);
                text2.setValue(key);
            } catch(NullPointerException npe) {
                System.out.println("No image selected.");
            }
        });


        // Button3 functionality
        button3.addActionListener((ActionEvent actionEvent) -> {
            Short key = (Short) text2.getValue();
            if(key < 1)
                System.out.println("Key must be positive integer.");
            else {
                try {
                    text1.setText(canvas.extract(key));
                } catch(Exception e) {
                    System.out.println("No image selected.");
                }
            }
        });

        // Button4 functionality
        button4.addActionListener((ActionEvent actionEvent) -> {
            try {
                canvas.display(true);
            } catch(Exception e) {
                System.out.println("No image selected.");
            }
        });

        // Button5 functionality
        button5.addActionListener((ActionEvent actionEvent) -> {
            if(canvas == null)
                System.out.println("No image available to save.");
            else {
                try {
                    canvas.save(loader.saveDialog());
                } catch (Exception e) {
                    System.out.println("No file selected.");
                }
            }
        });

        // Placing components in content pane
        pane.add(button1);
        layout.putConstraint(SpringLayout.WEST, button1, 15, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, button1, 35, SpringLayout.NORTH, pane);

        pane.add(label1);
        layout.putConstraint(SpringLayout.WEST, label1, 10, SpringLayout.EAST, button1);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, label1, 35, SpringLayout.NORTH, pane);

        pane.add(text1);
        layout.putConstraint(SpringLayout.WEST, text1, 15, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, text1, 15, SpringLayout.SOUTH, button1);

        pane.add(label2);
        layout.putConstraint(SpringLayout.WEST, label2, 15, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, label2, 20, SpringLayout.SOUTH, text1);

        pane.add(text2);
        layout.putConstraint(SpringLayout.WEST, text2, 10, SpringLayout.EAST, label2);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, text2, 0, SpringLayout.VERTICAL_CENTER, label2);

        pane.add(button5);
        layout.putConstraint(SpringLayout.EAST, button5, 0, SpringLayout.EAST, text1);
        layout.putConstraint(SpringLayout.NORTH, button5, 15, SpringLayout.SOUTH, text1);

        pane.add(button4);
        layout.putConstraint(SpringLayout.EAST, button4, -10, SpringLayout.WEST, button5);
        layout.putConstraint(SpringLayout.NORTH, button4, 15, SpringLayout.SOUTH, text1);

        pane.add(button3);
        layout.putConstraint(SpringLayout.EAST, button3, -10, SpringLayout.WEST, button4);
        layout.putConstraint(SpringLayout.NORTH, button3, 15, SpringLayout.SOUTH, text1);

        pane.add(button2);
        layout.putConstraint(SpringLayout.EAST, button2, -10, SpringLayout.WEST, button3);
        layout.putConstraint(SpringLayout.NORTH, button2, 15, SpringLayout.SOUTH, text1);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
