package corpus;
import java.util.ArrayList;
import java.io.*;
import corpus.Document;

public class Corpus {
	
	ArrayList<Document> docs;
	Vocabulary vocab;
	boolean frozen=false;
	
	public Corpus(){
		this.vocab = new Vocabulary();
		this.docs = new ArrayList<Document>();
	}
	public Corpus(ArrayList<Document> docs, Vocabulary vocab){
		this.docs = docs;
		this.vocab = vocab;
	}
	public Corpus(ArrayList<Document> docs, Vocabulary vocab, boolean frozen){
		this.docs = docs;
		this.vocab = vocab;
		this.frozen = frozen;
	}
	
	public void add(String name, ArrayList<String> tokens){
		if(!frozen){
			int[] w = new int[tokens.size()];
			int idx=0;
			for(String s: tokens){
				vocab.add(s);
				w[idx]=vocab.get(s).intValue();
				idx++;
			}
			docs.add(new Document(this, name,w));	
		}	
	}
	
	public void freeze(){
		int count = 0;
		for (Document doc: docs){
			System.out.println(count);
			count++;
			doc.freeze();
		}
		frozen=true;
	}
	
	public Document get(Integer i){
		return docs.get(i);
	}
	
	public Corpus getSlice(Integer i, Integer j){
		ArrayList<Document> slice = new ArrayList<Document>();
		for(int x = i; x<=j; x++){
			slice.add(docs.get(x));
		}
		return new Corpus(slice, vocab, frozen);
	}
	
	public int size(){return docs.size();}
	
	public void save(String filename){
		try{
			FileOutputStream fileout = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileout);
			out.writeObject(this);
			out.close();
			fileout.close();
		}catch(IOException e)
		{e.printStackTrace();}
	}
	
	public Corpus load(String filename){
		Corpus c = null;
		try{
			FileInputStream filein = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(filein);
			c = (Corpus)in.readObject();
			in.close();
			filein.close();
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException e){e.printStackTrace();}
		return c;
	}
	//TODO add an interator

}
