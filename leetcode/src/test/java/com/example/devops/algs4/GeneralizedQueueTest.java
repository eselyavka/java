package com.example.devops.algs4;

import org.junit.Test;

import static org.junit.Assert.*;

public class GeneralizedQueueTest {
    @Test
    public void TestGeneralizedQueueCorrectness() {
        GeneralizedQueue generalizedQueue = new GeneralizedQueue();
        String item = "dsdd";
        String item2 = "aaaaaa";
        String item3 = "rrrr";
        String item4 = "4eee4";
        generalizedQueue.append(item);
        generalizedQueue.append(item2);
        generalizedQueue.append(item3);
        generalizedQueue.append(item4);
        assertEquals(generalizedQueue.removeIthItem(1), item2);
        assertEquals(generalizedQueue.remove(), item);
        assertEquals(generalizedQueue.remove(), null);
        assertEquals(generalizedQueue.remove(), item3);
        assertEquals(generalizedQueue.remove(), item4);
    }
}