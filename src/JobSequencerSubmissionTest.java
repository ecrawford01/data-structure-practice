import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Correctness and performance tests for JobSequencer
 * 
 * @author Nathan Sprague
 *
 */
@TestMethodOrder(Alphanumeric.class)
public class JobSequencerSubmissionTest {

  private static boolean passedCorrectness = false;

  @Test
  public void test00Correctness() {
    JobSequencer sequencer = new JobSequencer();

    // Tests from description
    sequencer.addJob("A", 101);
    sequencer.addJob("B", 105);
    sequencer.addJob("A", 93);
    sequencer.addJob("B", 202);

    assertEquals(101, sequencer.nextJob("A"));
    assertEquals(105, sequencer.nextJob("B"));
    assertEquals(202, sequencer.nextJob("B"));
    assertEquals(93, sequencer.nextJob("A"));

    // Interleave some similar tests
    sequencer.addJob("A", 105);
    assertEquals(105, sequencer.nextJob("A"));

    sequencer.addJob("B", 101);
    assertEquals(101, sequencer.nextJob("B"));

    sequencer.addJob("A", 202);
    assertEquals(202, sequencer.nextJob("A"));

    sequencer.addJob("B", 93);
    assertEquals(93, sequencer.nextJob("B"));

    // Larger scale test
    String typesIn = "ABCDEF";
    String typesOut = "FEDCBA";
    for (int i = 0; i < typesIn.length(); i++) {
      for (int j = 0; j < 10; j++) {
        sequencer.addJob(typesIn.substring(i, i + 1), typesIn.charAt(i) * 100 + j);
      }
    }

    for (int i = 0; i < typesOut.length(); i++) {
      for (int j = 0; j < 10; j++) {
        int jobID = sequencer.nextJob(typesIn.substring(i, i + 1));
        assertEquals(typesIn.charAt(i) * 100 + j, jobID);
      }
    }
    passedCorrectness = true;
  }

  public long[] timePerformance(int numJobsPerType, int numTrials, boolean display) {
    long addTime = 0;
    int addCounter = 0;
    long getTime = 0;
    int getCounter = 0;

    JobSequencer sequencer = new JobSequencer();

    String typesIn = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    typesIn = typesIn + typesIn.toLowerCase();
    String typesOut = "";

    for (int i = typesIn.length() - 1; i >= 0; i--) {
      typesOut += typesIn.substring(i, i + 1);
    }

    for (int trial = 0; trial < numTrials; trial++) {

      long start = System.nanoTime();
      for (int i = 0; i < typesIn.length(); i++) {
        for (int j = 0; j < numJobsPerType; j++) {
          sequencer.addJob(typesIn.substring(i, i + 1), typesIn.charAt(i) * 100 + j);
          addCounter++;
        }
      }
      addTime += (System.nanoTime() - start);


      start = System.nanoTime();

      for (int i = 0; i < typesOut.length(); i++) {
        for (int j = 0; j < numJobsPerType; j++) {
          int jobID = sequencer.nextJob(typesIn.substring(i, i + 1));
          getCounter++;
        }
      }
      getTime += (System.nanoTime() - start);
    }

    if (display) {

      System.out.println("\nTIMING JOB SEQUENCER:");
      System.out.printf("TOTAL TIME: %.5f seconds.\n", (addTime + getTime) / 1000000000.0);

      System.out.printf(
          "Add:  %8d operations performed in %4.5f seconds, %8.3f microseconds per call.\n",
          addCounter, addTime / 1000000000.0, addTime / 1000.0 / addCounter);

      System.out.printf(
          "Next: %8d operations performed in %4.5f seconds, %8.3f microseconds per call.\n",
          getCounter, getTime / 1000000000.0, getTime / 1000.0 / getCounter);
    }


    return new long[] {addTime, getTime};
  }

  @Test
  public void test01Perfomance() {
    if (!passedCorrectness) {
      fail("Cannot run performance tests unless correctness tests pass.");
    }

    int maxScaling = 20;

    timePerformance(100, 2, false);
    timePerformance(1000, 2, false);

    long[] small = timePerformance(100, 2, true);
    long[] big = timePerformance(1000, 2, true);

    assertTrue(big[0] < maxScaling * small[0],
        "Performance scaling does not satisfy requirements (add)."); 
    assertTrue(big[1] < maxScaling * small[1],
        "Performance scaling does not satisfy requirements (next)."); 



  }
}
