import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Correctness and performance tests for EventSequencer
 * 
 * @author Nathan Sprague
 *
 */
@TestMethodOrder(Alphanumeric.class)
public class EventSequencerSubmissionTest {

  private static boolean passedCorrectness = false;


  /**
   * Generate random numbers with no repeats in a given range.
   */
  public ArrayList<Integer> randomSample(int min, int max, int howMany) {
    Random gen = new Random(100101);
    HashSet<Integer> seen = new HashSet<>();
    ArrayList<Integer> result = new ArrayList<>();

    while (result.size() < howMany) {
      int next = gen.nextInt(max - min) + min;
      if (!seen.contains(next)) {
        seen.add(next);
        result.add(next);
      }
    }
    return result;
  }


  @Test
  public void test00Correctness() {

    EventSequencer sequencer = new EventSequencer();
    String events = "ABCDEFGHIJKLMNOPQRSTUV";
    String moreEvents = "XYZ";

    for (int i = 0; i < events.length(); i++) {
      sequencer.addEvent(i * 2, events.substring(i, i + 1));
    }

    assertEquals("A", sequencer.nextEvent()); // 0
    assertEquals("B", sequencer.nextEvent()); // 2
    assertEquals("C", sequencer.nextEvent()); // 4
    assertEquals("D", sequencer.nextEvent()); // 6

    for (int i = 0; i < moreEvents.length(); i++) {
      sequencer.addEvent(9 - 2 * i, moreEvents.substring(i, i + 1)); // 9, 7, 5
    }

    assertEquals("Y", sequencer.nextEvent()); // 7
    assertEquals("E", sequencer.nextEvent()); // 8
    assertEquals("X", sequencer.nextEvent()); // 9
    assertEquals("F", sequencer.nextEvent()); // 10
    passedCorrectness = true;

  }

  public long[] timePerformance(int numEvents, boolean display) {
    long addTime = 0;
    int addCounter = 0;
    long getTime = 0;
    int getCounter = 0;
    int maxTime = numEvents * 10;

    EventSequencer sequencer = new EventSequencer();

    ArrayList<Integer> times = randomSample(1, maxTime, numEvents);

    long start = System.nanoTime();
    for (int time : times) {
      sequencer.addEvent(time, "Some Event");
      addCounter++;
    }
    addTime += (System.nanoTime() - start);


    start = System.nanoTime();
    for (int i = 0; i < numEvents / 2; i++) {
      String event = sequencer.nextEvent();
      getCounter++;
    }
    getTime += (System.nanoTime() - start);


    // Half of these will be before.
    times = randomSample(maxTime / 2, maxTime + maxTime / 2, numEvents);

    start = System.nanoTime();
    for (int time : times) {
      sequencer.addEvent(time, "Some Event");
      addCounter++;
    }
    addTime += (System.nanoTime() - start);


    start = System.nanoTime();
    for (int i = 0; i < numEvents / 2; i++) {
      String event = sequencer.nextEvent();
      getCounter++;
    }
    getTime += (System.nanoTime() - start);

    if (display) {
      System.out.println("\nEVENT SEQUENCER TIMING:");
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
  public void test01Performance() {

    if (!passedCorrectness) {
      fail("Cannot run performance tests unless correctness tests pass.");
    }

    int maxScaling = 20;
    long[] small;
    long[] big;

    // increase the number of operations by a factor of ten, running time must increase by less than
    // a factor of 20.


    timePerformance(1000, false);
    timePerformance(10000, false);

    small = timePerformance(1000, true);
    big = timePerformance(10000, true);


    assertTrue(big[0] < maxScaling * small[0],
        "Performance scaling does not satisfy requirements. (add)"); // add timing
    assertTrue(big[1] < maxScaling * small[1],
        "Performance scaling does not satisfy requirements. (next)"); // duplicate timing


  }

}
