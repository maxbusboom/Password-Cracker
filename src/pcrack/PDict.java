
/**
 
 */
package pcrack;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;

/**
 * @author max
 *
 */
public class PDict {

	ArrayList<String> alphabet; // THe list of all possible characters
	HashMap<String, String> dict = new HashMap<String, String>(); //The hashmap that will be inserted into the file
	ArrayList<String> candidates; // All the possible solutions that will be put in the hashmap.

	static String defaultHashFunction = "SHA-256"; //This specifies which hash function we will use. we use it 
	String hashFunction; 

	File cacheFile = new File("candidates.txt"); //creates a new file with the name candidates.txt

	boolean testing = false;// a flag that specifies if the program is being tested or not
	int test_n = 1000;// the amount of entries in the hashmap during testing

	/**
	 * Constructor that uses the default has function. 
	 */
	public PDict(ArrayList<String> a) throws IOException {
		this(a, defaultHashFunction);
	}

	/**
	 * Primary constructor
	 * @param a
	 * @param hashFunction
	 * @throws IOException
	 */
	public PDict(ArrayList<String> a, String hashFunction) throws IOException {
		
		alphabet = a;
	
		this.hashFunction = hashFunction;
		
		loadCandidates();


	}

	/**
	 * hashPlainText hashes string param to whatever hashing algorithm is defined by
	 * the instance variable defaultHashFunction
	 * 
	 * @param s: The plaintext
	 * @return encoded: The hashed version of s.
	 */
	public String hashPlainText(String s) {
		try {
			MessageDigest digest = MessageDigest.getInstance(hashFunction); //implements hashing algorithm defined by global variable hashFunction.
			byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));//turns digest into a series of bytes
			String encoded = Base64.getEncoder().encodeToString(hash);//encodes this byte array into a string.
			return encoded;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * generates an array list of all possible candidates for a four character
	 * password given the current set of characters.
	 * 
	 * @param alphabet: the current set of characters
	 * @return rtable: the list of possible candidates.
	 */
	ArrayList<String> genCanidates(ArrayList<String> alphabet) {
		int n = 0;//number of iterations that have happened
		ArrayList<String> rtable = new ArrayList<String>();//list of possible candidates
		// These nested for loops generate every possible four character string that can
		// be made with every possible character in array list alphabet.
		for (String i : alphabet) {
			for (String j : alphabet) {
				for (String k : alphabet) {
					for (String l : alphabet) {
							
						if (testing & n++ > test_n) {
							//if the testing flag is on and n increment by one is greater than the number of test values.
							return rtable;//return the list of possible candidates.
						}

						rtable.add(String.join("", i, j, k, l));//add a string made up of i, j,k, and l to rtable

					}
				}
			}
		}
		return rtable;//return the list of possible candidates.
	}

	/**
	 * puts the hashed version of plainText and plainText in the hashmap dict.
	 * 
	 * @param plainText: the original unencoded string
	 * @return none
	 */
	void add(String plainText) {

		dict.put(hashPlainText(plainText), plainText);

	}

	/** lookup a hashed password and return the plaintext
	 * 
	 * @param hashedP
	 * @return
	 */
	String getPlainText(String hashedP) {

		String s = dict.get(hashedP);//uses the key to search for the value in the key-value pair
		return s;

	}

	/**
	 * Either gets candidates from gencandidates and saves them, or reads them from the file.
	 * 
	 * @param none
	 * @return none
	 */
	void loadCandidates() throws IOException {

		if (!cacheFile.exists()) {//if the file does not exist

			System.out.println("Generate candidates, this will be slow");
			candidates = genCanidates(alphabet); //make list of all possible passcodes

			int n = 0;
			for (String i : candidates) {
				if (testing & n++ > test_n) {
					break;//stop iterating if n incremented by 1 is greater than the number of tests (only if testing flag on.)
				}
				add(i); //put i in the hashmap dict.

			}

			// Iterate through all candidates and assign the planintext and
			// hash to the maps. key<-hash, value<-planintext

			cacheDict();
		} else {

			readCache();

		}
	}

	
	/**
	 * Write file via properties, via https://stackoverflow.com/a/12748101
	 * 
	 * @param none
	 * @return none
	 */ 
	void cacheDict() throws IOException {

		Properties properties = new Properties(); //create object of properties class

		System.out.println("Writing cache, also slow, ");

		for (Map.Entry<String, String> entry : dict.entrySet()) {
			properties.put(entry.getKey(), entry.getValue()); //puts all key-value pairs from hashmap called dict into properties.
		}

		properties.store(new FileOutputStream(cacheFile), null); //puts every item in properties into text file

	}

	/**
	 * puts the hashed version of plainText and plainText in the hashmap dict.
	 * 
	 * @param plainText: the original unencoded string
	 * @return none
	 */
	void readCache() throws FileNotFoundException, IOException {

		System.out.println("Reading cache, this will be slow");

		Properties properties = new Properties();//creates new instance of properties

		properties.load(new FileInputStream(cacheFile)); //loads key-value pairs from cachefile using properties

		for (String key : properties.stringPropertyNames()) {
			dict.put(key, properties.get(key).toString()); //for every key-value pair add it to hashmap dict. 
		}

	}

	/**
	 * First, runs constructor with alphabet array list. A new object of class pdict is created.
	 *  Then the constructor of the object calls loadCandidates.
	 *  Pdict is then constructed and can be used.
	 *  
	 * 
	 * @param none
	 * @return none
	 */
	public static void main(String[] args) throws Exception {

		System.out.println("Starting pdic construct ");
		PDict o = new PDict(PasswordCracker.constructAlphabet());

		System.out.println("done with pdict construction ");

		// test
		String abcd_hash = o.hashPlainText("aaaa");
		System.out.println(o.getPlainText(abcd_hash));
		assert o.getPlainText(abcd_hash) == "aaaa";
	}
}
