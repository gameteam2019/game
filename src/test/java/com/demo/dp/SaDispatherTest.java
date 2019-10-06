package com.demo.dp;

import com.demo.dp.model.Node;
import org.junit.Test;

import java.util.*;

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
    @Test
    public void testIteratorMap() {
        Map<String,Integer> test = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            test.put(i+":", i);
        }
        Iterator<Map.Entry<String, Integer>> iterator = test.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,Integer> next = iterator.next();
            if (next.getValue().equals(9)) {
                System.out.println("remove:"+next.getValue());
                iterator.remove();
                continue;
            }
            System.out.println(next.getValue());

        }
        System.out.println("Map.size:"+test.size());
    }
}
