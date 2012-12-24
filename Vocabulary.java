package corpus;
import java.util.HashMap;

public class Vocabulary {
	
	private HashMap<String, Integer> mapping;
	private HashMap<Integer, String> reverse;
	
	private int idx = 0;
	
	public Vocabulary(){
		this.mapping = new HashMap<String, Integer>();
		this.reverse = new HashMap<Integer, String>();
	}
	
	public String lookUp(Integer i){
		assert Integer.class.isInstance(i);
		return reverse.get(i);
	}
	
	public String plainText(){
		return reverse.toString();
	}
	
	public boolean contains(String s){
		assert String.class.isInstance(s);
		//System.out.println(mapping.containsKey(s));
		return mapping.containsKey(s);
	}
	
	public Integer get(String s){
		if(!String.class.isInstance(s)){throw new IllegalArgumentException("Invalid key: must be a string.");}
		return mapping.get(s);
	}
	
	public void add(String s){
		if(!String.class.isInstance(s)){throw new IllegalArgumentException("Invalid key: must be a string.");}
		if(!contains(s)){
			Integer i = idx;
			idx++;
			mapping.put(s,i);
			reverse.put(i,s);
		}
	}
	
	public int size(){return mapping.size();}
	
	//TODO add an iterator

}

