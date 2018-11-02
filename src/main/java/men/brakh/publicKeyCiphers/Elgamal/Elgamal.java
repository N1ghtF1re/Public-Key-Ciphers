package men.brakh.publicKeyCiphers.Elgamal;

import men.brakh.publicKeyCiphers.PublicKeyCiphersMath;

import java.util.List;

/**
 * Elgamal Cipher Realisation
 * @author Pankratiew Alexandr
 */
public class Elgamal {

    private ElgamalPublicKey publicKey; // p - prime number, g - primitive root module p, y = g^x mod p.
    private long privateKey; // Private key 1 < x < p - 1

    private long k; // Session key


    /**
     * Constructor of Elgamal cipher
     * @param p Prime number
     * @param x Private key, number of range (1; p - 1)
     * @param k Session key, mutually prime with p number of range (1; p - 1)
     */
    public Elgamal(long p, long x, long k, int g) {
        if(!PublicKeyCiphersMath.isPrime(p)) {
            throw new ArithmeticException("Incorrect number P (It should be prime)");
        }

        if(p > Integer.MAX_VALUE) {
            throw new ArithmeticException("P is too long");
        }
        if((x >= p - 1) || (x <= 1)) {
            throw new ArithmeticException("Incorrect number X. (Need: 1 < x < p - 1)");
        }
        if((k >= p - 1) || (k <= 1)) {
            throw new ArithmeticException("Incorrect number K. (Need: 1 < k < p - 1)");
        }
        if(PublicKeyCiphersMath.gcd(p-1, k) != 1) {
            throw new ArithmeticException("Incorrect number K. (Need: gcd(p-1, k) == 1)");
        }
        if(p <= 255) {
            throw new ArithmeticException("Incorrect number P. (Must not be less than maximum byte value (255))");
        }
        List<Integer> primitiveRoots = PublicKeyCiphersMath.getPrimitiveRoots(p);
        if(!primitiveRoots.contains(g)) {
            throw new ArithmeticException("G is not a primitive root of p");
        }

        this.privateKey = x; // Private key

        this.k = k; // Session key

        publicKey = new ElgamalPublicKey(p, x, g); // Created Public Key. Public key - set(p,g,y). p - prime number, g - primitive root modulo p, y = g^x mod p.
    }

    /**
     * Get the public key
     * @return Public key
     */
    public ElgamalPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Get the private key
     * @return Private key
     */
    public long getPrivateKey() {
        return privateKey;
    }

    /**
     * Translates signed byte to unsigned
     * @param b byte
     * @return unsigned byte
     */
    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }

    /**
     * Converts an array of bytes to an array of ints
     * @param src Array of bytes
     * @return Array of ints
     */
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

    /**
     * Converts an array of ints to an array of byte
     * @param src Array of ints
     * @return Array of bytes
     */
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

    /**
     * Encodes an array of bytes
     * -- One byte of source array turns into two elements of the array of ints.
     * -- CIPHERTEXT[i] = p^k mod g
     * -- CIPHERTEXT[i+1] = (y^k * PlAINTEXT[i/2]) mod p
     *
     * @param plaintext Source array of bytes
     * @return Ciphertext (array of ints)
     */
    public int[] encrypt(byte[] plaintext) {
        int[] ciphertext = new int[2*plaintext.length];

        long p = publicKey.getP();
        long g = publicKey.getG();
        long y = publicKey.getY();

        for(int i = 0; i < ciphertext.length; i += 2) {
            ciphertext[i] = (int) PublicKeyCiphersMath.power(g,k,p); // a
            ciphertext[i + 1] = (int) ((PublicKeyCiphersMath.power(y,k,p) * unsignedToBytes(plaintext[i/2])) % p); // b
        }

        return ciphertext;
    }

    /**
     * Decodes an array of ints
     * -- Two elements of ciphertext array (int[]) turns into one byte of plaintext array
     * -- (b = ciphertext[i + 1], a =  ciphertext[i])
     * -- PLAINTEXT[i/2] =  ( b * (a^x)^-1 ) mod p = (b * (a^x)^(phi(p) - 1)) mod p, phi - Euler function
     * @see PublicKeyCiphersMath#phi(int)
     *
     * @param ciphertext Ciphertext (array of ints)
     * @return Plaintext (array of bytes)
     */
    public byte[] decrypt(int[] ciphertext) {
        byte[] plaintext = new byte[ciphertext.length/2];

        long p = publicKey.getP();

        for(int i = 0; i < ciphertext.length ; i += 2) {
            int a = ciphertext[i];
            int b = ciphertext[i + 1];
            plaintext[i/2] = (byte) ( b * PublicKeyCiphersMath.power(PublicKeyCiphersMath.power(a,privateKey, p),
                                PublicKeyCiphersMath.phi((int) p)-1, p) % p);
        }
        return plaintext;
    }

}
