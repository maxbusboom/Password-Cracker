
/**
 *putting password candidates in dictionary, move method generateCandidates over here. 
 *use a map , the key is the hashed candidate, the value is the plaintext.
 *go to crackstation https://crackstation.net/files/crackstation-human-only.txt.gz
 *
 * Best thing to do would be to download the file if it doesn't exist.
 * start to load into a map in memory, but 
 * https://stackoverflow.com/questions/9772058/which-embedded-db-written-in-java-for-a-simple-key-value-store
 * 
 */
package pcrack;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.nio.file.*;
import java.io.*;

/**
 * @author max
 *
 */
public class PDict {
	
	ArrayList<String> alphabet;
	HashMap<String, String> dict = new HashMap<String,String>();
	ArrayList<String> candidates;
	
	
	static String defaultHashFunction = "SHA-256";
	String hashFunction;
	
	File cacheFile = new File("candidates.txt");
	
	boolean testing = true;
	int test_n = 1000;
	
	
	public PDict(ArrayList<String> a ) throws IOException{
		alphabet = a;
		hashFunction = defaultHashFunction;
		
		loadCandidates();
			
	}
	
	public PDict(ArrayList<String> a, String hashFunction ) throws IOException{
		this(a);
		
		this.hashFunction = hashFunction;
		
	}
	
	public String hashPlainText(String s)  {
		try {
			MessageDigest digest = MessageDigest.getInstance(hashFunction);
			byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
			String encoded = Base64.getEncoder().encodeToString(hash);
			return encoded;
		} catch(NoSuchAlgorithmException e) {
			return null;
		}
	}

	ArrayList<String> genCanidates(ArrayList<String> alphabet){
		int n = 0;
		ArrayList<String> rtable = new ArrayList<String>();
		//These nested for loops generate every possible four character string that can be made with the characters in alphabet.
		for(String i: alphabet) {
			for(String j:alphabet) {
				for(String k: alphabet) {
					for(String l: alphabet) {
						
						if ( testing & n++ > test_n) {
							return rtable;
						}
						
						rtable.add(String.join("", i,j,k,l));
					
					}
				}
			}
		}
		return rtable;
	}
	
	void add(String plainText) {
		
		dict.put(hashPlainText(plainText), plainText);
		
	}
	
	
	// lookup a hashed password and return the plaintext 
	String getPlainText(String hashedP) {
		
		String s = dict.get(hashedP);
		return s;
		
	}

	
	void loadCandidates() throws IOException {
	
		
		if ( !cacheFile.exists()) {
			
			System.out.println("Generate candidates, this will be slow");
			candidates = genCanidates(alphabet);
			
			int n = 0;
			for(String i: candidates) {
				if ( testing & n++ > test_n) {
					break;
				}
				add(i);
				
			}
			
			// Iterate through all candidates and assign the planintext and
			// hash to the maps. key<-hash, value<-planintext
			
			cacheDict();
		} else {
			
			readCache();
			
		}
	}
	
	// Write file via properties, via https://stackoverflow.com/a/12748101
	void cacheDict() throws IOException {
		
		Properties properties = new Properties();
		
		System.out.println("Writing cache");
	
		for (Map.Entry<String,String> entry : dict.entrySet() ) {
			properties.put(entry.getKey(), entry.getValue());
		}
		
		properties.store(new FileOutputStream(cacheFile), null);
		
	
	}
	
	void readCache() throws FileNotFoundException, IOException {
		
		
		System.out.println("Reading cache, this will be slow");
		
		Properties properties = new Properties();
		
		properties.load(new FileInputStream(cacheFile));

		for (String key : properties.stringPropertyNames()) {
		   dict.put(key, properties.get(key).toString());
		}
		
	}

	
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
