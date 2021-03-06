package men.brakh.publicKeyCiphers.Elgamal;

import men.brakh.publicKeyCiphers.PublicKeyCiphersMath;

/**
 * Elgamal Public Key class
 * @author Pankratiew Alexandr
 */
public class ElgamalPublicKey {
    private long p;
    private long g;
    private long y;

    public ElgamalPublicKey(long p, long x, long g) {
        this.p = p;
        this.g = g;
        this.y = PublicKeyCiphersMath.power(g,x,p); // y = g^x mod p
    }

    public long getP() {
        return p;
    }

    public long getG() {
        return g;
    }

    public long getY() {
        return y;
    }


}
