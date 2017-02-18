package com.hyunjae.coati;

import java.io.IOException;

public class Retry {

    private static final int MAX = 10;
    private static final long MILLIS = 1000;

    private Retry() {
    }

    public static <T> T execute(Retriable<T> retriable) throws IOException {
        for (int i = 0; i < MAX; i++) {
            try {
                return retriable.execute();
            } catch (IOException e) {
                doWait();
            }
        }

        throw new IOException();
    }

    private static void doWait() {
        try {
            Thread.sleep(MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
