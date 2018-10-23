package men.brakh.publicKeyCiphersFrame;
import men.brakh.publicKeyCiphers.Elgamal.Elgamal;
import sun.misc.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A class that implements file encryption and decryption
 * @author Pankratiew Alexandr
 */

public class FilesEncoder {
    Elgamal cipher;
    final int bytesCount = 15;

    public FilesEncoder(Elgamal cipher) {
        this.cipher = cipher;
    }

    /**
     * Return Returns an array of bytes of the source file.
     * @param filePath The path to the file
     * @return An array of bytes of the source file.
     * @throws IOException
     */
    private byte[] readFile(String filePath) throws IOException {
        byte[] content;
        try{
            content =  Files.readAllBytes(Paths.get(filePath));
        } catch (Exception e) {
            throw new IOException(e);
        }
        return content;

    }

    /**
     * Write array of bytes (content) to the file
     * @param filePath The path to the file
     * @param content The byte array to write to the file.
     * @throws IOException
     */
    private void writeFile(String filePath, byte[] content) throws IOException {

        try{
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            fos.write(content);
            fos.close();
        }catch (Exception e) {
            throw new IOException(e);
        }
    }


    private String getOutEncodePath(String filePath) {
        return filePath + ".encoded";
    }
    private String getOutDecodePath(String filePath) {
        return filePath + ".decoded";
    }
    private String getOutKeyPath(String filePath) {
        return filePath + "-key.txt";
    }


    public String[] encode(String filePath) throws IOException {
        byte[] plainText = readFile(filePath);
        int[] cipherText = cipher.encrypt(plainText);
        String newPath = getOutEncodePath(filePath);
        writeFile(newPath, Elgamal.int2byte(cipherText));

        BytesConverter bytesConverter = new BytesConverter();
        String cipherStr = bytesConverter.intArray2String(cipherText);

        return new String[] {cipherStr, String.valueOf(cipher.getPublicKey().getY()), newPath};
    }

    public String[] decode(String filePath) throws IOException {
        int[] cipherText = Elgamal.byte2int(readFile(filePath));
        byte[] plainText = cipher.decrypt(cipherText);
        String newPath = getOutDecodePath(filePath);
        writeFile(newPath, plainText);

        BytesConverter bytesConverter = new BytesConverter();
        String plainStr = bytesConverter.byteArray2String(plainText);

        return new String[] {plainStr, String.valueOf(cipher.getPublicKey().getY()), newPath};
    }
}