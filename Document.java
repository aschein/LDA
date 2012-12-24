package corpus;
import java.util.ArrayList;

public class Document{
	Corpus corpus;
	String name;
	ArrayList<Integer> w;
	ArrayList<Integer> Nv;
	
	public Document(Corpus corpus, String name, ArrayList<Integer> w){
		this.corpus = corpus;
		this.name = name;
		this.w = w;
	}
	
	public int size(){return w.size();}
	public void freeze(){Nv = Utils.binCount(w);}
	public String plainText(){
		String s = "";
		for(Integer x : w){
			String word = corpus.vocab.lookUp(x);
			s+=word+" ";
		}
		return s;
	}
}
