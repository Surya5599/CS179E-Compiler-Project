package RegisterAllocation;

import java.util.*;

public class LivenessInterval {
	public String variable;
	private int start;
	private int stop;
	private List<Integer> useLocations;

	public LivenessInterval(String v, int b, int e) {
		this.variable = v;
		this.start = b;
		this.stop = e;
		this.useLocations = new ArrayList<>();
	}

	public String getName() {
		return this.variable;
	}

	public void addUse(int x) {
		useLocations.add(x);
	}

	public int useSize() {
		return useLocations.size();
	}

	public void setStart(int x) {
		this.start = x;
	}

	public int getStart() {
		return this.start;
	}

	public void setStop(int x) {
		this.stop = x;
	}

	public int getStop() {
		return this.stop;
	}

	public int compareStart(LivenessInterval value) {
		Integer s1 = this.start;
		Integer s2 = value.getStart();
		return s1.compareTo(s2);
	}

	public int compareStop(LivenessInterval value) {
		Integer s1 = this.stop;
		Integer s2 = value.getStop();
		return s1.compareTo(s2);
	}

	public LivenessInterval getValue() {
		return null;
	}

}