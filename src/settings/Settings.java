package settings;

public class Settings {

	public static String Datapath = "";
	public static String Resultpath = "";
	public static String Statspath = "";
	public static String workingDir = "";
	public static String Repositorypath = "";
	public static String Repositoryname = "";
	public static String owner = "";
	public static String repo = "";
	public static String Htmlpath = "";
	
	
	public void initiate() {
		 workingDir = System.getProperty("user.dir");		 
		 workingDir = workingDir.replace("\\", "//");
		 //System.out.println("Current working directory : " + workingDir);
		 
		 Datapath = workingDir + "//DataFull//";
		 Resultpath = workingDir + "//Results//";
		 Statspath = workingDir + "//Stats//";
		 Htmlpath = workingDir + "//src//html//";
	}

}
