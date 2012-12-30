


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
	
	public static int log_sum_exp(int[] dist){
		
		int max=0;
		for(int i: dist){
			if(max<i){
				max=i;
			}
		}
		int adjusted_sum = 0;
		for(int j=0; j<dist.length;j++){
			adjusted_sum+=Math.exp(dist[j]-max);
		}
		
		return max+adjusted_sum;
	
		
	}
	
	public static double[] array_scalar_multiply(double scalar, double[] array){
		double[] scaled = new double[array.length];
		for(int i=0;i<array.length;i++){
			scaled[i]=array[i]*scalar;
		}
		return scaled;
	}
	
	public static double[] cumsum(double[] dist){
		double sum = 0;
		double[] cdf = new double[dist.length];
		for(int i=0; i<dist.length; i++){
			sum+=dist[i];
			cdf[i]=sum;
		}
		return cdf;
	}
	
	public static int[] sample(double[] dist, int num_samples){
		double[] cdf = cumsum(dist);
		double sum = cdf[cdf.length-1];
		
		int[] samples = new int[num_samples];
		
		for(int i=0; i<num_samples; i++){
			double r = Math.random()*sum;
			samples[i]=searchsorted(cdf, r);
		}
		
		return samples;
	}
	
	public static int searchsorted(double[] dist, double r){
		int idx=0;
		//TODO This is an O(n) implementation of searchsorted.
		while(dist[idx]<=r){
			idx++;
		}
		return idx;
	}
	
	public static double array_sum(double[] vector){
		double sum = 0;
		for(double d:vector){
			sum+=d;
		}
		return sum;
	}
	
	public static double[] array_multiply(double[] first, double[] second){
		assert(first.length==second.length);
		double[] product = new double[first.length];
		for(int i =0; i < first.length; i++){
			product[i]=first[i]*second[i];
		}
		return product;
	}
	
	public static double[] array_divide(double[] num, double[] denom){
		//TODO insert try-catch or some assert to prevent dividing by zero
		assert(num.length==denom.length);
		double[] quotient = new double[num.length];
		for(int i=0; i < num.length; i++){
			quotient[i]=num[i]/denom[i];
		}
		return quotient;
		
	}
	
	public static double[] collapse(double[][] matrix, int axis){
		double[] collapsed;
		if(axis==0){
			collapsed = new double[matrix.length];
			for(int i = 0; i<matrix.length; i++){
				collapsed[i]=array_sum(matrix[i]);
			}
		}
		else{
			//TODO This isn't finished.  Can you invert a 2D array in Java?
			collapsed = new double[matrix[0].length];
		}
		return collapsed;
	}
	
	public static void main(String[] args) {
		//double[] dist = {0.5, 4.5, 10, 0, 1.1, 35};
		//int[] samples = sample(dist, 10);
		//for(int i: samples){
		//	System.out.println(i);
		//}
		int[][] matrix = new int[5][4];
		System.out.println(matrix[0].length);

	}

}
	



