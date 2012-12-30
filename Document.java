

public class Document{
	Corpus corpus;
	String name;
	int[] w;
	int[] Nv;
	
	public Document(Corpus corpus, String name, int[] w){
		this.corpus = corpus;
		this.name = name;
		this.w = w;
	}
	public int[] getW(){return w;}
	public int size(){return w.length;}
	public void freeze(){
		Nv = Utils.binCount(w);
		//w = null;
		}
	public String plainText(){
		String s = "";
		for(Integer x : w){
			String word = corpus.vocab.lookUp(x);
			s+=word+" ";
		}
		return s;
	}
}
