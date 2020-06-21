package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;



/**
 * <b>Parser Class</b> that implements <b>Runnable</b> allowing it to be Thread able
 * <i>Parses Everything in inputed Files into Their Maps</i>
 * @author James Porter G00327095
 * @version 1.0
 *
 */
public class Parser implements Runnable {

	private Database db = null;
	private String file;
	private int k;
	
	/**
	 * Constructor for Parser Class that takes in a Subject Database File
	 * and the NGram type you wish to use between 1 and 5
	 * @param file Subject Database File
	 * @param k NGram to use between 1 and 5
	 */
	public Parser(String file, int k) {
		this.file = file;
		this.k = k;
	}
	
	/**
	 * Sets the Database for the Parser Class
	 * @param db Database
	 */
	public void setDb(Database db) {
		this.db = db;
	}
	
		/**
		 * Reads in the Language Value from file and creates NGrams for the Language in Question
		 * and then adds them to the Database.
		 * @param text takes Subject Database File turned into a String
		 * @param lang Takes in String of Language
		 */
		private void parse(String text, String lang){
			Language language = Language.valueOf(lang);
			
			for (int i = 0; i <= text.length() - k; i++) {
				CharSequence kmer = text.substring(i,i+k);
				db.add(kmer, language);
				
			}
		}
	
		
		
	/**
	 * A Thread Able Runner Method that takes in a File (Subject Database in this Case)
	 * Trims the White Spaces in Each Line and Splits the Language after the AT Symbol
	 * From the Line. Passes the Language and the Lines into the Parse Method to be turned into NGram's
	 */
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			
			while((line = br.readLine()) != null) {
				String[] record = line.trim().split("@");
				if(record.length != 2) continue;
				parse(record[0],record[1]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	 /**
	  * Takes in a Map and Sorts it in <i>Descending</i> order based off Value
	 * @param wordCounts Takes in Map
	 * @return <b>Returns the Map Sorted in Descending Order</b>
	 */
	public static Map<Integer, Integer> sortByValue(final Map<Integer, Integer> wordCounts) {
	        return wordCounts.entrySet()
	                .stream()
	                .sorted((Map.Entry.<Integer, Integer>comparingByValue().reversed()))
	                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	    }
	
	 
	
	/**
	 * Parses the Query File Into A Map which is then Sorted by Value 
	 * and Creates a Map containing NGrams,The NGram Frequencies and Ranks
	 * @param File Query File
	 * @param kmers Type of NGram to use (1,2,3,4,5)
	 * @return the Query Map of NGrams mapped to their Frequency and Rank
	 */
	public Map<Integer, LanguageEntry> queryFileParse(String File, int kmers) {
		int frequency = 0;
		int ngram = 0;
		int r = 1;
		Map<Integer, Integer> m = new HashMap<Integer, Integer>();
		Map<Integer, LanguageEntry> queryMap = new ConcurrentHashMap<>();
		LanguageEntry le = new LanguageEntry(ngram, frequency);
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(File)));
			String line = null;
			String words = null;
			ArrayList<String> wordArray = new ArrayList<String>();
			String query = "";
			
			while((line = br.readLine()) !=  null) {
				words = line.trim().replace("\n", "").replace("\r", "");
				wordArray.add(words);

				}
			
			String wordx = String.join("", wordArray);
			
			for (char c : wordx.toCharArray()) {
				query += c;
				if(query.length() >= 400) {
					break;
				}
				br.close();
			}
			
			for(int i = 0; i <= query.length() - kmers ; i++) {
				CharSequence kmer = query.substring(i,i+kmers);
				
				if(m.containsKey(kmer.hashCode())) {
					m.put(kmer.hashCode(), 
							m.get(kmer.hashCode())+1);
				}else {
					m.put(kmer.hashCode(), 1);
					}
				}
				
			
			m = sortByValue(m);

			int curRank = 1;
			for (Map.Entry<Integer, Integer> pair : m.entrySet()) {
			  LanguageEntry langEntry = new LanguageEntry(pair.getKey(), pair.getValue());
			  langEntry.setRank(curRank);
			 
			  queryMap.put(langEntry.getKmer(), langEntry);
		
			  if(curRank >= 1) {
				  curRank = curRank + 1;
			  }
			 
			}
			//System.out.println(queryMap);
		}
		
		 catch (Exception e) {
			e.printStackTrace();
		}
		return queryMap;
	}

	
}
