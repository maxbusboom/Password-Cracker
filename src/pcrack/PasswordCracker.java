/*
 * This class is ... 
 *  
 **/
package pcrack;

import java.io.IOException;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class PasswordCracker {

	ArrayList<String> alphabet; // List of all valid characters in a password
	PDict pdict; // Maps hashes to plaintext

	public PasswordCracker() throws IOException {

		alphabet = constructAlphabet();

		pdict = new PDict(alphabet);
	}

	
	/**
	 * Version of the constructor that allows turning off creating pdcit, for testing. 
	 * 
	 * @param makePDict
	 * @throws IOException
	 */
	public PasswordCracker(boolean makePDict) throws IOException {

		alphabet = constructAlphabet();

		if(makePDict) {
			pdict = new PDict(alphabet);
		}
	}
	
	/**
	 * assembles all possible characters into an array list.
	 * 
	 * @param none
	 * @return alphabet: an array list of all possible characters.
	 */
	public static ArrayList<String> constructAlphabet() {
		// generate alphabet (array list of all possible alphanumeric characters in
		// password including space)
		ArrayList<String> alphabet = new ArrayList<String>();
		char a = 'a', z = 'z', A = 'A', Z = 'Z', zero = '0', nine = '9';
		// append chars a to z, chars A to Z, and chars 0 to 9 to alphabet
		for (char i = a; i <= z; i++) {
			alphabet.add(Character.toString(i));
		}
		for (char i = A; i <= Z; i++) {
			alphabet.add(Character.toString(i));
		}
		for (char i = zero; i <= nine; i++) {
			alphabet.add(Character.toString(i));
		}

		// add space char to alphabet. this is to infill passwords which have 3
		// or less alphanumeric characters, guaranteeing a size of 4 characters for the
		// password.
		alphabet.add(Character.toString(' '));
		return alphabet;
	}

	/**
	 * Try to get the plain text version of a hashed password.
	 * 
	 * @param hashedPassword The hashed version of the plaintext
	 * @return The plaintext associated with the hash
	 */
	String solve(String hashedPassword) throws NoSuchAlgorithmException {

		return pdict.getPlainText(hashedPassword);

	}

	/**
	 * Read input from a scanner.  If the input characters are not in the
	 * alphabet or the user inputs a string of more than 4 characters the method will
	 * prompt again to enter a valid password.
	 * 
	 * @param none
	 * @return hashedPassword: the hashed version of the password the user entered.
	 */
	
	String getInput(Scanner s) throws NoSuchAlgorithmException {
		
		String usersPassword;

		while (true) {
			System.out.println(
					"Enter what you want to hash in alphanumeric characters (including spaces), do not enter more than four characters. Passwords less than four characters will be appended with spaces.");

			usersPassword = s.nextLine();//reads user input using scanner s

			int plen = usersPassword.length(); //This is how long the passcode is.

			// This case-switch statement works because variable usersPassword "cascades" down the conditions.
			switch (plen) {// checks password length
			case 1:
				usersPassword = String.join("", usersPassword, " ");
			case 2:
				usersPassword = String.join("", usersPassword, " ");
			case 3:
				usersPassword = String.join("", usersPassword, " ");
			}

			plen = usersPassword.length(); 

			ArrayList<String> alphabet = constructAlphabet(); //alphabet is the list of all possible characters.
			boolean isCorrectChar = true;

			for (char i : usersPassword.toCharArray()) {
				// See if any characters in usersPassword are not in alphabet.
				if (alphabet.indexOf(Character.toString(i)) == -1) {
					isCorrectChar = false; //Sets the flag isCorrectChar to signify 
					//that there is at least 1 incorrect character in users password.
					break;
				}
			}

			if (isCorrectChar == false) { // if string has illegal characters
				System.out.println("Bad characters");
				continue;
			}

			if (plen != 4) { // if string is not 4 characters
				System.out.println("Wrong size");
				continue;
			}
			
			s.close(); //closes scanner to stop memory leak
			
			return usersPassword;


		}

	}
	
	/**
	 * Try to read user input, if the user inputs characters that are not in the
	 * alphabet or the user inputs a string of more than 4 characters the method will
	 * prompt again to enter a valid password.
	 * 
	 * @param none
	 * @return hashedPassword: the hashed version of the password the user entered.
	 */
	String getUserInput() throws NoSuchAlgorithmException {
		Scanner s = new Scanner(System.in);
		return getInput(s);
	}

	/*
	 * run(): helper method to run methods necessary to the execution of the
	 * program.
	 * 
	 * @param: none
	 * 
	 * @return: none
	 */
	void run() throws NoSuchAlgorithmException {
		String input = getUserInput();
		processInput(input);
	}
	
	
	void processInput(String usersPassword) throws NoSuchAlgorithmException {
		
		
		String hashedPassword = pdict.hashPlainText(usersPassword); //Shows hash to user to demonstrate that hashing algorithm works
		System.out.println("Here's your hash:\n" + hashedPassword);
		
		String solved = solve(usersPassword);
		if (solved == null) {
			System.out.println("Your password was not found.");
		} else {
			System.out.println("Your password is: " + solved);
		}
	}
	

	/**
	 * First a PasswordCracker object is initialized. 
	 * then the set of all possible characters is created in an array list called 
	 * alphabet.
	 * An object pdict of class PDict is initialized.
	 * Then the code attempts to solve the strings in plaintexts.
	 * 
	 * @param plainText: the original unencoded string
	 * @return none
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

		// Test user input
		System.out.println("\n\nTest User Input\n\n");
		PasswordCracker o = new PasswordCracker(false);
		
		String input = o.getInput(new Scanner( // Multiple faked user inputs. 
				"12345\n"+
				"@!$#@$\n"+
				"aaaaa\n"+
				"aaaa\n"));
		
		System.out.println("Input "+input);
		
		// Test some plaintexts
		System.out.println("\n\nTest Fetching Hashes\n\n");
		o = new PasswordCracker();
		ArrayList<String> alphabet = constructAlphabet();


		ArrayList<String> plainTexts = new ArrayList<String>(Arrays.asList("aaaa", "aaba", "aaaz"));

		for (String pt : plainTexts) {
			String hashed = o.pdict.hashPlainText(pt);
			String solved = o.solve(hashed);
			System.out.println("pt: '" + pt + "' -> '" + solved + "'");
			assert pt.equals(solved);
		}

	}

}
