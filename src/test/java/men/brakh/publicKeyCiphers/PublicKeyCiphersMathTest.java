package men.brakh.publicKeyCiphers;

import static org.junit.Assert.*;

public class PublicKeyCiphersMathTest {

    private long badPow(int x, int y) {
        long res = 1;
        for(int i = 0; i < y; i++) {
            res *= x;
        }
        return res;
    }

    @org.junit.Test
    public void powerTest() {
        final int end = 30;
        final int endPow = 13;
        for(int i = 1; i < end; i++) {
            for(int j = 1; j < endPow; j++) {
                for(int k = 3; k < end; k++) {
                    long needResult = (badPow(i, j)) % k; // i^j mod k
                    long result = PublicKeyCiphersMath.power(i,j,k);
                    if(needResult != result) {
                        System.out.println(i);
                        System.out.println(j);
                        System.out.println(k);
                    }
                    assertEquals(needResult, result);
                }
            }
        }
    }
}