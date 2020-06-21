package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;



/**
 * 
 * <b>Database</b> Class which is used for <i>Constructing Language Databases</i> for the
 * Subject Database File.
 * Also <i>Contains the Method to get the Language of the Query</i> File by using
 * Distance Measure Comparison
 * @author James Porter G00327095
 * @version 1.0
 * 
 *
 */
public class Database {

	
	/**
	 * Map for the <b>235</b> Languages in the Wili Dataset parsed into a ConcurrentHashMap
	 * For Multi Threaded Purposes
	 */
	private Map<Language, Map<Integer, LanguageEntry>> db = new ConcurrentHashMap<>();
	
	/**
	 * Adds a Char Sequence converted into <b>HashCode</b> into the Language Database
	 * And <i>Organizes them by Frequency.</i>
	 * Get Handle of Language Map
	 * <b>Create Map if it isnt there or Return it if it is</b>
	 * @param s Character Sequence S
	 * @param lang The Language in Question
	 */
	public void add(CharSequence s, Language lang) {
		
		//Convert String to Hash Code
		int kmer = s.hashCode();
		
		//Get The Handle of the map for the particular language
		Map<Integer, LanguageEntry> langDb = getLanguageEntries(lang);
		
		
		int frequency = 1;
		//If Language Mapping Already has the Kmer, increment its Frequency
		if (langDb.containsKey(kmer)) {
			frequency += langDb.get(kmer).getFrequency();
		}
		//Otherwise Insert into Map as New Language Entry
		langDb.put(kmer, new LanguageEntry(kmer, frequency));
	
		
	}
	
	// Retrieve the Language Maps
	/**
	 * <i>Used in Add to get the Current Language Entries</i>
	 * @param lang Language in Question
	 * @return Return all Language Entries
	 */
	private Map<Integer, LanguageEntry> getLanguageEntries(Language lang) {
		Map<Integer,LanguageEntry> langDb = null;
		if(getDb().containsKey(lang)) {
			//If Database Contains the Language Return it
			langDb = getDb().get(lang);
		}else {
			//Create a New Language to Database and Return it
			langDb = new ConcurrentHashMap<Integer, LanguageEntry>();
			getDb().put(lang, langDb);
		}
		return langDb;
	}
	
	//Cut down the map to the top 300<Max> entries
	/** Cuts down a map to the <b>Size</b> entered as <b>Max</b>
	 * @param max Sets The <b>Max Amount of NGrams</b> You want in the Map
	 */
	public void resize(int max) {
		Set<Language> keys = getDb().keySet();
		System.out.println(keys);
		for(Language lang : keys) {
			Map<Integer,LanguageEntry> top = getTop(max,lang);
			getDb().put(lang,top);
		}
	}
	
	
	/**
	 * Creates a Concurrent HashMap and <b>Sorts the Collection</b> In <i>Descending Order</i> based off Frequency
	 * Also <b>Adds a Rank to Each</b>
	 * @param max Amount of <b>NGrams</b> you want in a map
	 * @param lang Language in Question
	 * @return the <b>New Map Known as Temp</b>
	 */
	public Map<Integer, LanguageEntry> getTop(int max, Language lang) {
		Map<Integer, LanguageEntry> temp = new ConcurrentHashMap<>();
		List<LanguageEntry> les = new ArrayList<>(getDb().get(lang).values());
		
		Collections.sort(les);
		
		int rank = 1;
		for (LanguageEntry le : les) {
			le.setRank(rank);
			temp.put(le.getKmer(), le);			
			if (rank == max) break;
			rank++;
		}
		// Return
		return temp;
	}
	
	/**
	 * <b>Takes in the Map You Generated from the Query File Inputed in Menu
	 * And Discovers It's Language for You</b>
	 * @param query Query Map of NGrams relating to their Ranks and Frequencies
	 * @return Returns the Language the Program Believes It is based off the Distance Measurement
	 */
	public Language getLanguage(Map<Integer, LanguageEntry> query) {
		TreeSet<OutOfPlaceMetric> oopm = new TreeSet<>();
		
		Set<Language> langs = getDb().keySet();
		for (Language lang : langs) {
			oopm.add(new OutOfPlaceMetric(lang, getOutOfPlaceDistance(query, getDb().get(lang))));
		}
		return oopm.first().getLanguage();
	}
	
	/**
	 * <i>Compares A Query Map of NGrams,Frequencies,Ranks</i> to the <b>Subject Database</b>
	 * To Determine a <b>Distance Measure</b> which can be <i>used to Determine the Query Text File's Language</i>
	 * @param query Query Database Map
	 * @param subject Subject Database Map
	 * @return The Distance Measure between the The Query and Subject Map
	 */
	private int getOutOfPlaceDistance(Map<Integer, LanguageEntry> query, Map<Integer, LanguageEntry> subject) {
		int distance = 0;
		
		Set<LanguageEntry> les = new TreeSet<>(query.values());		
		for (LanguageEntry q : les) {
			LanguageEntry s = subject.get(q.getKmer());
			if (s == null) {
				distance += subject.size() + 1;
			}else {
				distance += s.getRank() - q.getRank();
			}
		}
		return distance;
	}
	
	/**
	 * <b>Out of Place Metric Class Used to Compare Absolute Distances</b> of Query Maps
	 * Uses Comparable to do this.
	 * @author James Porter G00327095
	 * @version 1.0
	 *
	 */
	private class OutOfPlaceMetric implements Comparable<OutOfPlaceMetric>{
		private Language lang;
		private int distance;
		
		/**
		 * Constructor for Out of Place Metric Class
		 * @param lang Language in Question
		 * @param distance Distance between Languages
		 */
		public OutOfPlaceMetric(Language lang, int distance) {
			super();
			this.lang = lang;
			this.distance = distance;
		}
		
		
		/**
		 * Gets the Current Language Applied to OutOfPlaceMetric Class Object
		 * @return Returns the Language
		 */
		public Language getLanguage() {
			return lang;
		}
		
		
		/**
		 * Takes in a Integer of Distance and if it's a negative
		 * Changes it into a Positive using Math.abs
		 * @return Absolute Distance
		 */
		public int getAbsoluteDistance() {
			return Math.abs(distance);
		}

		/**
		 * Compares The Class Instance Absolute Distance to Objects Absolute Distance
		 */
		@Override
		public int compareTo(OutOfPlaceMetric o) {
			return Integer.compare(this.getAbsoluteDistance(), o.getAbsoluteDistance());
		}
		
		/**
		 * Overridden To String Method that Returns the Language and it's absolute distance
		 */
		@Override
		public String toString() {
			return "[lang=" + lang + ", distance=" + getAbsoluteDistance() + "]";
		}
	}
		
		/**
		 * <b>Returns the Subject Database's Language,
		 * The NGrams,The NGram Frequencies and Rank of the NGrams</b>
		 */
		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			int langCount = 0;
			int kmerCount = 0;
			Set<Language> keys = getDb().keySet();
			for (Language lang : keys) {
				langCount++;
				sb.append(lang.name() + "->\n");
				 
				 Collection<LanguageEntry> m = new TreeSet<>(getDb().get(lang).values());
				 kmerCount += m.size();
				 for (LanguageEntry le : m) {
					 sb.append("\t" + le + "\n");
				 }
			}
			sb.append(kmerCount + " total k-mers in " + langCount + " languages"); 
			return sb.toString();
			}

		/**
		 * Gets the Database Object Instance
		 * @return The Database Object Instance
		 */
		public Map<Language, Map<Integer, LanguageEntry>> getDb() {
			return db;
		}

		/**
		 * Sets the Database based of the Entered Database
		 * @param db Takes in a Database
		 */
		public void setDb(Map<Language, Map<Integer, LanguageEntry>> db) {
			this.db = db;
		}

	
	}
