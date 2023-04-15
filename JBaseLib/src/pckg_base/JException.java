package pckg_base;

import java.io.PrintWriter;
import java.io.StringWriter;





public class JException extends Exception {

	private String file = "";
	private String trace = "";
	
	
	public JException(Exception e, String file) {
		// TODO Auto-generated catch block
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		e.printStackTrace(pw);
		this.trace = pw.toString();
		this.file = file;
		return;
	}

	
	public String getInfo() {
		return this.file + this.trace;
	}
}
