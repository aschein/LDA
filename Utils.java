package corpus;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {
	
	public static ArrayList<Integer> binCount(ArrayList<Integer> intlist){
		
		Integer max = Collections.max(intlist);
		int[] bincount = new int[max.intValue()+1];
		
		for(Integer i: intlist){
			bincount[i.intValue()]++;
		}
		ArrayList<Integer> bincountList = new ArrayList<Integer>(bincount.length);
		
		for(int j = 0; j<bincount.length; j++){
			bincountList.add(new Integer(bincount[j]));
		}
		
		return bincountList;
	}
	
}


