package men.brakh.publicKeyCiphers;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.Random;

public class OpenKeyCiphersMath {

    /*
     * a^z mod m - возведение a в степень z по модулю m
     */
    public static long power(long a, long z, long m)
    {
        long a1 = a;
        long z1 = z;
        long x = 1;
        while (z1 != 0) {
            while(z1 % 2 == 0) {
                z1 /= 2;
                a1 = (a1*a1) % m;
            }
            z1 = z1 - 1;
            x = (x*a1) % m;
        }
        return x;
    }

    public static Boolean isPrime(long x) {
        for(long i=2;i<=Math.sqrt(x);i++)
        if(x%i==0)
            return false;
        return true;
    }

    public static long gcd(long a, long b){
        if(b==0)
            return a;
        return gcd(b, a%b);
    }


    public static int getPrimitive (long p) {
        ArrayList<Long> fact = new ArrayList<Long>();
        long phi = p-1;
        long n = phi;
        for (long i=2; i*i<=n; ++i)
            if (n % i == 0) {
                fact.add(i);
                while (n % i == 0)
                    n /= i;
            }
        if (n > 1)
            fact.add(n);

        for (int res=2; res<=p; ++res) {
            Boolean ok = true;
            for (int i=0; (i<fact.size()) && ok; ++i)
                ok &= power (res, phi / fact.get(i), p) != 1;
            if (ok)  return res;
        }
        return -1;
    }

    public static int phi (int n) {
        int result = n;
        for (int i=2; i*i<=n; ++i)
            if (n % i == 0) {
                while (n % i == 0)
                    n /= i;
                result -= result / i;
            }
        if (n > 1)
            result -= result / n;
        return result;
    }


    public static void main(String[] args) {
        System.out.println(6 * power(power(8,5, 13), phi(13)-1, 13) % 13);//Math.pow(Math.pow(8,5), phi(13)-1) % 13);
    }
}
