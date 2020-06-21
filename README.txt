This is the ReadMe for my Multithreaded Language Detector using NGrams

There is 5 Classes that give the functionality of the program.

1. Database
2. Language
3. Language Entry
4. Parser
5. Runner

The Language Class holds the Enums for every Language that is expected to be inputted into the program.
It contains Get and Set methods for the language and a CharSequence Variable known as language.

The Language Entry Class is the class that will be used as a object to hold each Ngram,Ngram Frequency and Ngram Rank
along with getters and setters for each and a method to compare Language Entry's and a Overridden to String Method which
prints the NGram,Frequency and Rank.

The Database Class is the class which is used for creating the Subject Database which can be done using a Wili Data File.
It maps each Language Entry to their Language and resizes the map to keep the top 300 ranked Language Entry's in a Language.
It also holds the key functionality to this program which is to get the Language of a Query File. It does this by using
a getOutOfPlaceDistance(Metric) and compares these distances between the Language's in the Database to the Query File
To determine the Query's Text's Language.

Parser is a Multi-Threaded Class That implements Runnable Interface. It reads in the Subject Database Query File breaks it into kmers
and uses functionality in the Database,Language Entry and Language Classes to create a Database of all the Languages
and their Language Entries (Ngrams,Frequencies,Ranks). It also contains the ability to parse a query file into
a sorted map of kmers and then creates a Language Entry for Each so that it can be passed into the Databases getLanguage() method.

Runner is a class that works like the Main Menu for The user. It asks the user to enter the N-Gram Break up. So basically
if they want 1Grams up to 5Grams seperations. And Then asks for the Subject Database File known as Wili and the query file.
Then Finally Compares them and Prints the assumed Language of the Query File. It is not 100% accurate and it
seems to have issues when using larger wili files but with the small file it is fairly accurate and quick.


That is the layout of the program and what each class does. Thank you for reading this ReadMe and using my program!

