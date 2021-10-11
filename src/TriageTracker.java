import java.util.*;

/*
 * This work complies with the JMU honor code. I have not discussed this assignment with others. I
 * will not share information about my solution. I have not accessed resources outside of those
 * posted to the course Canvas page.
 * 
 * Signed: Ethan Crawford
 */

public class TriageTracker {

	private Queue<Integer> queue;
	private Map<String, Integer> map;

	public TriageTracker() {
//		map = new LinkedHashMap<>();
		queue = new PriorityQueue<>(Collections.reverseOrder());
		map = new LinkedHashMap<String, Integer>();
	}

	@SuppressWarnings("unchecked")
	public void addPatient(String id, int priority) {
		queue.add(priority);
		map.put(id, priority);

	}

	public String nextPatient() {

		for (String patient : map.keySet()) {
			if (map.get(patient).equals(queue.peek())) {
				String ans = patient;
				map.remove(patient);
				queue.poll();
				return ans;
			}
		}

		return null;
	}

	public void removePatient(String id) {
			queue.remove(map.get(id));
			map.remove(id);
	}

}
