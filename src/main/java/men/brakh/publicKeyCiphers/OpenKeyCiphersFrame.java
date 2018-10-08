package men.brakh.publicKeyCiphers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

public class OpenKeyCiphersFrame extends JFrame {
    private String currentPath;

    private JRadioButton radioIsEncrypt = new JRadioButton("Encode");
    private JRadioButton radioIsDecrypt = new JRadioButton("Decode");

    private JButton btnSelectFile = new JButton("Select input file");
    private JLabel lblCurrentFile = new JLabel("input.txt");

    private JLabel lblP = new JLabel("Enter prime number P:");
    private JTextField inpP = new JTextField("1061",40);
    private JLabel lblX = new JLabel("Enter number X (1 < x < p - 1): ");
    private JTextField inpX = new JTextField("15",40);
    private JLabel lblK = new JLabel("Enter number K (1 < k < p - 1, gcd(k, p-1) = 1): ");
    private JTextField inpK = new JTextField("17",40);

    private JButton btnApply = new JButton("To do everything!\n");

    public OpenKeyCiphersFrame() {
        super("Perfect Elgamal encoder/decoder");
        this.setBounds(100,100,500,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(10,1));
        container.add(lblP);
        container.add(inpP);
        container.add(lblX);
        container.add(inpX);
        container.add(lblK);
        container.add(inpK);

        lblCurrentFile.setVerticalAlignment(JLabel.CENTER);
        container.add(btnSelectFile);
        container.add(lblCurrentFile);

        ButtonGroup bgSelectMode = new ButtonGroup();
        Panel pnlSelectMode = new Panel(new GridLayout(1,2));
        bgSelectMode.add(radioIsDecrypt);
        bgSelectMode.add(radioIsEncrypt);
        pnlSelectMode.add(radioIsDecrypt);
        pnlSelectMode.add(radioIsEncrypt);
        container.add(pnlSelectMode);

        radioIsEncrypt.setSelected(true);

        btnSelectFile.addActionListener(new SelectFile());
        btnApply.addActionListener(new ButtonEventListener());
        container.add(btnApply);
    }

    public String trimStr(String str) {
        if (str.length() >= 15*8) {
            return str.substring(0, 15*8) + "...";
        }
        return  str;
    }
    void dialogMSG(String message, String title) {
        JOptionPane.showMessageDialog(null,
                message,
                title,
                JOptionPane.PLAIN_MESSAGE);
    }

    class SelectFile implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileopen = new JFileChooser();
            fileopen.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int ret = fileopen.showDialog(null, "OpenFile");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                currentPath = file.getAbsolutePath();
                lblCurrentFile.setText(file.getName());
            }
        }
    }
    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            long p = Long.parseLong(inpP.getText());
            long x = Long.parseLong(inpX.getText());
            long k = Long.parseLong(inpK.getText());
            try {
                Elgamal elgamal = new Elgamal(p,x,k);
                FilesEncoder filesEncoder = new FilesEncoder(elgamal);
                if (radioIsEncrypt.isSelected()) {
                    filesEncoder.encode(currentPath);
                } else {
                    filesEncoder.decode(currentPath);
                }
            } catch (Exception e2) {
                System.out.println(e2.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        OpenKeyCiphersFrame app = new OpenKeyCiphersFrame();
        app.setVisible(true);
    }
}