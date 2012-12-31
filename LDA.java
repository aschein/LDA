//import corpus.*;
import cc.mallet.util.*;

import java.util.ArrayList;
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
		Nd_plus_alpha = new double[D];
		Nvt_plus_beta_n = new double[V][T];
		Nt_plus_beta = new double[T];
		
		setup();
		z = new HashMap<Integer, int[]>();
		for(int doc = 0; doc < corpus.size(); doc++){
			int num_tokens = corpus.get(doc).size();
			z.put(new Integer(doc), new int[num_tokens]);
			
		}	
	}
	public void setup(){
		for(int d=0;d<D;d++){
			Nd_plus_alpha[d]=alpha;
			for(int t=0; t<T;t++){
				Ntd_plus_alpha_m[d][t]=alpha_m[t];
			}
		}
		for(int v=0; v<V; v++){
			for(int t=0; t<T; t++){
				Nt_plus_beta[t]=beta;
				Nvt_plus_beta_n[v][t]=beta_n[v];
			}
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
				t = Utils.sample(dist, 1)[0];
				
				
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
		System.out.println("Initialization");
		gibbs_iteration(true);
		double log_e = log_evidence_corpus_and_z();
		System.out.println(log_e);
		for(int i=1; i<num_itns+1; i++){
			gibbs_iteration(false);
			log_e = log_evidence_corpus_and_z();
			System.out.println(log_e);
		}
	}
	
	public double log_evidence_corpus_and_z(){
		double log_evidence_and_z = 0.0;
		
		for(int t=0;t < T;t++){
			int Nt = (int)(Nt_plus_beta[t]-beta);
			
			for(int k=0; k<Nt; k++){
				double Nt_plus_beta_minus_k = Math.log(Nt+beta-k);
				log_evidence_and_z -= Nt_plus_beta_minus_k;
			}
	
			for(int v=0; v<V;v++){
				double beta_nv = beta_n[v];
				int Nvt = (int)(Nvt_plus_beta_n[v][t]-beta_nv);
				
				for(int k=0; k<Nvt; k++){
					double Nvt_plus_beta_nv_minus_k = Math.log(Nvt+beta_nv-k);
					log_evidence_and_z += Nvt_plus_beta_nv_minus_k;
				}
			}
			
		}
		for(int d=0; d<D; d++){
			int Nd = (int)(Nd_plus_alpha[d]-alpha);
			
			for(int k=0; k<Nd; k++){
				double Nd_plus_alpha_minus_k = Math.log(Nd+alpha-k);
				log_evidence_and_z -= Nd_plus_alpha_minus_k;
			}
			
			for(int t=0; t<T; t++){
				double alpha_mt = alpha_m[t];
				int Ntd = (int)(Ntd_plus_alpha_m[d][t]-alpha_mt);
				
				for(int k=0; k<Ntd; k++){
					double Ntd_plus_alpha_mt_minus_k = Math.log(Ntd+alpha_mt-k);
					log_evidence_and_z += Ntd_plus_alpha_mt_minus_k;
				}
			}
		}
		
		return log_evidence_and_z;
	}
	
	public static void main(String[] args) {
		String filename = "/Users/aschein/Documents/workspaceSpring2013/LDA/src/questions.csv";
		String stopwords_filename = "/Users/aschein/Documents/workspaceSpring2013/LDA/src/stopwordlist.txt";
		ArrayList<String> extra_stopwords = new ArrayList<String>();
		extra_stopwords.add("answer");
		extra_stopwords.add("dont");
		extra_stopwords.add("im");
		extra_stopwords.add("find");
		extra_stopwords.add("information");
		extra_stopwords.add("ive");
		extra_stopwords.add("message");
		extra_stopwords.add("question");
		extra_stopwords.add("read");
		extra_stopwords.add("science");
		extra_stopwords.add("wondering");
		Corpus c = Preprocess.preprocess(filename, stopwords_filename, extra_stopwords);
		System.out.println(c.size());
		
		Corpus train_corpus = c.getSlice(0, c.size()-102);
		assert train_corpus.vocab==c.vocab;
		Corpus test_corpus = c.getSlice(c.size()-101,c.size()-1);
		assert test_corpus.vocab==c.vocab;
		
		int V = c.vocab.size();
		int T = 100;
		
		double alpha = 0.1*T;
		double[] m = new double[T];
		for(int t=0; t<T; t++){
			m[t]=1.0/T;
		}
		
		double beta = 0.01*V;
		double[] n = new double[V];
		for(int v=0; v<V; v++){
			n[v]=1.0/V;
		}
		
		LDA lda = new LDA(train_corpus, alpha, beta, m, n);
		lda.gibbs(250);
	}

}
