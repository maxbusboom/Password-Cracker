/*ask user to enter password (4 char, all lowercase alphanumeric)
 * exactly 4
 * program will encrypt password with sha-256 hash (print hash to user to begin)
 * then "forget" what they typed in and then crack the sha hash which means
 * go through all possible combinations, then crack it through brute force.
 * generate each one and then crack it immediately, comparing the hash of the generated password to the one they came up with.
 * do not store intermediate hashes- onece you have used them forget them.
 * 
 * 
 * sha256 encryption function adapted from https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha-256-in-java
 */
package pcrack;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class PasswordCracker {
	
	ArrayList<String> alphabet;
	PDict pdict;
	
	 public PasswordCracker() throws IOException {
		 
		 alphabet = constructAlphabet();
		 
		 pdict = new PDict(alphabet);
	 }
	 
	 
	 
	public static ArrayList<String> constructAlphabet(){
		//generate alphabet (array list of all possible alphanumeric characters in password including space)
		ArrayList<String> alphabet = new ArrayList<String>();
		char a='a', z='z', A='A', Z='Z', zero='0', nine = '9';
		//append chars a to z, chars A to Z, and chars 0 to 9 to alphabet
			for(char i = a; i<=z;i++) {
				alphabet.add(Character.toString(i));
			}
			for(char i = A;i<=Z;i++) {
				alphabet.add(Character.toString(i));
			}
			for(char i = zero;i<=nine;i++) {
				alphabet.add(Character.toString(i));
			}
			
			//add space char to alphabet. this is so we can infill passwords which have 3 or less alphanumeric characters, guaranteeing a size of 4 characters for the password.
			alphabet.add(Character.toString(' '));
		return alphabet;
	}
		

	/**
	* This method solves the password using iteration to compare possible 
	* hashes to the actual password hash.
	
	* @param  hashedPassword The hashed version of the plaintext
	* @return The plaintext associated with the hash
	*/
	String solve( String hashedPassword) throws NoSuchAlgorithmException {
		// next we need to compare the hash of each item in rtable to the hashed password.
	
		return pdict.getPlainText(hashedPassword);
		
	}
	
	
	String getUserInput() throws NoSuchAlgorithmException {
		Scanner s = new Scanner(System.in);
		String usersPassword;
		
		while(true) {
			System.out.println("Enter what you want to hash in alphanumeric characters (including spaces), do not enter more than four characters. Passwords less than four characters will be prepended with spaces.");
			
			usersPassword = s.nextLine();
			
			int plen = usersPassword.length();
			
			
			// This would be better if we just subtracted the len from 4
			// and appended that number of spaces, but doing it this was as a 
			// demonstration of fall-though in a switch/case
			switch(plen) {//checks password length
				case 1:
					usersPassword = String.join("", " ", usersPassword);
				case 2:
					usersPassword = String.join("", " ", usersPassword);
				case 3:
					usersPassword = String.join("", " ", usersPassword);
			}
			
			plen = usersPassword.length();
			
			ArrayList<String> alphabet = constructAlphabet();
			boolean isCorrectChar=true;
			
			for(char i:usersPassword.toCharArray()) {
				//System.out.println(i);
				if(alphabet.indexOf(Character.toString(i))==-1) {
					isCorrectChar = false;
					break;
				} 
			}
			
			if (isCorrectChar == false) { // if string has illegal characters
				System.out.println("Bad characters");
				continue;
			}
			
			if (plen != 4 ) { // if string is not 4 characters
				System.out.println("Wrong size");
				continue;
			}
			

			String hashedPassword = pdict.hashPlainText(usersPassword);
			System.out.println("Here's your hash:\n" + hashedPassword);
			return hashedPassword;
	
		}
		
	}
	
	void run() throws NoSuchAlgorithmException {
		
		String input = getUserInput();
		String solved = solve(input);
		System.out.println("Your password is: "+ solved);
		
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		
		PasswordCracker o = new PasswordCracker();
		o.run();
		
		
	}
	
	
	
	
}
