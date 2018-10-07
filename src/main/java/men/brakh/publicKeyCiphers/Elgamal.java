package men.brakh.publicKeyCiphers;

import java.util.HashMap;

public class Elgamal {
    private long p;
    private long x; // 1 < x < p - 1
    private long g; // Первообразный корень
    private long y; // y = g^x mod p.

    private long k; // Сессионный ключ


    public Elgamal(long p, long x, long k) {
        if(!OpenKeyCiphersMath.isPrime(p)) {
            throw new ArithmeticException("Incorrect number P (It should be simple)");
        }
        if((x >= p - 1) || (x <= 1)) {
            throw new ArithmeticException("Incorrect number X. (Need: 1 < x < p - 1)");
        }
        if(OpenKeyCiphersMath.gcd(p-1, k) != 1) {
            throw new ArithmeticException("Incorrect number X. (Need: gcd(p-1, k) == 1)");
        }


        this.p = p;
        this.x = x;
        this.k = k;

        g = OpenKeyCiphersMath.getPrimitive(p);
        y = OpenKeyCiphersMath.power(g,x,p);
    }

    public HashMap<Character, Long> getPublicKey() {
        HashMap<Character, Long> publicKey = new HashMap<Character, Long>();
        publicKey.put('p', p);
        publicKey.put('g', g);
        publicKey.put('y', y);
        return publicKey;
    }

    public long getPrivateKey() {
        return x;
    }

    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }

    public int[] bytesToInts(byte buf[]) {
        int intArr[] = new int[buf.length / 4];
        int offset = 0;
        for(int i = 0; i < intArr.length; i++) {
            intArr[i] = (buf[3 + offset] & 0xFF) | ((buf[2 + offset] & 0xFF) << 8) |
                    ((buf[1 + offset] & 0xFF) << 16) | ((buf[0 + offset] & 0xFF) << 24);
            offset += 4;
        }
        return intArr;
    }

    public int[] encrypt(byte[] plaintext) {
        int[] ciphertext = new int[2*plaintext.length];
        for(int i = 0; i < ciphertext.length; i += 2) {
            if(plaintext[i/2] >= p) {
                throw new ArithmeticException();
            }
            ciphertext[i] = (int) OpenKeyCiphersMath.power(g,k,p); // a
            ciphertext[i + 1] = (int) ((OpenKeyCiphersMath.power(y,k,p) * unsignedToBytes(plaintext[i/2])) % p); // b
        }

        return ciphertext;
    }


    public byte[] decrypt(int[] ciphertext) {
        byte[] plaintext = new byte[ciphertext.length/2];
        for(int i = 0; i < ciphertext.length ; i += 2) {
            int a = ciphertext[i];
            int b = ciphertext[i + 1];
            plaintext[i/2] = (byte) ( b * OpenKeyCiphersMath.power(OpenKeyCiphersMath.power(a,x, p),
                                OpenKeyCiphersMath.phi((int) p)-1, p) % p);
        }
        return plaintext;
    }

    public static void main(String[] args) {
        Elgamal elgamal = new Elgamal(1061, 15, 17);
        int[] lol = elgamal.encrypt("Мем :))".getBytes());
        System.out.println("MSG:" + new String(elgamal.decrypt(lol)));
    }


}
