package com.transaction.bankingapplication.utils;

public class GenericUtils {
    public static String transactionReferenceNumber() {
        byte[] lock = new byte[0];
        long w = 100000000;
        long r = 0;
        synchronized (lock) {
            r = (long) ((Math.random() + 1) * w);
        }

        return System.currentTimeMillis() + String.valueOf(r).substring(1);
    }
    private GenericUtils(){

    }
}
