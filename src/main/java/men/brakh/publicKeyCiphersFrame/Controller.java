package men.brakh.publicKeyCiphersFrame;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import men.brakh.publicKeyCiphers.Elgamal.Elgamal;
import men.brakh.publicKeyCiphers.PublicKeyCiphersMath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Controller {

    private String plainPath;
    private String cipherPath;

    /* FXML FIELDS */
    @FXML
    private TextField tfX;

    @FXML
    private Button btnEncryp;

    @FXML
    private ComboBox<Integer> cbG;

    @FXML
    private TextArea taCipher;

    @FXML
    private TextArea taPlain;

    @FXML
    private Button btnLoadPlain;

    @FXML
    private TextField tfK;

    @FXML
    private TextField tfY;

    @FXML
    private Button btnLoadCipher;

    @FXML
    private Button btnDecrypt;

    @FXML
    private TextField tfP;



    void errorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText("An error occurred during execution.");

        alert.showAndWait();
    }

    private void updateG(int p_value) {
        if(PublicKeyCiphersMath.isPrime(p_value)) {
            java.util.List<Integer> primitivesRoot = PublicKeyCiphersMath.getPrimitiveRoots(p_value);
            for(int g : primitivesRoot) {
                System.out.println(g);
                cbG.getItems().add(g);
            }

        }
    }


    @FXML
    void tfPKeyReleased(KeyEvent event) {
        try {
            cbG.getItems().clear();
            int p_value = Integer.valueOf(tfP.getText());
            updateG(p_value);
        } catch (NumberFormatException e) {

        }
    }

    @FXML
    void textFieldPressed(KeyEvent event) {
        tfY.setText("");
    }

    @FXML
    void btnEncryptClick(ActionEvent event) {
        try {
            long p = Long.parseLong(tfP.getText());
            long x = Long.parseLong(tfX.getText());
            long k = Long.parseLong(tfK.getText());
            if(cbG.getSelectionModel().isEmpty()) {
                errorAlert("Value of G is not selected");
                return;
            }
            int g = cbG.getValue();
            System.out.println(g);

            // CREATE ELGAMAL CIPHER OBJECT
            Elgamal elgamal = new Elgamal(p,x,k, g);

            FilesEncoder filesEncoder = new FilesEncoder(elgamal);
            String[] res = filesEncoder.encode(plainPath);

            taCipher.setText(res[0]);
            tfY.setText(res[1]);
            cipherPath = res[2];


        } catch (IOException exception1) {
            errorAlert("File not found");
        } catch (ArithmeticException exception2) {
            errorAlert(exception2.getMessage());
        } catch (NumberFormatException exception3) {
            errorAlert("Incorrect data entered:\n" + exception3.getMessage());
        }
    }


    @FXML
    void btnDecryptClick(ActionEvent event) {
        try {
            long p = Long.parseLong(tfP.getText());
            long x = Long.parseLong(tfX.getText());
            long k = Long.parseLong(tfK.getText());
            int g = cbG.getValue();
            System.out.println(g);

            // CREATE ELGAMAL CIPHER OBJECT
            Elgamal elgamal = new Elgamal(p,x,k, g);

            FilesEncoder filesEncoder = new FilesEncoder(elgamal);
            String[] res = filesEncoder.decode(cipherPath);

            taPlain.setText(res[0]);
            tfY.setText(res[1]);
            plainPath = res[2];

        } catch (IOException exception1) {
            errorAlert("File not found");
        } catch (ArithmeticException exception2) {
            errorAlert(exception2.getMessage());
        } catch (NumberFormatException exception3) {
            errorAlert("Incorrect data entered:\n" + exception3.getMessage());
        }
    }

    @FXML
    void btnLoadCipherClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        String path;
        if (selectedFile != null) {
            path = selectedFile.getAbsolutePath();
            try {
                int[] cipher = Elgamal.byte2int(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
                BytesConverter bytesConverter = new BytesConverter();
                taPlain.clear();
                taCipher.clear();
                taCipher.setText(bytesConverter.intArray2String(cipher));
                cipherPath = path;
            } catch (IOException e) {
            }
        }
    }

    @FXML
    void btnLoadPlainClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        String path;
        if (selectedFile != null) {
            path = selectedFile.getAbsolutePath();
            try {
                byte[] plain = Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()));
                BytesConverter bytesConverter = new BytesConverter();
                taPlain.clear();
                taCipher.clear();
                taPlain.setText(bytesConverter.byteArray2String(plain));
                plainPath = path;
            } catch (IOException e) {
            }
        }
    }


    @FXML
    void initialize() {
        tfP.setText("1061");
        updateG(1061);
    }

}
