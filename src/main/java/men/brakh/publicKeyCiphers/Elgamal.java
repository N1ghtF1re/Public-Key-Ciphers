package men.brakh.publicKeyCiphers;

import java.util.HashMap;

public class Elgamal {

    private ElgamalOpenKey openKey; // p - простое число, g - первообр. корень, y = g^x mod p.
    private long privateKey; // Закрытый ключ 1 < x < p - 1

    private long k; // Сессионный ключ


    public Elgamal(long p, long x, long k) {
        if(!OpenKeyCiphersMath.isPrime(p)) {
            throw new ArithmeticException("Incorrect number P (It should be prime)");
        }
        if((x >= p - 1) || (x <= 1)) {
            throw new ArithmeticException("Incorrect number X. (Need: 1 < x < p - 1)");
        }
        if((k >= p - 1) || (k <= 1)) {
            throw new ArithmeticException("Incorrect number K. (Need: 1 < k < p - 1)");
        }
        if(OpenKeyCiphersMath.gcd(p-1, k) != 1) {
            throw new ArithmeticException("Incorrect number K. (Need: gcd(p-1, k) == 1)");
        }


        this.privateKey = x; // Закрытый ключ

        this.k = k; // Сессионный ключ

        long g = OpenKeyCiphersMath.getPrimitive(p);
        long y = OpenKeyCiphersMath.power(g,x,p);

        openKey = new ElgamalOpenKey(p,g,y);
    }

    public ElgamalOpenKey getPublicKey() {
        return openKey;
    }

    public long getPrivateKey() {
        return privateKey;
    }

    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }

    public static int[] byte2int(byte[]src) {
        int dstLength = src.length >>> 2;
        int[]dst = new int[dstLength];

        for (int i=0; i<dstLength; i++) {
            int j = i << 2;
            int x = 0;
            x += (src[j++] & 0xff) << 0;
            x += (src[j++] & 0xff) << 8;
            x += (src[j++] & 0xff) << 16;
            x += (src[j++] & 0xff) << 24;
            dst[i] = x;
        }
        return dst;
    }


    public static byte[] int2byte(int[]src) {
        int srcLength = src.length;
        byte[]dst = new byte[srcLength << 2];

        for (int i=0; i<srcLength; i++) {
            int x = src[i];
            int j = i << 2;
            dst[j++] = (byte) ((x >>> 0) & 0xff);
            dst[j++] = (byte) ((x >>> 8) & 0xff);
            dst[j++] = (byte) ((x >>> 16) & 0xff);
            dst[j++] = (byte) ((x >>> 24) & 0xff);
        }
        return dst;
    }

    public int[] encrypt(byte[] plaintext) {
        int[] ciphertext = new int[2*plaintext.length];

        long p = openKey.getP();
        long g = openKey.getG();
        long y = openKey.getY();

        for(int i = 0; i < ciphertext.length; i += 2) {
            if(plaintext[i/2] >= p) {
                throw new ArithmeticException(String.format("Encountered byte value of the source text " +
                        "m[%d] = %d, greater than the number p = %d", i/2, plaintext[i/2], p)
                );
            }
            ciphertext[i] = (int) OpenKeyCiphersMath.power(g,k,p); // a
            ciphertext[i + 1] = (int) ((OpenKeyCiphersMath.power(y,k,p) * unsignedToBytes(plaintext[i/2])) % p); // b
        }

        return ciphertext;
    }


    public byte[] decrypt(int[] ciphertext) {
        byte[] plaintext = new byte[ciphertext.length/2];

        long p = openKey.getP();

        for(int i = 0; i < ciphertext.length ; i += 2) {
            int a = ciphertext[i];
            int b = ciphertext[i + 1];
            plaintext[i/2] = (byte) ( b * OpenKeyCiphersMath.power(OpenKeyCiphersMath.power(a,privateKey, p),
                                OpenKeyCiphersMath.phi((int) p)-1, p) % p);
        }
        return plaintext;
    }

}
