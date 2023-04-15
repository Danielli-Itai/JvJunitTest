package pckg_build_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pckg_base.JText;
import pckg_base.JFiles;
import pckg_java_build.JavaBuild;
import pckg_java_build.JavaFiles;
import pckg_java_build.JavaProjects;
import pckg_java_run.DevelopTestRun;




class ProcJavaBuildTest {
	private static final String WORKSPACE_DIR = "D:\\_SourceDev5.Java\\JvOOP";
		
	@BeforeEach                                         
    void setUp() {
		try {
//			this.java_build.ChangeDir(SOURCE_DIRS);        
		}catch(Exception e) {
			fail("Test failed");
		}
    }

	/*
	 * Test build process while working at the source files folder. 
	 */
	@Test
	void test1FileRun() {	 
		JavaBuild java_build = new JavaBuild(WORKSPACE_DIR);
		final String EX1_HELLO_WORLD = "Ex01.HelloWorld\\src";
		//final String EX1_HELLO_WORLD = "Ex02.LoopsAndArrays\\src";
		final String TEST_FILE = ".\\hello_world\\HelloWorld.java";
		//final String TEST_FILE = ".\\loops\\LoopsBranch.java";
		try {
			java_build.ChangeDir(EX1_HELLO_WORLD);
			java_build.CleanFile(TEST_FILE);
			java_build.BuildClass(TEST_FILE);
			String output_txt = java_build.Execute(TEST_FILE);

			String expected = java_build.AppExpOutput(TEST_FILE);
			String result = JText.Compare(expected,output_txt);
			System.out.println(result);
		}catch(Exception e) {
			fail("Test failed");
		}
		return;
	}


	
	@Test
	void test2FolderRun() {
		JavaBuild java_build = new JavaBuild(WORKSPACE_DIR);
		final String PROJECT_DIR = "Ex01.HelloWorld/src";
		//final String PROJECT_DIR = "Ex02.LoopsAndArrays";
		try {
			java_build.ChangeDir(PROJECT_DIR);
			
			String[] code_files = JavaFiles.getFilesFind(new File(WORKSPACE_DIR, PROJECT_DIR).toString(), JavaFiles.EXT_JAVA);
			for(String file: code_files) {
				file = java_build.getRelativePath(file);
				
				java_build.CleanFile(file);
				java_build.BuildClass(file);
				String output_txt = java_build.Execute(file);

				String expected = java_build.AppExpOutput(file);
				String result = JText.Compare(expected,output_txt);
				System.out.println(result);
			}
		}catch(Exception e){
			fail("Test failed");			
		}
		return;
	}
	
	
	@Test
	void test3FolderRunMethod() {
		final String PROJECT_DIR = "Ex01.HelloWorld";
		//final String PROJECT_DIR = "Ex02.LoopsAndArrays";
		try {
			JavaProjects project_run = new JavaProjects(WORKSPACE_DIR);
			String project_path = new File(WORKSPACE_DIR, PROJECT_DIR).toString();
			String[] code_files = JavaFiles.getFilesFind(project_path, JavaFiles.EXT_JAVA);
			for(String file: code_files) {
				project_run.TestFile(PROJECT_DIR, file);
			}
			
		}catch(Exception e){
			fail("Test failed");			
		}
		return;
	}
	

	
	
	@Test
	void test3WorkspaceRun() {
		JavaProjects workspace_run = new JavaProjects(WORKSPACE_DIR);
		try {
			String[] ws_projects = JavaFiles.getFoldersNames(WORKSPACE_DIR, ".");
			for(String project: ws_projects) {
				workspace_run.ProjectRun(project);
			}
			
		}catch(Exception e) {
			fail("Test failed");
		}
	}	

	@Test
	void test3WorkspaceRunMethod() {
		try {
			DevelopTestRun.WorkspaceRun(WORKSPACE_DIR);			
		}catch(Exception e) {
			fail("Test failed");
		}
	}	

	@Test
	void test5CleanWorkspace() {
		JavaProjects workspace_run = new JavaProjects(WORKSPACE_DIR);
		try {
			String[] ws_projects = JavaFiles.getFoldersNames(WORKSPACE_DIR, ".");
			for(String project: ws_projects) {
				workspace_run.ProjectClean(project);
			}
			
		}catch(Exception e) {
			fail("Test failed");
		}
	}	


}
