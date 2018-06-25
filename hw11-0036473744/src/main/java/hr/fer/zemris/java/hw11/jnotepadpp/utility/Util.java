package hr.fer.zemris.java.hw11.jnotepadpp.utility;

/**
 * Class Utility for calculating reoccuring actions in the application.
 * @author vladimir
 *
 */
public class Util {

	/**
	 * Method for getting number of characters
	 * @param content content to be evaluated
	 * @return number of chars
	 */
	public static long getNumberOfCharacters(String content){
		
		if(content == null || content.isEmpty()) return 0;
				
		return content.length();
				
	}
	
	/**
	 * Method for inverting case on current text.
	 * @param text being evalutaed
	 * @return returns new evaluated text
	 */
	public static String invertText(String text) {

		StringBuilder sb = new StringBuilder(text.length());

		for (char c : text.toCharArray()) {
			if (Character.isUpperCase(c)) {
				sb.append(Character.toLowerCase(c));
			} else if (Character.isLowerCase(c)) {
				sb.append(Character.toUpperCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/**
	 * Get number of non blank characters.
	 * @param content
	 * @return
	 */
	public static long getNumberOfNonBlankCharacters(String content){
		
		if(content == null || content.isEmpty()) return 0;
		
		return content.chars().filter(e->{
			if(e == '\t' || e == '\n' || e == ' '){
				return false;
			}
			return true;
		}).count();
	}
	
	/**
	 * Method for getting number of lines in current text.
	 * @param content content being evaluated
	 * @return returns current number of lines in text.
	 */
	public static long getNumberOfLines(String content){
		
		if(content == null || content.isEmpty()) return 0;
		
		return content.split("\n").length;
	}
}
