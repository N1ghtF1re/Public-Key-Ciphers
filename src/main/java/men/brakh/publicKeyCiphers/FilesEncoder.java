package men.brakh.publicKeyCiphers;
import men.brakh.publicKeyCiphers.Elgamal.Elgamal;

import java.io.*;

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
            FileInputStream fis = new FileInputStream(new File(filePath));
            content = fis.readAllBytes();
            fis.close();
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

    private String getCipherString(int[] arr, int bytesCount) {
        bytesCount *= 2;
        if(bytesCount > arr.length) bytesCount = arr.length;
        final String elementFormat = "   %d %d    ";

        StringBuilder result = new StringBuilder();

        for(int i = 0; i < bytesCount; i +=2) {
            result.append(String.format(elementFormat, arr[i], arr[i+1]));
        }
        return result.toString();
    }

    private String getPlainString(byte[] arr, int bytesCount) {
        if(bytesCount > arr.length) bytesCount = arr.length;
        final String elementFormat = "   %d    ";

        StringBuilder result = new StringBuilder();

        for(int i = 0; i < bytesCount; i++) {
            result.append(String.format(elementFormat, Elgamal.unsignedToBytes(arr[i])));
        }
        return result.toString();
    }

    public String[] encode(String filePath) throws IOException {
        byte[] plainText = readFile(filePath);
        int[] cipherText = cipher.encrypt(plainText);
        writeFile(getOutEncodePath(filePath), Elgamal.int2byte(cipherText));

        String plainStr = getPlainString(plainText, bytesCount);
        String cipherStr = getCipherString(cipherText, bytesCount);

        return new String[] {plainStr, cipherStr};
    }

    public String[] decode(String filePath) throws IOException {
        int[] cipherText = Elgamal.byte2int(readFile(filePath));
        byte[] plainText = cipher.decrypt(cipherText);
        writeFile(getOutDecodePath(filePath), plainText);

        String plainStr = getPlainString(plainText, bytesCount);
        String cipherStr = getCipherString(cipherText, bytesCount);

        return new String[] {plainStr, cipherStr};
    }
}