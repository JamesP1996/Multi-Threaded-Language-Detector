package ie.gmit.sw;


/**
 * <b>Language Entry Class</b> that takes in the <i>Ngrams, The Frequency of the NGrams
 * and their Rank in the Map.</i> Uses <b>Comparable</b> to compare ngrams and frequencies to determine
 * Rank.
 * @author James Porter G00327095
 * @version 1.0
 * 
 *
 */

public class LanguageEntry implements Comparable<LanguageEntry> {
	
	
	/**
	 * The Variables for The LanguageEntry Object Class
	 */
	private int kmer;
	private int frequency;
	private int rank;
	
	
	/**
	 * Constructor called to create a Language Entry
	 * @param kmer Is the NGram
	 * @param frequency is the Frequency of occurence of that Ngram
	 */
	public LanguageEntry(int kmer, int frequency) {
		super();
		this.kmer = kmer;
		this.frequency = frequency;
	}
	
	
	/**
	 * Returns the NGram in the Language Entry Object
	 * @return The NGram
	 */
	public int getKmer() {
		return kmer;
	}
	
	
	/**
	 * Sets the NGram in the Language Entry Object
	 * @param kmer Sets The NGram
	 */
	public void setKmer(int kmer) {
		this.kmer = kmer;
	}
	
	
	/**
	 * Returns the Frequency of the NGram in the Language Entry
	 * @return The Frequency of the NGram
	 */
	public int getFrequency() {
		return frequency;
	}
	
	
	/**
	 * Sets the Frequency of the Ngram in the Language Entry
	 * @param frequency Sets the Frequency
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	
	/**
	 * Gets Rank of the NGram in the Language Entry
	 * @return The Rank
	 */
	public int getRank() {
		return rank;
	}
	
	/**
	 * Sets The Rank of the NGram in the Language Entry
	 * @param rank Sets Rank
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	
	/**
	 * <b>Compares One Language Entry to Another</b>
	 */
	@Override
	public int compareTo(LanguageEntry next) {
		return - Integer.compare(frequency, next.getFrequency());
	}
	
	/**
	 * Overriden To String Method that prints the NGram,Frequency and Rank
	 */
	@Override
	public String toString() {
		return "[" + kmer + "/" + frequency + "/" + rank + "]";
	}
}
