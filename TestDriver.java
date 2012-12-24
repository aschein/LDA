package corpus;
import corpus.*;
import java.util.ArrayList;
import java.util.Collections;


public class TestDriver {

	public static void main(String[] args) {

		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(5));
		list.add(new Integer(3));
		list.add(new Integer(10));
		list.add(new Integer(2));
		Integer i = Collections.max(list);
		System.out.println(i.shortValue());
		
		
		String filename = "/Users/aschein/Documents/workspaceSpring2013/LDA/src/corpus/questions.csv";
		String stopwords_filename = "/Users/aschein/Documents/workspaceSpring2013/LDA/src/corpus/stopwordlist.txt";
		ArrayList<String> extra_stopwords = new ArrayList<String>();
		extra_stopwords.add("falcons");
		extra_stopwords.add("lions");
		extra_stopwords.add("bears");
		Corpus c = Preprocess.preprocess(filename, stopwords_filename, extra_stopwords);
		System.out.println(c.size());

	}

}
