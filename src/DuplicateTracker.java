import java.util.*;

/*
 * This work complies with the JMU honor code. I have not discussed this assignment with others. I
 * will not share information about my solution. I have not accessed resources outside of those
 * posted to the course Canvas page.
 * 
 * Signed: Ethan Crawford
 */

public class DuplicateTracker {

	private List<Integer> ids;
	Set<Integer> duplicates;
	
	public DuplicateTracker() {
		ids = new LinkedList<Integer>();
		duplicates = new HashSet<>();
	}

	public void addID(int id) {
		ids.add(id);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getDuplicates() {
		Set<Integer> temp = new HashSet<>();
		for (Integer id : ids) {
			if (!temp.add(id)) {
				duplicates.add(id);
			}
		}

		List<Integer> ans = new ArrayList<>(duplicates);
		Collections.sort(ans);

		return ans;
	}

}
