package com.demo.dp;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SaDispatherTest {
    @Test
    public void testRandom() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Math.random());
        }
    }
    @Test
    public void testMapOrder() {
        Map<String,Integer> test = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            test.put(i+":", i);
        }
        int cnt =0;
        for (Map.Entry<String, Integer> data : test.entrySet()) {

            if (cnt == 22) {
                System.out.println(data.getKey());
            }
            cnt++;
        }

    }
    @Test
    public void testSetInteger() {
        Set<Integer> test = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            test.add(i);
        }
        System.out.println(test.contains(22));
    }
}
