
import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.regex.*;

public class Preprocess {
	
	public static HashSet<String> createStopwordList(ArrayList<String> stopwords){
		HashSet<String> stopwordSet = new HashSet<String>();
		for(String s: stopwords){
			stopwordSet.add(s.trim());
		}
		return stopwordSet;
	}
	
	public static HashSet<String> createStopwordList(String filename){
		if(!String.class.isInstance(filename)){throw new IllegalArgumentException("Filename must be a string.");} 
		HashSet<String> stopwordSet = new HashSet<String>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line =null;
			while((line=reader.readLine()) != null){
				String[] tokens = line.split("\\s");
				for(String t : tokens){
					stopwordSet.add(t.trim());
				}
			}
		}catch(IOException e){e.printStackTrace();}
		return stopwordSet;
	}
	public static ArrayList<String> tokenize(String filename, String stopword_filename){
		if(!String.class.isInstance(filename)){throw new IllegalArgumentException("Filename must be a string.");}
		if(!String.class.isInstance(stopword_filename)){throw new IllegalArgumentException("Filename must be a string.");}
		HashSet<String> stopwordSet = createStopwordList(stopword_filename);
		ArrayList<String> text = new ArrayList<String>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while((line=reader.readLine()) != null){
				ArrayList<ArrayList<Character>> wordsInLine = new ArrayList<ArrayList<Character>>();
				int idx=0;
				boolean lastWasLetter = false;
				for(int i=0; i<line.length(); i++){
					if(Character.isLetter(line.charAt(i))){
						wordsInLine.get(idx).add(new Character(Character.toLowerCase(line.charAt(i))));
						i++;
						lastWasLetter = true;
						}
					else{
						if(lastWasLetter){
							wordsInLine.add(new ArrayList<Character>());
							idx++;
							lastWasLetter = false;	
						}
					}
					
				}
				for(ArrayList<Character> word : wordsInLine){
					StringBuilder builder = new StringBuilder(word.size());
					for(Character ch: word){builder.append(ch);}
					if(!stopwordSet.contains(builder.toString())){text.add(builder.toString());}
				}
				
			}
				
		}catch(IOException e){e.printStackTrace();}
		
		return text;
	}

	public static ArrayList<String> tokenize(String text, HashSet<String> stopwordSet){
		ArrayList<String> tokenized_text = new ArrayList<String>();
		ArrayList<ArrayList<Character>> words = new ArrayList<ArrayList<Character>>();
		boolean lastWasLetter = false;
		int idx=0;
		for(int i=0; i<text.length(); i++){
			if(Character.isLetter(text.charAt(i))){
				if(!lastWasLetter){words.add(new ArrayList<Character>());}
				words.get(idx).add(new Character(Character.toLowerCase(text.charAt(i))));
				lastWasLetter = true;
				}
			else{
				if(lastWasLetter){
					idx++;
					lastWasLetter = false;
					}
				}	
			}
		for(ArrayList<Character> word : words){
			StringBuilder builder = new StringBuilder(word.size());
			for(Character ch: word){builder.append(ch);}
				if(!stopwordSet.contains(builder.toString())){
					tokenized_text.add(builder.toString());
				
				}		
		}
		return tokenized_text;
	}

	/** preprocess takes in a corpus and stopwords, tokenizes both, and returns a Corpus object.
	 * 
	 * @param filename (String)
	 * @param stopword_filename (String)
	 * @param extra_stopwords (ArrayList<String)
	 * @return Corpus object
	 */
	
	public static Corpus preprocess(String filename, String stopword_filename, ArrayList<String> extra_stopwords){
		if(!String.class.isInstance(filename)){throw new IllegalArgumentException("Filename must be a string.");}
		
		HashSet<String> stopwordSet = createStopwordList(stopword_filename);
		stopwordSet.addAll(extra_stopwords);
		Corpus c = new Corpus();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while((line=reader.readLine())!=null){
				String[] corpusEntry = line.split("\t");
				String name = corpusEntry[0];
				ArrayList<String> text = tokenize(corpusEntry[corpusEntry.length-1], stopwordSet);
				c.add(name, text);
			}
			//c.freeze();
		}catch(IOException e){e.printStackTrace();}
		return c;
	}
	
}
