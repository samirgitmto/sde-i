package mt5_data_sharing_between_threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
numberOfInstances is defined as a static variable, which means its value is shared among all instances of the class and stored in the heap memory,
 making it accessible to all threads. This illustrates key concepts of static variables and thread accessibility in Java, reinforcing your understanding
  of how data is shared in a multi-threaded environment.
 */

/*
the count variable is a local primitive type declared within the getAllNames() method, meaning each thread maintains its own separate copy on its stack
 when executing this method. This encapsulation ensures that concurrent threads do not interfere with each other’s count values, reinforcing key concepts
  of thread safety and local variable scope in Java.
 */

/*
allNames is indeed a local variable stored on the stack, while the ArrayList<String> object it refers to is allocated on the heap. This distinction is
 crucial for understanding memory management in Java, particularly how local references and objects are managed differently.
 */

public class Quiz1 {

	private Map<Integer, String>  idToNameMap;

	private static long numberOfInstances = 0;

	public Quiz1() {
		this.idToNameMap = new HashMap<>();
		numberOfInstances++;
	}

	public List<String> getAllNames() {
		int count = idToNameMap.size();
		List<String> allNames = new ArrayList<>();

		allNames.addAll(idToNameMap.values());

		return allNames;
	}

}     
