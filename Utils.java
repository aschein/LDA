package corpus;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {
	
	public static int[] binCount(int[] intlist){
		int max = 0;
		for(int i: intlist){
			if(i>max){
				max=i;
			}
		}
		int[] bincount = new int[max+1];
		
		for(int i: intlist){
			bincount[i]++;
		}

		return bincount;
	}
}
	



