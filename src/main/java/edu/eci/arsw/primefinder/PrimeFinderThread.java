package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrimeFinderThread extends Thread{

    private int a, b;
    private List<Integer> primes;
    private boolean isLocked = false;
    private Object locker = new Object();

    public PrimeFinderThread(int a, int b) {
        super();
        this.primes = new LinkedList<>();
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        for (int i = a; i < b; i++) {
            if (!isLocked) {
                if (isPrime(i)) primes.add(i);
            } else {
                synchronized(locker){
                    try {
                        locker.wait();
                    } catch (InterruptedException e) {
                        Logger.getLogger(PrimeFinderThread.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
                i--;
            }
        }
    }

    boolean isPrime(int n) {
        boolean ans;
        if (n > 2) {
            ans = n % 2 != 0;
            for (int i = 3; ans && i * i <= n; i += 2) {
                ans = n % i != 0;
            }
        } else {
            ans = n == 2;
        }
        return ans;
    }

    public List<Integer> getPrimes() {
        return primes;
    }

    public void lock(){
        isLocked = true;
    }

    public void unlock(){
        synchronized(locker){
            locker.notify();
        }
        isLocked = false;
    }

}
