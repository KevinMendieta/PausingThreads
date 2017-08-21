/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class Control extends Thread {

    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];

    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];
        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1);
    }

    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
           pft[i].start();
        }
        boolean isAlive = pft[0].isAlive();
        for(int i = 1; i < pft.length; i++) {
           isAlive = isAlive || pft[i].isAlive();
        }
        while (isAlive) {
            try {
                Thread.sleep(TMILISECONDS);
                for(int i = 0; i < pft.length; i++){
                    pft[i].lock();
                }
                int total = 0;
                for(int i = 0; i < pft.length; i++){
                    total += pft[i].getPrimes().size();
                }
                System.out.println("Actually are " + total + " calculated primes.");
                (new BufferedReader(new InputStreamReader(System.in))).readLine();
                for(int i = 0; i < pft.length; i++){
                    pft[i].unlock();
                }
            }catch (Exception e) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, e);
            }
           isAlive = pft[0].isAlive();
           for (int i = 1; i < pft.length; i++) {
                isAlive = isAlive || pft[i].isAlive();
           }
        }
    }

}
