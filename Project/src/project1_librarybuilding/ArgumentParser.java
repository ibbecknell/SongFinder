package project1_librarybuilding;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

	/** Stores arguments in a map, where the key is a flag. */
	private final Map<String, String> argumentMap;

	/**
	 * Initializes an empty argument map. The method
	 * {@link #parseArguments(String[])} should eventually be called to populate
	 * the map.
	 */
	public ArgumentParser() {
		argumentMap = new HashMap<>();
	}

	/**
	 * Initializes the argument map with the provided command-line arguments.
	 * Uses {@link #parseArguments(String[])} to populate the map.
	 *
	 * @param args
	 *            command-line arguments
	 * @see #parseArguments(String[])
	 */
	public ArgumentParser(String[] args) {
		this();
		parseArguments(args);
	}

	/**
	 * Iterates through the array of command-line arguments. If a flag is found,
	 * will attempt to see if it is followed by a value. If so, the flag/value
	 * pair is added to the map. If the flag is followed by another flag, then
	 * the first flag is added to the map with a null value.
	 *
	 * @param args
	 *            command-line arguments
	 *
	 * @see #isFlag(String)
	 * @see #isValue(String)
	 */
	public void parseArguments(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (isFlag(args[i])) {
				if (i < args.length - 1 && isValue(args[i + 1])) {
					argumentMap.put(args[i], args[i + 1]);
				} else {
					argumentMap.put(args[i], null);
				}
			}
		}
	}

	/**
	 * Tests if the provided argument is a flag by checking that it starts with
	 * a "-" dash symbol, and is followed by at least one non-whitespace
	 * character. For example, "-a" and "-1" are valid flags, but "-" and "- "
	 * are not valid flags.
	 *
	 * @param arg
	 *            command-line argument
	 * @return true if the argument is a flag
	 */
	public static boolean isFlag(String arg) {
		return arg.startsWith("-") && arg.trim().length() > 1;
	}

	/**
	 * Tests if the provided argument is a value by checking that it does not
	 * start with a "-" dash symbol, and contains at least one non-whitespace
	 * character. For example, "a" and "1" are valid values, but "-" and " " are
	 * not valid values.
	 *
	 * @param arg
	 *            command-line argument
	 * @return true if the argument is a value
	 */
	public static boolean isValue(String arg) {
		return !arg.startsWith("-") && arg.trim().length() >= 1;
	}

	/**
	 * Returns the number of flags stored.
	 *
	 * @return number of flags
	 */
	public int numFlags() {
		return argumentMap.size();
	}

	/**
	 * Tests if the provided flag is stored in the map.
	 *
	 * @param flag
	 *            flag to check
	 * @return value if flag exists and has a value, or null if the flag does
	 *         not exist or does not have a value
	 */
	public boolean hasFlag(String flag) {
		return argumentMap.containsKey(flag);
	}

	/**
	 * Tests if the provided flag has a non-empty value.
	 *
	 * @param flag
	 *            flag to check
	 * @return t rue if the flag exists and has a non-null non-empty value
	 */
	public boolean hasValue(String flag) {
		return argumentMap.get(flag) != null;
	}

	/**
	 * Returns the value of a flag if it exists, and null otherwise.
	 *
	 * @param flag
	 *            flag to check
	 * @return value of flag or null if flag does not exist or has no value
	 */
	public String getValue(String flag) {
		return argumentMap.get(flag);
	}

	/**
	 * Checks if the user has valid input, output, and order values/paths
	 * @param input path to check
	 * @param output path to check
	 * @param order flag to check
	 * @return true if all three values are used
	 */
	public boolean hasValidValues(String input, String output, String order){
		return argumentMap.get(input) != null && argumentMap.get(output) != null && argumentMap.get(order) != null;
	}
	
	/**
	 * Checks if the user has valid input, output, and order flags
	 * @param input flag to check
	 * @param output flag to check
	 * @param order flag to check
	 * @return true if all three flags are used
	 */
	public boolean hasValidFlags(String input, String output, String order){
		return argumentMap.containsKey(input) && argumentMap.containsKey(output) && argumentMap.containsKey(order);

	}
	
	public boolean hasValidSearchInput(String input){
		Path p = Paths.get(input);
		return p.toFile().exists();
	}
	
	public boolean hasThreadFlag(String thread){
		return argumentMap.containsKey(thread);
	}
	
	/**
	 * Checks if the user has valid order
	 * @param artist order to check
	 * @param title order to check
	 * @param tag order to check
	 * @return true if either of the three orders are given
	 */
	public boolean hasValidOrder(String order, String artist, String title, String tag){
		return order.compareTo(artist) == 0 || order.compareTo(title) == 0
				|| order.compareTo(tag) == 0;
	}
	public boolean isValidThread(String flag){
		String strings = "[A-Za-z]+";
		return flag.contains(strings);
	}
	public boolean hasValidThreadCount(String flag){
		return isValidThread(flag) && hasValue(flag) && Integer.parseInt(getValue(flag))<=1000 && Integer.parseInt(getValue(flag))>=1;
	}
	
//	public boolean hasValidSearchInput(){
//		
//	}
	
	/**
	 * Converts the flag and value pairs to a string
	 */
	@Override
	public String toString() {
		return argumentMap.toString();
	}

}

