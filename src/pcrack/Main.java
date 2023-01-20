/**
 *  This is the main entry pint for the program; running ha JAR file runs the main()
 *  from this class. 
 */
package pcrack;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author max
 *
 */
public class Main {

	/**
	 * @param args
	 */
	
	/**
	 * Main entry point or the program. 
	 * 
	 * @param none
	 * @return none
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		
		PasswordCracker o = new PasswordCracker();
		o.run();
		
		
	}

}
