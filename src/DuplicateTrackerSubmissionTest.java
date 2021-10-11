import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Unit and performance tests for DuplicateTracker
 * 
 * @author Nathan Sprague
 */
@TestMethodOrder(Alphanumeric.class)
public class DuplicateTrackerSubmissionTest {

  private static boolean passedCorrectness = false;


  /**
   * Generate random numbers with no repeats.
   */
  public ArrayList<Integer> randomSample(int howMany) {
    Random gen = new Random(100101);
    HashSet<Integer> seen = new HashSet<>();
    ArrayList<Integer> result = new ArrayList<>();

    while (result.size() < howMany) {
      int next = gen.nextInt();
      if (!seen.contains(next)) {
        seen.add(next);
        result.add(next);
      }
    }
    return result;
  }

  @Test
  public void test00Correctness() {
    DuplicateTracker tracker = new DuplicateTracker();

    ArrayList<Integer> allIds = randomSample(20);
    ArrayList<Integer> duplicates = new ArrayList<>(allIds.subList(0, 10));
    allIds.addAll(duplicates); // add some repeats
    allIds.addAll(duplicates);

    Collections.shuffle(allIds);

    Collections.sort(duplicates);

    for (int cur : allIds) {
      tracker.addID(cur);
    }

    List<Integer> result = tracker.getDuplicates();

    assertEquals(duplicates, result);


    // Make sure modifying the returned list of duplicates doesn't break the tracker.
    result.remove(0);
    result = tracker.getDuplicates();
    assertEquals(duplicates, result);
    passedCorrectness = true;
  }

  @Test
  public void test01Performance() {
    if (!passedCorrectness) {
      fail("Cannot run performance tests unless correctness tests pass.");
    }

    int maxScaling = 20;
    long[] small;
    long[] big;

    // increase the number of operations by a factor of ten, running time must increase by less than
    // a factor of 20.

    timePerformance(200, 2000, 100, false);
    timePerformance(200, 20000, 1000, false);

    small = timePerformance(200, 2000, 100, true);
    big = timePerformance(200, 20000, 1000, true);


    assertTrue("Performance scaling does not satisfy requirements.",
        big[0] < maxScaling * small[0]); // add timing
    assertTrue("Performance scaling does not satisfy requirements.",
        big[1] < maxScaling * small[1]); // duplicate timing
  }


  /*
   * getDuplicates should be O(#duplicates) addId should be O(log #duplicates)
   */
  private long[] timePerformance(int numGetDuplicateTrials, int numUnique, int numDups,
      boolean display) {

    long addTime = 0;
    long getTime = 0;

    DuplicateTracker tracker = new DuplicateTracker();
    ArrayList<Integer> allIds = randomSample(numUnique);
    ArrayList<Integer> duplicates = new ArrayList<>(allIds.subList(0, numDups));
    allIds.addAll(duplicates); // add some repeats

    Collections.shuffle(allIds);

    long start = System.nanoTime();
    for (int cur : allIds) {
      tracker.addID(cur);
    }
    addTime = System.nanoTime() - start;

    start = System.nanoTime();
    for (int i = 0; i < numGetDuplicateTrials; i++) {
      List<Integer> result = tracker.getDuplicates();
    }

    getTime = System.nanoTime() - start;

    if (display) {

      System.out.println("\nTIMING FOR DUPLICATE TRACKER: ");
      System.out.printf("TOTAL TIME: %.5f seconds.\n", (addTime + getTime) / 1000000000.0);

      System.out.printf(
          "Add:        %8d operations performed in %4.5f seconds, %8.2f microseconds per call.\n",
          allIds.size(), addTime / 1000000000.0, addTime / 1000.0 / allIds.size());

      System.out.printf(
          "Duplicates: %8d operations performed in %4.5f seconds, %8.2f microseconds per call.\n",
          numGetDuplicateTrials, getTime / 1000000000.0, getTime / 1000.0 / numGetDuplicateTrials);
    }


    return new long[] {addTime, getTime};

  }


}
