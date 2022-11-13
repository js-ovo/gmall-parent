package com.jing.gmall.search.repository;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

public class StreamTest {
    @Test
    public void test(){
        int[] i = new int[10];
        i = null;
        IntStream stream = Arrays.stream(i);
        System.out.println(stream);
    }
}
