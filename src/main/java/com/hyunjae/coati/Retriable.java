package com.hyunjae.coati;

import java.io.IOException;

public interface Retriable<T> {
    T execute() throws IOException;
}
