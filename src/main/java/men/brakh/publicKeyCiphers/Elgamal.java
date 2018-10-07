package men.brakh.publicKeyCiphers;

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

    public long[] getPrivateKey() {
        return new long[] {p,g,y};
    }

    public long getPublicKey() {
        return x;
    }

}
