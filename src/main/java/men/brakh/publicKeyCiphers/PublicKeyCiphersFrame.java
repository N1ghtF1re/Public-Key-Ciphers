package men.brakh.publicKeyCiphers;

import men.brakh.publicKeyCiphers.Elgamal.Elgamal;
import men.brakh.publicKeyCiphers.Elgamal.ElgamalPublicKey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Frame for test Public Key Cuphers
 * @author Pankratiew Alexandr
 */

public class PublicKeyCiphersFrame extends JFrame {
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

    private JLabel lblG = new JLabel("Select prime root modulo  P:");

    private JComboBox gSelect = new JComboBox();


    private JButton btnApply = new JButton("To do everything!\n");

    public PublicKeyCiphersFrame() {
        super("Perfect Elgamal encoder/decoder");
        this.setBounds(100,100,500,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(12,1));
        container.add(lblP);
        container.add(inpP);
        container.add(lblG);
        container.add(gSelect);
        inpP.addKeyListener(new InpPKeyListener());
        updateG(Integer.valueOf(inpP.getText()));
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

    private void updateG(int p_value) {
        if(PublicKeyCiphersMath.isPrime(p_value)) {
            java.util.List<Integer> primitivesRoot = PublicKeyCiphersMath.getPrimitiveRoots(p_value);
            for(int g : primitivesRoot) {
                gSelect.addItem(g);
            }

        }
    }

    private class InpPKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            try {
                gSelect.removeAllItems();
                int p_value = Integer.valueOf(inpP.getText());
                updateG(p_value);
            } catch (NumberFormatException e) {
                dialogMSG("Invalid p", "Error");
            }
            System.out.println(inpP.getText());
        }
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

            final String succesMessage = "%s!\n\nSource bytes:\n%s\n\nResult:\n%s\n\n %s";
            final String publicKeyMsg = "Public key: p = %d, g = %d, y = %d";
            final String privateKeyMsg = "Private key: %d";
            String title;
            String sourceBytes;
            String result;
            String additionalInfo;
            try {
                // PUBLIC KEY:
                long p = Long.parseLong(inpP.getText());
                long x = Long.parseLong(inpX.getText());
                long k = Long.parseLong(inpK.getText());
                int g = (int) gSelect.getItemAt(gSelect.getSelectedIndex());
                System.out.println(g);

                // CREATE ELGAMAL CIPHER OBJECT
                Elgamal elgamal = new Elgamal(p,x,k, g);

                FilesEncoder filesEncoder = new FilesEncoder(elgamal);

                if (radioIsEncrypt.isSelected()) {
                    title = "Encrypted";
                    String[] res = filesEncoder.encode(currentPath);

                    sourceBytes = res[0]; // res[0] - PlainBytes
                    result = res[1]; // res[1] - CipherBytes

                    ElgamalPublicKey key = elgamal.getPublicKey();
                    additionalInfo = String.format(publicKeyMsg, key.getP(), key.getG(), key.getY());
                } else {
                    title = "Decrypted";
                    String[] res = filesEncoder.decode(currentPath);

                    sourceBytes = res[1]; // res[1] - CipherBytes
                    result = res[0]; // res[0] - PlainBytes
                    additionalInfo = String.format(privateKeyMsg, elgamal.getPrivateKey());
                }

                String message = String.format(succesMessage, title, sourceBytes, result, additionalInfo);
                dialogMSG(message, title);
            } catch (IOException exception1) {
                dialogMSG("File not found", "ERROR");
            } catch (ArithmeticException exception2) {
                dialogMSG(exception2.getMessage(), "ERROR");
            } catch (NumberFormatException exception3) {
                dialogMSG("Incorrect data entered:\n" + exception3.getMessage(), "ERROR");
            }
        }
    }

    public static void main(String[] args) {
        PublicKeyCiphersFrame app = new PublicKeyCiphersFrame();
        app.setVisible(true);
    }
}