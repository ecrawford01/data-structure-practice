import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Random;
import org.junit.jupiter.api.Order;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Correctness and performance tests for TriageTracker
 * 
 * @author Nathan Sprague
 *
 */
@TestMethodOrder(Alphanumeric.class)
public class TriageTrackerSubmissionTest {

  private static boolean passedCorrectness = false;

  @Test
  public void test00Correctness() {
    TriageTracker triage = new TriageTracker();
    // Tests with no duplicates and no removal:
    triage.addPatient("A", 0);
    triage.addPatient("B", 1000000000);
    triage.addPatient("C", 5);
    triage.addPatient("D", 2);

    assertEquals("B", triage.nextPatient());
    assertEquals("C", triage.nextPatient());
    triage.addPatient("Z", 100);
    assertEquals("Z", triage.nextPatient());
    assertEquals("D", triage.nextPatient());
    assertEquals("A", triage.nextPatient());

    // Tests with removal, but no duplicates

    triage.addPatient("A", 0);
    triage.addPatient("B", 10);
    triage.addPatient("C", 5);
    triage.addPatient("D", 1);
    triage.addPatient("E", 2);
    triage.addPatient("F", 7);

    triage.removePatient("A");
    triage.removePatient("C");
    triage.removePatient("F");

    assertEquals("B", triage.nextPatient());
    assertEquals("E", triage.nextPatient());
    assertEquals("D", triage.nextPatient());

    // Tests with duplicates and removal

    triage.addPatient("A", 10);
    triage.addPatient("B", 0);
    triage.addPatient("G", 0);
    triage.addPatient("C", 0);
    triage.addPatient("D", 10);
    triage.addPatient("E", 10);
    triage.addPatient("F", 0);

    triage.removePatient("B");
    triage.removePatient("C");

    assertEquals("A", triage.nextPatient());
    assertEquals("D", triage.nextPatient());
    assertEquals("E", triage.nextPatient());
    assertEquals("G", triage.nextPatient());
    assertEquals("F", triage.nextPatient());

    triage.addPatient("A", 3);
    triage.addPatient("B", 1);
    triage.addPatient("C", 1);
    triage.addPatient("D", 2);

    triage.removePatient("B");
    triage.removePatient("C");

    assertEquals("A", triage.nextPatient());
    assertEquals("D", triage.nextPatient());

    passedCorrectness = true;

  }

  public long[] timePerformance(int numPatients, boolean display) {

    long addTime = 0;

    long removeTime = 0;
    int removeCounter = 0;
    long nextTime = 0;
    int nextCounter = 0;

    TriageTracker triage = new TriageTracker();

    // First, generate random priorities...
    Random gen = new Random(100101);
    ArrayList<Integer> priorities = new ArrayList<>();
    for (int i = 0; i < numPatients / 2; i++) {
      priorities.add(gen.nextInt(numPatients / 4)); // guarantees some
                                                    // random duplicates
    }
    for (int i = numPatients / 2; i < numPatients; i++) {
      priorities.add(100); // second half all duplicates
    }

    // TIME THE ADDS...
    long start = System.nanoTime();
    for (int i = 0; i < numPatients; i++) {
      triage.addPatient("" + i, priorities.get(i));
    }
    addTime = System.nanoTime() - start;

    // NOW TIME REMOVAL OF 40%
    start = System.nanoTime();
    for (int i = numPatients / 10; i < 5 * numPatients / 10; i++) {
      triage.removePatient("" + i);
      removeCounter++;
    }
    removeTime = System.nanoTime() - start;

    // NOW TIME NEXT CALL OF REMAINING 60%
    start = System.nanoTime();
    for (int i = 0; i < 6 * numPatients / 10; i++) {
      triage.nextPatient();
      nextCounter++;
    }
    nextTime = System.nanoTime() - start;

    if (display) {
      System.out.println("\nTIMING TRIAGE TRACKER:");

      System.out.printf("TOTAL TIME: %.5f seconds.\n",
          (addTime + removeTime + nextTime) / 1000000000.0);

      System.out.printf(
          "Add:    %8d operations performed in %4.5f seconds, %8.3f microseconds per call.\n",
          numPatients, addTime / 1000000000.0, addTime / 1000.0 / numPatients);

      System.out.printf(
          "Remove: %8d operations performed in %4.5f seconds, %8.3f microseconds per call.\n",
          removeCounter, removeTime / 1000000000.0, removeTime / 1000.0 / removeCounter);

      System.out.printf(
          "Next:   %8d operations performed in %4.5f seconds, %8.3f microseconds per call.\n",
          nextCounter, nextTime / 1000000000.0, nextTime / 1000.0 / nextCounter);
    }

    return new long[] {addTime, removeTime, nextTime};

  }

  @Test
  public void test01Performance() {

    if (!passedCorrectness) {
      fail("Cannot run performance tests unless correctness tests pass.");
    }

    int maxScaling = 20;

    timePerformance(2000, false);
    timePerformance(20000, false);

    long[] small = timePerformance(2000, true);
    long[] big = timePerformance(20000, true);

    assertTrue(big[0] < maxScaling * small[0],
        "Performance scaling does not satisfy requirements (add).");
    assertTrue(big[1] < maxScaling * small[1],
        "Performance scaling does not satisfy requirements (remove).");
    assertTrue(big[2] < maxScaling * small[2],
        "Performance scaling does not satisfy requirements (add).");



  }

}
