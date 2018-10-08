package men.brakh.publicKeyCiphers;

public class ElgamalOpenKey {
    private long p;
    private long g;
    private long y;

    public ElgamalOpenKey(long p, long g, long y) {
        this.p = p;
        this.y = y;
        this.g = g;
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
