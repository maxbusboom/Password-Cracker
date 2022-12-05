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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Passwordcracker {
	public static String tosha256(String s) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
		String encoded = Base64.getEncoder().encodeToString(hash);
		return encoded;
	}
	public static void main(String[] args) throws NoSuchAlgorithmException {
		Scanner s = new Scanner(System.in);
		System.out.println("enter what you want to hash");
		
		String a = s.nextLine();
		String x = tosha256(a);
		System.out.println("here's your hash:\n" + x);
	}
}
