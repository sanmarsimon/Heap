package tests;

import org.junit.jupiter.api.Test;
import tp4.Interview;
import tp4.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class InterviewTest {
    @Test
    void simpleEmptyWorking() {
        Integer circleSize = 1;
        List<Integer> centers = List.of(0, 3);
        List<Point> points = List.of(new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3));
        List<Integer> friendsToRemove = Interview.getFriendsToRemove(circleSize, centers, points);
        assertEquals(List.of(), friendsToRemove);
    }

    @Test
    void simpleSingleWorking() {
        Integer circleSize = 1;
        List<Integer> centers = List.of(0, 2);
        List<Point> points = List.of(new Point(0, 0), new Point(1, 1), new Point(2, 2));
        List<Integer> friendsToRemove = Interview.getFriendsToRemove(circleSize, centers, points);
        assertEquals(List.of(1), friendsToRemove);
    }

    @Test
    void simpleAllWorking() {
        Integer circleSize = 2;
        List<Integer> centers = List.of(0, 1, 2);
        List<Point> points = List.of(new Point(0, 0), new Point(1, 1), new Point(2, 2));
        List<Integer> friendsToRemove = Interview.getFriendsToRemove(circleSize, centers, points);
        assertEquals(List.of(0, 1, 2), friendsToRemove);
    }

    @Test
    void simpleWorking() throws FileNotFoundException {
        for (int i = 0; i < 10; ++i) {
            final Scanner inputScanner = new Scanner(new File("generated_q2/input/input0" + i + ".txt"));
            Integer circleSize = Integer.parseInt(inputScanner.nextLine());
            List<Integer> centers = Arrays
                    .stream(inputScanner.nextLine().split(" +"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            List<Point> points = Arrays
                    .stream(inputScanner.nextLine().split(","))
                    .map(Point::new)
                    .collect(Collectors.toList());

            List<Integer> friendsToRemove = Interview.getFriendsToRemove(circleSize, centers, points);

            final Scanner outputScanner = new Scanner(new File("generated_q2/output/output0" + i + ".txt"));
            List<Integer> answerFriends = Arrays
                    .stream(outputScanner.nextLine().split(" +"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            if (answerFriends.get(0).equals(-1)) {
                answerFriends.remove(0);
            }
            assertEquals(answerFriends, friendsToRemove);
        }
    }

    @Test
    void testComplexityInterviewWithTime() throws IOException {
        assertTimeoutPreemptively(Duration.ofSeconds(1000), () -> {
            int increaseRate = 100;
            int maxSize = 3000;
            ArrayList<Double> Xs = new ArrayList<>();
            ArrayList<Double> Ys = new ArrayList<>();
            for (int listSize = increaseRate; listSize < maxSize; listSize += increaseRate) {
                System.out.println(listSize);
                Integer circleSize = listSize - 1;
                List<Integer> centers = IntStream.range(0, listSize).boxed().collect(Collectors.toList());
                List<Point> points = new ArrayList<>();
                for (int i = 0; i < listSize; ++i) {
                    Integer x = ThreadLocalRandom.current().nextInt(-listSize, listSize);
                    Integer y = ThreadLocalRandom.current().nextInt(-listSize, listSize);
                    points.add(new Point(x, y));
                }

                long startTime = System.nanoTime();
                List<Integer> friendsToRemove = Interview.getFriendsToRemove(circleSize, centers, points);
                long endTime = System.nanoTime();
                assertEquals(centers, friendsToRemove);

                long totalBarometer = endTime - startTime;
                Xs.add((double)listSize * listSize);
                Ys.add((double)totalBarometer);
            }

            LinearRegression regression = new LinearRegression(Xs.toArray(new Double[0]), Ys.toArray(new Double[0]));
            // The trend should be linear between input size and time => R2 ~= 1 => O(n^2 * log(n)). -> tends to n^2 on large numbers
            System.out.println(regression.R2());
            regression.plot("testComplexityInterviewWithTime");
            assertEquals(1.0, regression.R2(), 0.1);
        }, "Votre algorithme n'est probablement pas en O(n^2 * log(n))");
    }
}