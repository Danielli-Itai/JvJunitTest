package pckg_base;





public class JText {

	public static final String EQUAL = "Equal";
	public static final String UN_EQUAL= "Not Equal";
	public static String TextStr(String[] text_lines, String Separator) {
		String text_str = "";
		if(null != text_lines) {
			for(String line: text_lines) {
				text_str+= line;
				if(null != Separator) {
					text_str += Separator;
				}
			}
		}
		return text_str;
	}
	public static String TextStr(String[] text_lines) {
		return TextStr(text_lines, null); 
	}


	public static String getSimpleText(String text) {
		return text.trim().replaceAll("[^a-zA-Z0-9]", " ").replaceAll("\\s{2,}", " ").toLowerCase();
	}
	public static String Compare(String source, String compare)
	{
		source = getSimpleText(source);
		compare = getSimpleText(compare);

		String diff = EQUAL;		
		if(!source.equals(compare)) {
			diff = UN_EQUAL;
		}
		return diff;
	}
}
