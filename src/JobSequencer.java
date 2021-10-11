import java.util.*;

/*
 * This work complies with the JMU honor code. I have not discussed this assignment with others. I
 * will not share information about my solution. I have not accessed resources outside of those
 * posted to the course Canvas page.
 * 
 * Signed: Ethan Crawford
 */

public class JobSequencer {
	private List<Map<String, Integer>> jobs;

	public JobSequencer() {
		jobs = new LinkedList<>();
	}

	public void addJob(String jobType, int jobID) {
		Map<String, Integer> m = new LinkedHashMap<>();
		m.put(jobType, jobID);
		jobs.add(m);

	}

	public int nextJob(String jobType) {
		int answer = 0;

		for (Map<String, Integer> job : jobs) {
			if (job.containsKey(jobType)) {
				answer = job.get(jobType);
				jobs.remove(job);
				return answer;
			}
		}

		return answer;
	}

}
