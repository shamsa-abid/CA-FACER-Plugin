package support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StopWordsRemoval {
public static void main(String args[]) throws IOException
{
	//remove stop words from query
	List<String> stopwords = Files.readAllLines(Paths.get("F:/FACER_2020/DataFiles/stopwords.txt"));
	System.out.println(removeStopWords("get the full file path from uri", stopwords));
	}

public static String removeStopWords(String original, List<String> stopwords) {
	 ArrayList<String> allWords = 
		      Stream.of(original.toLowerCase().split(" "))
		            .collect(Collectors.toCollection(ArrayList<String>::new));
		    allWords.removeAll(stopwords);
		 
		    String result = allWords.stream().collect(Collectors.joining(" "));
	return result;
}
}
