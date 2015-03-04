package settings;

public class Settings {

	public static String Datapath = "";
	public static String Resultpath = "";
	public static String Statspath = "";
	public static String workingDir = "";
	public static String Repositorypath = "C://Users//Raman Workstation//Documents//GitHub//TypeChef//.git";
	public static String Repositoryname = "TypeChef";
	public static String owner = "joyent";
	public static String repo = "node";
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
