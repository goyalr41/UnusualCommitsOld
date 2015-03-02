package detection;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class retit {
	public static void main(String args[]) throws FileNotFoundException {
		Double glb25loc = 0.0,glb75loc = 0.0,glb25nof = 0.0,glb75nof = 0.0;
		int loc = 0;
		String Repository = "node.git";
		Scanner fileScanner = new Scanner(new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\Stats\\"+Repository+"\\Global\\quartile.txt"));
		fileScanner.useDelimiter(", |\n");
		while (fileScanner.hasNext()){
			//System.out.println(fileScanner.next());
		  if(fileScanner.next().startsWith("[R")) {
			  if(loc == 0 ){
				  glb25loc = Double.parseDouble(fileScanner.next());
				  fileScanner.next();
				  glb75loc = Double.parseDouble(fileScanner.next());
				  loc = 1;
			  }else if (loc == 1) {
				  glb25nof = Double.parseDouble(fileScanner.next());
				  fileScanner.next();
				  glb75nof = Double.parseDouble(fileScanner.next());
				  loc = 0;
				  break;
			  }
		  }
		}
		  int var = 0;
		  double auth25time = 0.0, auth75time = 0.0, auth25nof = 0.0, auth75nof = 0.0, auth25loc = 0.0, auth75loc = 0.0;
		  String author_email = "ry@tinyclouds.org";
		  fileScanner = new Scanner(new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\Stats\\"+Repository+"\\Author\\"+author_email+".txt"));
		  fileScanner.useDelimiter(", |\n");
			while (fileScanner.hasNext()){
				//System.out.println(fileScanner.next());
			  if(fileScanner.next().startsWith("[R")) {
				  if(var == 0 ){
					  auth25time = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  auth75time = Double.parseDouble(fileScanner.next());
					  var = 1;
				  }else if (var == 1) {
					  auth25nof = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  auth75nof = Double.parseDouble(fileScanner.next());
					  var = 2;
				  }else if (var == 2) {
					  auth25loc = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  auth75loc = Double.parseDouble(fileScanner.next());
					  var = 0;
					  break;
				  }
			  }
		}
		fileScanner.close();
		
		//System.out.println(glb25loc + " " + glb75loc + " " + glb25nof + " " + glb75nof);
		//System.out.println(auth25loc + " " + auth75loc + " " + auth25nof + " " + auth75nof);
	}
	
}

