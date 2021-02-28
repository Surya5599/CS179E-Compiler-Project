import java.util.*;
import java.util.Map.Entry;

public class LinearScan {

	private Map<String, LivenessInterval> active;
	private Map<String, Register> registers;
	private List<Register> freeRegisters;
	private Set<String> stackLocation;

	public LinearScan() {
		active = new LinkedHashMap<String, LivenessInterval>();
		registers = new LinkedHashMap<String, Register>();
		Register r = new Register();
		freeRegisters = new ArrayList<Register>();
		freeRegisters.addAll(r.getCallerSaved());
		stackLocation = new HashSet<>();
	}

	public Map<String, Register> getRegisterMap(){
		return this.registers;
	}

	public void performLinearScan(Map<String, LivenessInterval> li) {
		LinearScanRegisterAllocation(li);
	}

	public  void LinearScanRegisterAllocation(Map<String, LivenessInterval> li) {
		Map<String, LivenessInterval> l = sortbyStart(li);
		for (Map.Entry<String, LivenessInterval> i : l.entrySet()) {
			ExpireOldIntervals(i);
			if (active.size() == freeRegisters.size()) {
				SpillAtInterval(i);
			} else {
				registers.put(i.getKey(), freeRegisters.get(0));
				freeRegisters.remove(0);
				active.put(i.getKey(), i.getValue());
			}
		}
	}

	public  void ExpireOldIntervals(Map.Entry<String, LivenessInterval> i) {
		active = sortByEnd(active);
		List<String> removeArr = new ArrayList<>(); 
		for (Map.Entry<String, LivenessInterval> j : active.entrySet()) {
			if (j.getValue().getStop() >= (i.getValue()).getStart()) {
				return;
			}
			removeArr.add(j.getKey());
			Register r = registers.get(j.getKey());
			freeRegisters.add(r);
			sortFreeRegisters();
		}
		for(String s: removeArr){
			active.remove(s);
		}
	}

	public  void SpillAtInterval(Map.Entry<String, LivenessInterval> i) {
		Map.Entry<String, LivenessInterval> spill = getLast(active);
		if(spill != null){
			if (spill.getValue().getStop() > i.getValue().getStop() ) {
				String key = i.getKey();
				Register r = registers.get(spill.getKey());
				registers.replace(key, r);
				stackLocation.add(spill.getKey());
				active.remove(spill.getKey());
				active.put(i.getKey(), i.getValue()); 
				active = sortByEnd(active);
			}
		}
		else{
			stackLocation.add(i.getKey());
		}
	}

	public Map.Entry<String, LivenessInterval> getLast(
			Map<String, LivenessInterval> map) {
		Iterator<Map.Entry<String, LivenessInterval>> iterator = map.entrySet().iterator();
		Map.Entry<String, LivenessInterval> result = null;
		while (iterator.hasNext()) {
			result = iterator.next();
		}
		return result;
	}

	public Map<String, LivenessInterval> sortbyStart(Map<String, LivenessInterval> li) {
			List<Entry<String, LivenessInterval>> sorted = new ArrayList<>(li.entrySet());
			Collections.sort(sorted, new Comparator<Entry<String, LivenessInterval>>() {
				@Override
				public int compare(Entry<String, LivenessInterval> o1, Entry<String, LivenessInterval> o2) {
					return o1.getValue().compareStart(o2.getValue());
				}
			});
			Map<String, LivenessInterval> sortedMap = new LinkedHashMap<String, LivenessInterval>();
			for (Entry<String, LivenessInterval> entry : sorted) {
				sortedMap.put(entry.getKey(), entry.getValue());
			}

			return sortedMap;
		}

		public Map<String, LivenessInterval> sortByEnd(Map<String, LivenessInterval> li) 
		{ 
			List<Entry<String,LivenessInterval>> sorted = new ArrayList<>(li.entrySet());
			Collections.sort(sorted, new Comparator<Entry<String, LivenessInterval>>() {
				@Override
					public int compare(Entry<String, LivenessInterval> o1, Entry<String, LivenessInterval> o2) {
							return o1.getValue().compareStop(o2.getValue());
				}
			});
		Map<String, LivenessInterval> sortedMap = new LinkedHashMap<String, LivenessInterval>();
    for (Entry<String, LivenessInterval> entry : sorted)
    {
        sortedMap.put(entry.getKey(), entry.getValue());
    }

    return sortedMap;
		}

		private void sortFreeRegisters(){
			Collections.sort(freeRegisters, new Comparator<Register>() {
				@Override
				public int compare(Register o1, Register o2)
				{
	
						return  o1.toString().compareTo(o2.toString());
				}
		});
		}
}