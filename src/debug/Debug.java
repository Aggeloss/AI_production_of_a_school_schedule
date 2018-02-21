package debug;

public class Debug {

	static boolean PRINT_DEBUG_INFO;
	
	static {
		PRINT_DEBUG_INFO = true;
	}
	
	public static void printException(Exception exception) {
		if (PRINT_DEBUG_INFO)
			exception.printStackTrace();
	}
	
	public static void println(Object output) {
		if (PRINT_DEBUG_INFO)
			System.out.println(output.toString());
	}
	
	public static void printArrayListOfStringArrays(java.util.ArrayList<String []> list) {
		for ( String [] stringArray : list )
			printStringArray(stringArray);
	}
	
	public static void printStringArray(String [] array) {
		System.out.println(joinStringArray(array));
	}
	
	public static String joinStringArray(String [] array) {
		String result = "";
		for (String s : array)
			result += s + " ";
		return result.trim();
	}
}
