import java.util.*;
import java.util.Map.Entry;

public class EventSequencer {

	/*
	 * This work complies with the JMU honor code. I have not discussed this
	 * assignment with others. I will not share information about my solution. I
	 * have not accessed resources outside of those posted to the course Canvas
	 * page.
	 * 
	 * Signed: Ethan Crawford
	 */

	private Map<Integer, String> events;
	private int checker;

	public EventSequencer() {
		events = new TreeMap<>();
		checker = -1;
	}

	public void addEvent(int timeStamp, String event) {
		events.put(timeStamp, event);
	}

	public String nextEvent() {
		
		for (Integer event : events.keySet()) {
			if (event > checker){
				checker = event;
				String ans = events.get(event);
				events.remove(event);
				return ans;
			}
		}
		return null;
	}

}
