package pckg_base;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;




public class JFiles {
	
	public static String getName(String full_name) 
	{
		String file_name = new File(full_name).getName();
		
		int name_end = file_name.lastIndexOf(".");
		if(name_end<0) name_end= file_name.length();
		
		file_name = file_name.substring(0, name_end );
		return file_name;
	}

	public static String getNameNoExt(String full_name) {
		String file_name = full_name.substring(0, full_name.lastIndexOf("."));
		return file_name;
	}

	public static String setNameExt( String file_name, String ext) {
		String new_name = file_name.substring(0, file_name.lastIndexOf(".") + 1) + ext;
		return new_name;
	}
	
	
	public static String getRelativePath(String folder_path, String file_path) {
		String relative_path = "." + file_path.substring(folder_path.length(),file_path.length());
		return relative_path;
	}


	public static boolean fileExists(String file_path) {
        File my_file = new File(file_path);
        return my_file.exists();		
	}

	public static String getFilePath(String folder, String name) {
		String path = "";
		try { path = new File(folder,name).toString();}
		catch(Exception e) {}
		
		return path;
	}
	
	public static boolean filetypeExistsRec(String file_path, String ext){
		final List<File> foundFiles = new ArrayList<>();

		try (Stream<Path> walkStream = Files.walk(new File(file_path).toPath())) {
		  walkStream.filter(p->p.toFile().isFile())
		      .forEach(file -> {
		        if (file.toString().endsWith(ext)) {
		          foundFiles.add(file.toFile());
		        }
		      });
		}catch(Exception e) {}
        return foundFiles.size()>0;		
	}

	public static String [] TextRead(String path, String file_path) throws FileNotFoundException {
		
				
		String[] text_lines = null;
        File my_file = new File(path, file_path);
        if(my_file.exists())
        {
    		ArrayList<String> file_text = new ArrayList<String>();
	        Scanner myScanner = new Scanner(my_file);
	        
	        while (myScanner.hasNextLine()) {
	          String data = myScanner.nextLine() + System.lineSeparator();
	          file_text.add(data);
	        }
	        
	        myScanner.close();
	        text_lines = file_text.toArray(new String[0]);
        }        
        return text_lines;
	}
	
	public static void TextSave(String path, String headers, String[] file_text) throws IOException{
		Files.deleteIfExists(new File(path).toPath());
		
    	FileWriter fileWriter = new FileWriter(path);
    	fileWriter.append(headers + System.lineSeparator());
    	for(String text: file_text) {
    		fileWriter.append(text + System.lineSeparator());
    	}
    	fileWriter.close();
		return;
	}
	
	
	
}
