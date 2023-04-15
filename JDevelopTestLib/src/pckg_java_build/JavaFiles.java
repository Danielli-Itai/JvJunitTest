package pckg_java_build;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import pckg_base.JFiles;





public class JavaFiles {
	

	public static void Print(String[] files) {
		for(String  file: files) {
			System.out.println(file);
		}
		return;
	}

	public static String getString(String[] files) {
		String files_string = "";
		for(String  file: files) {
			files_string += file + " ";
		}
		files_string = files_string.trim();
		return files_string;
	}

	
	public static String[] getAllFolders(String path) throws IOException
	{
		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		System.out.println(Arrays.toString(directories));
		return directories;
	}
	
	
	public static String[] getAllFiles(String path) throws IOException 
	{
		File file = new File(path);
		String[] files = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isFile();
		  }
		});
		System.out.println(Arrays.toString(files));
		return files;
	}
	
	public static String[] getFiles(String path, String ext) throws IOException 
	{
		File directoryPath = new File(path);
	      
		FilenameFilter textFilefilter = new FilenameFilter(){
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(ext)) {
					return true;
				} else {
					return false;
				}
			}
		};
	      
		//List of all the text files
		String filesList[] = directoryPath.list(textFilefilter);
		return filesList;
	}
	
	public static final String EXT_JAVA = "java";
	public static final String EXT_CLASS = "class";
	public static final String EXT_JAR = "jar";
	public static String[] getFilesFind(String path, String ext) 
	{
		String[] files_arr = new String[0]; 
		try {
			Stream<Path> files_find = Files.walk(Paths.get(path));
			ArrayList<String> files = new ArrayList<String>();
			files_find.forEach((file)->{
				if(file.toString().endsWith(ext)) {
					files.add(file.toString());
				}
			});
			files_find.close();
			files_arr = files.toArray(new String[0]);
		}catch (Exception e) {

		}
   		return files_arr;
	}

	public static boolean ExecutablkChk(String[] file_text)
	{
		boolean executable = false;
		for(String line : file_text) {
			if(line.contains("main")) {
				line = line.trim().replaceAll("[^a-zA-Z0-9]", " ").replaceAll("\\s{2,}", " ");
				String[] words = line.split(" ");
				if(words.length > 5) {
					if(words[0].equals("public") &&  words[1].equals("static") && words[2].equals("void") &&  words[3].contains("main")){
						executable = true;
					}
				}
			}
		}
		return executable;
	}


	public static File[] getFolders(String path, String ignore) throws IOException 
	{	
		File search_path = new File(path);
		
		File[] directories = search_path.listFiles(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return ((new File(current, name).isDirectory()) && (!name.startsWith(ignore)));
		  }
		});
		
   		return directories;
	}

	public static String[] getFoldersNames(String path, String ignore) 
	{	
		String[] directories = null;

		try {
			File search_path = new File(path);
			directories = search_path.list(new FilenameFilter() {
				@Override
				public boolean accept(File current, String name) {
					return ((new File(current, name).isDirectory()) && (!name.startsWith(ignore)));
				}
			});
		}catch(Exception e) {}
   		return directories;
	}

	
	public static String[] getFilesFind(String path, String file_name, String ext) throws IOException 
	{
		String file_full_name = file_name + "." + ext;
		
		Stream<Path> files_find = Files.find(Paths.get(path), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile());
		
		ArrayList<String> files = new ArrayList<String>();
		files_find.forEach((file)->{
			if(file.getFileName().toString().equals(file_full_name)) {
				files.add(file.toString());				
			}
		});
		
		files_find.close();
   		return files.toArray(new String[0]);
	}

	
	
	public static void DeleteFiles(String[] files) {
		for(String file:files) {
			File curr_file = new File(file);
			if(curr_file.exists()) {
				curr_file.delete();
			}
		}
		return;	
	}
}
