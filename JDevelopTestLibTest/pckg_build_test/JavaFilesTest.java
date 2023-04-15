package pckg_build_test;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import pckg_java_build.JavaFiles;





public class JavaFilesTest {
	private static final String SOURCE_DIRS = "D:\\_SourceDev5.Java\\JvOOP\\Ex01.HelloWorld\\src\\hello_world";
	
	@Test
	void testTrace() {
		String[] java_files = null;
		try {
			java_files = JavaFiles.getFiles(SOURCE_DIRS, JavaFiles.EXT_JAVA);
			JavaFiles.Print(java_files);

			java_files = JavaFiles.getFilesFind(SOURCE_DIRS, JavaFiles.EXT_JAVA);
			JavaFiles.Print(java_files);

			
		}catch (Exception e) {  
			// TODO: handle exception
		}
		return;
	}
}
