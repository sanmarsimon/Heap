package tests;

import org.junit.jupiter.api.Test;
import tp4.Heap;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HeapTest {

    @Test
    void simpleInsertPop() {
        Heap<Integer> heap = new Heap<>();
        heap.insert(1);
        assertEquals(1, heap.size());
        assertEquals(1, heap.pop());
    }

    @Test
    void simpleSmallestPop() {
        Heap<Integer> heap = new Heap<>();
        heap.insert(1);
        heap.insert(2);
        assertEquals(2, heap.size());
        assertEquals(1, heap.pop());

        heap.insert(0);
        assertEquals(2, heap.size());
        assertEquals(0, heap.pop());
    }

    @Test
    void simpleHeapWithList() {
        Heap<Integer> heap = new Heap<>(List.of(1,2,3,4,5,6));
        assertEquals(6, heap.size());
        assertEquals(1, heap.pop());

        heap.insert(0);
        assertEquals(6, heap.size());
        assertEquals(0, heap.pop());
    }

    @Test
    void worksReverse() {
        Heap<Integer> heap = new Heap<>(false, List.of(1,2,3,4,5,6));
        assertEquals(6, heap.size());
        assertEquals(6, heap.pop());

        heap.insert(0);
        assertEquals(6, heap.size());
        assertEquals(5, heap.pop());
    }

    @Test
    void worksWithPeek() {
        Heap<Integer> heap = new Heap<>(false, List.of(1,2,3,4,5,6));
        assertEquals(6, heap.size());
        assertEquals(6, heap.peek());

        heap.insert(0);
        assertEquals(7, heap.size());
        assertEquals(6, heap.peek());
    }

    @Test
    void testComplexityWithArrayInitializationWithBarometer() throws IOException {
        int increaseRate = 100;
        int maxSize = 10000;
        Random rnd = new Random();
        ArrayList<Double> Xs = new ArrayList<>();
        ArrayList<Double> Ys = new ArrayList<>();
        for (int listSize = increaseRate; listSize < maxSize; listSize += increaseRate) {
            List<Integer> randomInts = rnd.ints(listSize, -listSize, listSize).boxed().collect(Collectors.toList());
            List<MockValue> randomMocks = randomInts.stream().map(MockValue::new).collect(Collectors.toList());
            Heap<MockValue> heap = new Heap<>(false, randomMocks);
            assertEquals(Collections.max(randomInts), heap.peek().getValue());
            // Count the barometer operation for complexity.
            int totalBarometer = 0;
            for (MockValue value : heap) {
                totalBarometer += value.getBarometerCounter();
            }
            Xs.add((double)listSize);
            Ys.add((double)totalBarometer);
        }

        LinearRegression regression = new LinearRegression(Xs.toArray(new Double[0]), Ys.toArray(new Double[0]));
        // The trend should be linear between input size and time => R2 ~= 1 => O(n).
        System.out.println(regression.R2());
        regression.plot("testComplexityWithArrayInitializationWithBarometer");
        assertEquals(1.0, regression.R2(), 0.01);
    }

    @Test
    void testComplexitySortWithBarometer() throws IOException {
        int increaseRate = 100;
        int maxSize = 10000;
        Random rnd = new Random();
        ArrayList<Double> Xs = new ArrayList<>();
        ArrayList<Double> Ys = new ArrayList<>();
        for (int listSize = increaseRate; listSize < maxSize; listSize += increaseRate) {
            Heap<MockValue> heap = new Heap<>();
            List<Integer> randomInts = rnd.ints(listSize, -listSize, listSize)
                    .boxed().sorted().collect(Collectors.toList());
            List<MockValue> randomMocks = randomInts.stream().map(MockValue::new).collect(Collectors.toList());
            Collections.reverse(randomMocks);
            for (MockValue mock : randomMocks) {
                heap.insert(mock);
            }
            List<MockValue> sorted = heap.sort();
            assertEquals(randomInts, sorted.stream().map(MockValue::getValue).collect(Collectors.toList()));
            // Count the barometer operation for complexity.
            int totalBarometer = 0;
            for (MockValue value : sorted) {
                totalBarometer += value.getBarometerCounter();
            }
            Xs.add((double)listSize);
            Ys.add((double)totalBarometer);
        }

        LinearRegression regression = new LinearRegression(Xs.toArray(new Double[0]), Ys.toArray(new Double[0]));
        // The trend should be linear between input size and time => R2 ~= 1 => O(nlog(n)). -> tends to n on large numbers
        System.out.println(regression.R2());
        regression.plot("testComplexitySortWithBarometer");
        assertEquals(1.0, regression.R2(), 0.01);
    }

    @Test
    void testComplexitySortWithTime() throws IOException {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            int increaseRate = 20;
            int maxSize = 1000000;
            Random rnd = new Random();
            ArrayList<Double> Xs = new ArrayList<>();
            ArrayList<Double> Ys = new ArrayList<>();
            for (int listSize = increaseRate; listSize < maxSize; listSize *= increaseRate) {
                List<Integer> randomInts = rnd.ints(listSize, -listSize, listSize).boxed().collect(Collectors.toList());
                long startTime = System.nanoTime();
                Heap<Integer> heap = new Heap<>(randomInts);
                long endTime = System.nanoTime();
                Collections.sort(randomInts);
                assertEquals(randomInts, heap.sort());

                long totalBarometer = endTime - startTime;
                Xs.add((double)listSize);
                Ys.add((double)totalBarometer);
            }

            LinearRegression regression = new LinearRegression(Xs.toArray(new Double[0]), Ys.toArray(new Double[0]));
            // The trend should be linear between input size and time => R2 ~= 1 => O(nlog(n)). -> tends to n on large numbers
            System.out.println(regression.R2());
            regression.plot("testComplexitySortWithTime");
            assertEquals(1.0, regression.R2(), 0.1);
        }, "Votre algorithme n'est probablement pas en O(nlog(n))");
    }
}

class MockValue implements Comparable<MockValue> {
    private int barometerCounter = 0;
    private final Integer value;

    public MockValue(Integer value) {
        this.value = value;
    }

    public int getBarometerCounter() {
        return barometerCounter;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public int compareTo(MockValue o) {
        ++barometerCounter;
        return value.compareTo(o.value);
    }
}