//import corpus.*;
import cc.mallet.util.*;
import java.util.HashMap;
public class LDA {
	
	Corpus corpus; // corpus of documents
	double alpha; // concentration parameter for the Dirichlet prior over thetas
	double beta; // concentration parameter for the Dirichlet prior over phis
	double[] m; // T-dimensional mean of the Dirichlet prior over thetas
	double[] n;// V-dimensional mean of the Dirichlet prior over phis
	
	int D; // number of documents
	int V; // vocabulary size
	int T; // number of model components (i.e., topics)
	
	double[] alpha_m;	
	double[] beta_n;
	
	//TODO these need to be filled with ones not zeros at initialization
	double[][] Nvt_plus_beta_n;
	double[] Nt_plus_beta;
	
	double[][] Ntd_plus_alpha_m;
	double[] Nd_plus_alpha;
	
	HashMap<Integer,int[]> z;
	
	public LDA(Corpus corpus, double alpha, double beta, double[] m, double[] n){
		
		this.corpus = corpus;
		
		this.alpha = alpha;
		this.beta = beta;
		this.m = m;
		this.n = n;
		
		D = corpus.size();
		V = corpus.getVocab().size();
		T = m.length;
		
		assert(n.length==V);
		
		alpha_m = Utils.array_scalar_multiply(alpha, m);
		beta_n = Utils.array_scalar_multiply(beta, n);
		
		Ntd_plus_alpha_m = new double[D][T]; //NOTE: the ordering of dimensions does not match Nvt
		Nt_plus_beta = new double[T];
		Nvt_plus_beta_n = new double[V][T];
		Nt_plus_beta = new double[T];
		
		for(int doc = 0; doc < corpus.size(); doc++){
			int num_tokens = corpus.get(doc).size();
			z.put(new Integer(doc), new int[num_tokens]);
			
		}	
	}
	
	public void gibbs_iteration(boolean init){
		
		for(int d = 0; d < D; d++){
			Document doc = corpus.get(d);
			int[] zd = z.get(d);
			
			for(int n = 0; n < doc.size(); n++){
				int v = doc.getW()[n];
				int t = zd[n];
				
				if(!init){
					Nvt_plus_beta_n[v][t] -=1;
					Nt_plus_beta[t]-=1;
					Ntd_plus_alpha_m[d][t]-=1;
				
				double[] dist = Utils.array_multiply(Utils.array_divide(Nvt_plus_beta_n[v],Nt_plus_beta), Ntd_plus_alpha_m[d]);
				t = Utils.sample(dist, t)[0];
				
				Nvt_plus_beta_n[v][t]+=1;
				Nt_plus_beta[t]+=1;
				Ntd_plus_alpha_m[d][t]+=1;
				
				if(init){
					Nd_plus_alpha[d]+=1;
				}
				
				zd[n]=t;
				
				}
			}
		}
	}

	public void gibbs(int num_itns){
		//System.out.println("Initialization");
		gibbs_iteration(true);
		for(int i=1; i<num_itns+1; i++){
			gibbs_iteration(false);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
