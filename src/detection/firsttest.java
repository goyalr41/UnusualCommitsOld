package detection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import comment.extractcomment;

public class firsttest {
	
	public static double round2(double d) {
	     return Math.round(d * 1000) / 1000.0;
	}
	
public static void main(String[] args) throws NoHeadException, GitAPIException {
		
		Map<String,Double> mn = new HashMap<String,Double>();
		Map<String,Double> mn1 = new HashMap<String,Double>();
		
		try {
	        String Repository = "node.git";
			
	        File gitDir = new File("C://Users//Raman Workstation//Documents//GitHub//"+ Repository);
	        Repository repository = new FileRepository(gitDir);
	        Git git = new Git(repository);
	        
	        Iterable<RevCommit> logs = new Git(repository).log().all().call();
	        
	        //ObjectId lastCommitId = repository.resolve(Constants.MASTER);
	        ObjectId parentid = null;
	        ObjectId currid = null;
	        
	        //a new reader to read objects from getObjectDatabase()
	        ObjectReader reader = repository.newObjectReader();
	        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
	        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
	        
	        List<DiffEntry> diffs = null;
	        
	        File dir = new File("C://Users//Raman Workstation//workspace//UnusualCommits//Results//"+Repository);
	        dir.mkdirs();
	        FileWriter writer = new FileWriter(dir+"//result.tsv");
	        int normal = 0, anamoly = 0, authdatanot = 0;
	        
	        writer.append("Commit id\t");
	        writer.append("Author E-mail\t");
	        writer.append("LOC Changed\t");
	        writer.append("Global LOC Quartile?\t");
	        writer.append("Author LOC Quartile\t");
	        writer.append("LOC Added\t");
	        writer.append("Global LOC Added Quartile?\t");
	        writer.append("Author LOC  Added Quartile\t");
	        writer.append("LOC Removed\t");
	        writer.append("Global LOC Removed Quartile?\t");
	        writer.append("Author LOC  Removed Quartile\t");
	        writer.append("NOF Changed\t");
	        writer.append("Global NOF Changed Quartile?\t");
	        writer.append("Author NOF Changed Quartile\t");
	        writer.append("NOF Added\t");
	        writer.append("Author NOF Added Quartile\t");
	        writer.append("NOF Removed\t");
	        writer.append("Author NOF Removed Quartile\t");      
	        writer.append("Time of commit\t");
	        writer.append("Author time quartile?\t");
	        writer.append("TOF Changed\t");
	        writer.append("Global TOF Changed Quartile?\t");
	        writer.append("Author TOF Changed Quartile\t");
	        writer.append("Decision\t");
	        writer.append("Value\t");
	        writer.append("Comment\n");
	        
	        
	        Double glb25loc = 0.0,glb75loc = 0.0,glb25nof = 0.0,glb75nof = 0.0, glb100loc = 0.0, glb100nof = 0.0, glb25locadd = 0.0, glb75locadd = 0.0, glb100locadd = 0.0, glb25nofadd = 0.0, glb75nofadd = 0.0, glb100nofadd = 0.0, glb25locrem = 0.0, glb75locrem = 0.0, glb100locrem = 0.0, glb25nofrem = 0.0, glb75nofrem = 0.0, glb100nofrem = 0.0;
			int loc = 0;
			Scanner fileScanner = new Scanner(new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\Stats\\"+Repository+"\\Global\\quartile.txt"));
			fileScanner.useDelimiter(", |\n");
			while (fileScanner.hasNext()){
				//System.out.println(fileScanner.next());
			  if(fileScanner.next().startsWith("[R")) {
				  if(loc == 0 ){
					  glb25loc = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  glb75loc = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  fileScanner.next();
					  fileScanner.next();
					  String kl = fileScanner.next();
					  kl = kl.substring(0, kl.lastIndexOf(")"));
					  glb100loc = Double.parseDouble(kl);
					  loc = 1;
				  }else if (loc == 1) {
					  glb25locadd = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  glb75locadd = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  fileScanner.next();
					  fileScanner.next();
					  String kl = fileScanner.next();
					  kl = kl.substring(0, kl.lastIndexOf(")"));
					  glb100locadd = Double.parseDouble(kl);
					  loc = 2;
				  }else if (loc == 2) {
					  glb25locrem = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  glb75locrem = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  fileScanner.next();
					  fileScanner.next();
					  String kl = fileScanner.next();
					  kl = kl.substring(0, kl.lastIndexOf(")"));
					  glb100locrem = Double.parseDouble(kl);
					  loc = 3;
				  }else if (loc == 3) {
					  glb25nof = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  glb75nof = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  fileScanner.next();
					  fileScanner.next();
					  String kl = fileScanner.next();
					  kl = kl.substring(0, kl.lastIndexOf(")"));
					  glb100nof = Double.parseDouble(kl);
					  loc = 4;
				  }else if (loc == 4) {
					  glb25nofadd = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  glb75nofadd = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  fileScanner.next();
					  fileScanner.next();
					  String kl = fileScanner.next();
					  kl = kl.substring(0, kl.lastIndexOf(")"));
					  glb100nofadd = Double.parseDouble(kl);
					  loc = 5;
				  }else if (loc == 5) {
					  glb25nofrem = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  glb75nofrem = Double.parseDouble(fileScanner.next());
					  fileScanner.next();
					  fileScanner.next();
					  fileScanner.next();
					  String kl = fileScanner.next();
					  kl = kl.substring(0, kl.lastIndexOf(")"));
					  glb100nofrem = Double.parseDouble(kl);
					  loc = 0;
				  }
			  }
			}
			
		
		    fileScanner.close();
		    
	        double iqr_glblocl = glb25loc - 1.5*(glb75loc - glb25loc);
	        double iqr_glbloch = glb75loc + 1.5*(glb75loc - glb25loc);
	       
	        double iqr_glblocaddl = glb25locadd - 1.5*(glb75locadd - glb25locadd);
	        double iqr_glblocaddh = glb75locadd + 1.5*(glb75locadd - glb25locadd);
	        
	        double iqr_glblocreml = glb25locrem - 1.5*(glb75locrem - glb25locrem);
	        double iqr_glblocremh = glb75locrem + 1.5*(glb75locrem - glb25locrem);
			
	        double iqr_glbnofl = glb25nof - 1.5*(glb75nof - glb25nof);
	        double iqr_glbnofh = glb75nof + 1.5*(glb75nof - glb25nof);
	         
	        double iqr_glbnofaddl = glb25nofadd - 1.5*(glb75nofadd - glb25nofadd);
	        double iqr_glbnofaddh = glb75nofadd + 1.5*(glb75nofadd - glb25nofadd);
	        
	        double iqr_glbnofreml = glb25nofrem - 1.5*(glb75nofrem - glb25nofrem);
	        double iqr_glbnofremh = glb75nofrem + 1.5*(glb75nofrem - glb25nofrem);
			//fileScanner.close();
		
	        //To count total number of Commits.
	        int count = 0; 
	        
	        filecount fc = new filecount();
	        mn = fc.glbfilecount();
	       
	        int total = 0; 
	        
	        for (RevCommit rev : logs) {
	        	total++;
	        }
	       
	        total = total/2;
	       
	        Iterable<RevCommit> logs1 = new Git(repository).log().all().call();
	        for (RevCommit rev : logs1) {
	    		count++;
	        	if(count > total) {
	        		//System.out.println(count);
	        		break;
	        	}
	        		
	        	if(rev.getParentCount() == 1) { //Not tking Merge commits, for taking them make it >= .
	    	       
	        		try {
	    	        	
	    	        	parentid = repository.resolve(rev.getParent(0).getName()+"^{tree}");
	    	        	currid = repository.resolve(rev.getName()+"^{tree}");
		    	        //System.out.println("Printing diff between tree: " + currid + " and " + parentid);
	    	            
	    	            //Reset this parser to walk through the given tree
	    	            oldTreeIter.reset(reader, parentid);
	    	            newTreeIter.reset(reader, currid);
	    	            diffs = git.diff() //Returns a command object to execute a diff command
	    	                    .setNewTree(newTreeIter)
	    	                    .setOldTree(oldTreeIter)
	    	                    .call(); //returns a DiffEntry for each path which is different

	    	        } catch (RevisionSyntaxException | IOException | GitAPIException e) {
	    	            e.printStackTrace();
	    	        }
	    	       
	    	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	        //Create a new formatter with a default level of context.
	    	        DiffFormatter df = new DiffFormatter(out);
	    	        //Set the repository the formatter can load object contents from.
	    	        df.setRepository(git.getRepository());
	    	        
	    	        ArrayList<String> diffText = new ArrayList<String>();
	    	        ArrayList<String> filetypes = new ArrayList<String>();
	    	        
	    	        //A DiffEntry is 'A value class representing a change to a file' therefore for each file you have a diff entry
	    	        int ovllineadd = 0;
	                int ovllinerem = 0;
	                int totallinechanged = 0;
	                int totalfilrem = 0; 
	                int totalfiladd = 0;
	                int cnt = 0;
	    	        Map<String, Long> filecount = new HashMap<>();

	    	        for(DiffEntry diff : diffs) {
	    	           
	    	        	try {
	    	                
	    	                df.format(diff);  //Format a patch script for one file entry.
	    	                RawText r = new RawText(out.toByteArray());
	    	                r.getLineDelimiter();
	    	                diffText.add(out.toString());
	    	                
	    	                String st = out.toString();
	    	                String filetype = null;
	    	                int filerem = 0,fileadd = 0, filtyp = 0;;
	    	                int lineadd = 0, linerem = 0;
	    	                
	    	                String[] temp = st.split("\n");
	    	                
	    	                for(String tem : temp) {
	    	                	
	    	                	if(tem.startsWith("---") || tem.startsWith("+++")) {
		    	                	
	    	                		if(tem.startsWith("---")) {
		    	                		if(tem.endsWith("null")) {
		    	                			fileadd++;;
		    	                		}else {
		    	                			
		    	                			int h = tem.lastIndexOf(".");
		    	                			//int p = tem.lastIndexOf("/");
		    	                			if(h >= 0) {
		    	                				filetype = tem.substring(h, tem.length());
		    	                				if(filtyp != 1) {
		    		    	                		filetypes.add(filetype);
		    		    	                		if(filecount.containsKey(filetype)){
		    		    	                			long value = filecount.get(filetype)+1;
		    		    	                			filecount.put(filetype, value);
		    		    	                		}else {
		    		    	                			filecount.put(filetype, (long)1);
		    		    	                		}
		    		    	                		filtyp = 1;
		    		    	                	}
		    	                			}/*else if (p >= 0) {
		    	                				filetype = tem.substring(p+1, tem.length());
		    	                				if(filtyp != 1) {
		    		    	                		filetypes.add(filetype);
		    		    	                		filtyp = 1;
		    		    	                	}
		    	                			}*/
		    	                		}
		    	                	}
		    	                	if(tem.startsWith("+++")) {
		    	                		if(tem.endsWith("null")) {
		    	                			filerem++;
		    	                		} else {
		    	                			
		    	                			int h = tem.lastIndexOf(".");
		    	                			//int p = tem.lastIndexOf("/");
		    	                			if(h >= 0) {
		    	                				filetype = tem.substring(h, tem.length());
		    	                				if(filtyp != 1) {
		    		    	                		filetypes.add(filetype);
		    		    	                		if(filecount.containsKey(filetype)){
		    		    	                			long value = filecount.get(filetype)+1;
		    		    	                			filecount.put(filetype, value);
		    		    	                		}else {
		    		    	                			filecount.put(filetype, (long)1);
		    		    	                		}
		    		    	                		filtyp = 1;
		    		    	                	}
		    	                			}/*else if (p >= 0) {
		    	                				filetype = tem.substring(p+1, tem.length());
		    	                				if(filtyp != 1) {
		    		    	                		filetypes.add(filetype);
		    		    	                		if(filecount.containsKey(filetype)){
		    		    	                			long value = filecount.get(filetype)+1;
		    		    	                			filecount.put(filetype, value);
		    		    	                		}else {
		    		    	                			filecount.put(filetype, (long)1);
		    		    	                		}
		    		    	                		filtyp = 1;
		    		    	                	}
		    	                			}*/
		    	                		}
		    	                	}
		    	                
	    	                	}
	    	                	
	    	                	if(tem.startsWith("+") && !tem.startsWith("+++")) {
	    	                		lineadd++;
	    	                	}else if(tem.startsWith("-") && !tem.startsWith("---")) {
	    	                		linerem++;
	    	                	}
	    	                	
	    	                }
	    	                ovllineadd += lineadd;
	    	                ovllinerem += linerem;
	    	                totallinechanged += (lineadd + linerem);
	    	            	totalfiladd += fileadd;
	    	                totalfilrem += filerem;
	    	                
	    	                out.reset();
	    	                
	    	            } catch (IOException e) {
	    	                e.printStackTrace();
	    	            } 
	    	         }
	    	      
	    	    	
		    	    PersonIdent p = rev.getAuthorIdent();
		    	    DateFormat formatter= new SimpleDateFormat("HH");
		        	formatter.setTimeZone(TimeZone.getTimeZone("UTC"));  
	    	        writer.append(rev.getName().substring(0,11)+"\t");
	    	        writer.append(p.getEmailAddress()+"\t");
	    	        
	        		
		        	  int var = 0;
		  			  double auth25time = 0.0, auth75time = 0.0, auth100time = 0.0, auth25nof = 0.0, auth75nof = 0.0, auth25loc = 0.0, auth75loc = 0.0, auth100loc = 0.0, auth100nof = 0.0, auth25locadd = 0.0, auth75locadd = 0.0, auth100locadd = 0.0, auth25nofadd = 0.0, auth75nofadd = 0.0, auth100nofadd = 0.0, auth25locrem = 0.0, auth75locrem = 0.0, auth100locrem = 0.0, auth25nofrem = 0.0, auth75nofrem = 0.0, auth100nofrem = 0.0;
		  			  //String author_email = "ry@tinyclouds.org";
		  			 
		  			  File file = new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\Stats\\"+Repository+"\\Author\\"+p.getEmailAddress()+".txt");
		  			  boolean fileexist = true;	
		  			  if(file.exists()) {
		  			  		fileScanner = new Scanner(new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\Stats\\"+Repository+"\\Author\\"+p.getEmailAddress()+".txt"));
		  			  		fileScanner.useDelimiter(", |\n");
		  			  		while (fileScanner.hasNext()){
		  					//System.out.println(fileScanner.next());
		  			  			if(fileScanner.next().startsWith("[R")) {
		  			  				if(var == 0 ){
		  			  					auth25time = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  auth75time = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						  String kl = fileScanner.next();
				  						  kl = kl.substring(0, kl.lastIndexOf(")"));
				  						  auth100time = Double.parseDouble(kl);
				  						  var = 1;
				  					  }else if (var == 1) {
				  						  auth25loc = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  auth75loc = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						  String kl = fileScanner.next();
				  						  kl = kl.substring(0, kl.lastIndexOf(")"));
				  						  auth100loc = Double.parseDouble(kl);
				  						  var = 2;
				  					  }else if (var == 2) {
				  						  auth25locadd = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  auth75locadd = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						 String kl = fileScanner.next();
				  						  kl = kl.substring(0, kl.lastIndexOf(")"));
				  						  auth100locadd = Double.parseDouble(kl);
				  						  var = 3;
				  					  }else if (var == 3) {
				  						  auth25locrem = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  auth75locrem = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						 String kl = fileScanner.next();
				  						  kl = kl.substring(0, kl.lastIndexOf(")"));
				  						  auth100locrem = Double.parseDouble(kl);
				  						  var = 4;
				  					  }else if (var == 4) {
				  						  auth25nof = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  auth75nof = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						 String kl = fileScanner.next();
				  						  kl = kl.substring(0, kl.lastIndexOf(")"));
				  						  auth100nof = Double.parseDouble(kl);
				  						  var = 5;
				  					  }else if (var == 5) {
				  						  auth25nofadd = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  auth75nofadd = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						 String kl = fileScanner.next();
				  						  kl = kl.substring(0, kl.lastIndexOf(")"));
				  						  auth100nofadd = Double.parseDouble(kl);
				  						  var = 6;
				  					  }else if (var == 6) {
				  						  auth25nofrem = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  auth75nofrem = Double.parseDouble(fileScanner.next());
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						  fileScanner.next();
				  						 String kl = fileScanner.next();
				  						  kl = kl.substring(0, kl.lastIndexOf(")"));
				  						  auth100nofrem = Double.parseDouble(kl);
				  						  var = 0;
				  					  }
				  				  }
				  			}
		  			  	} else {
		  			  		fileexist = false;
		  			  	}
		  				
		  			double iqr_authtimel = auth25time - 1.5*(auth75time - auth25time);
		  		    double iqr_authtimeh = auth75time + 1.5*(auth75time - auth25time);
		        		
		  			double iqr_authlocl = auth25loc - 1.5*(auth75loc - auth25loc);
		  			double iqr_authloch = auth75loc + 1.5*(auth75loc - auth25loc);
		  	        
					double iqr_authlocaddl = auth25locadd - 1.5*(auth75locadd - auth25locadd);
		  			double iqr_authlocaddh = auth75locadd + 1.5*(auth75locadd - auth25locadd);
		  				
		  			double iqr_authlocreml = auth25locrem - 1.5*(auth75locrem - auth25locrem);
		  			double iqr_authlocremh = auth75locrem + 1.5*(auth75locrem - auth25locrem);
		  				
		 			double iqr_authnofl = auth25nof - 1.5*(auth75nof - auth25nof);
		  		    double iqr_authnofh = auth75nof + 1.5*(auth75nof - auth25nof);
		  		         
		  	        double iqr_authnofaddl = auth25nofadd - 1.5*(auth75nofadd - auth25nofadd);
		  	        double iqr_authnofaddh = auth75nofadd + 1.5*(auth75nofadd - auth25nofadd);
		  		        
		  	        double iqr_authnofreml = auth25nofrem - 1.5*(auth75nofrem - auth25nofrem);
		  	        double iqr_authnofremh = auth75nofrem + 1.5*(auth75nofrem - auth25nofrem);
		  	        
		  	        double mul1 = 1;
		  	        double mul2 = 1;
		  				
		  		        
	    	        writer.append( totallinechanged + "\t" ); 
	    	        
	    	        if(totallinechanged >= iqr_glblocl && totallinechanged <= iqr_glbloch) {
	    	        	cnt++;
	    	        	writer.append("0"+"\t");
	    	        }else {
	    	        	cnt--;
	    	        	if(totallinechanged > iqr_glbloch) {
	    	        		double g = (totallinechanged - iqr_glbloch) / (glb100loc - iqr_glbloch);
	    	        		double u = round2(g);
	    	        		if(round2(g) != 0.0) {
	    	        			mul1 = mul1*round2(g);
	    	        		}
	    	        		writer.append(round2(g)+"\t");
	    	        	}else {
	    	        		double g = (iqr_glblocl - totallinechanged) / (iqr_glblocl - 0); // As minimum is always 0
	    	        		double u = round2(g);
	    	        		if(round2(g) != 0.0) {
	    	        			mul1 = mul1*round2(g);
	    	        		}
	    	        		writer.append(round2(g)+"\t");
	    	        	}
	    	        }
	    	        
	    	        if(fileexist) {
		    	    	if(totallinechanged >= iqr_authlocl && totallinechanged <= iqr_authloch) {
		        			cnt++;
		        			writer.append("0"+"\t");
		        		}else {
		        			cnt--;
		        			if(totallinechanged > iqr_authloch) {
		    	        		double g = (totallinechanged - iqr_authloch) / Math.abs(Math.max(totallinechanged,auth100loc) - iqr_authloch);
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}else {
		    	        		double g = (iqr_authlocl - totallinechanged) / (iqr_authlocl - 0); // As minimum is always 0
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}
		        		}
	    	        }else {
	    	        	writer.append("-"+"\t");
	    	        }
	    	        
	    	        
	    	    	writer.append( ovllineadd + "\t" ); 
	    	    	
	    	        if(ovllineadd >= iqr_glblocaddl && ovllineadd <= iqr_glblocaddh) {
	    	        	cnt++;
	    	        	writer.append("0"+"\t");
	    	        }else {
	    	        	cnt--;
	    	        	if(ovllineadd > iqr_glblocaddh) {
	    	        		double g = (ovllineadd - iqr_glblocaddh) / (glb100locadd - iqr_glblocaddh);
	    	        		double u = round2(g);
	    	        		if(round2(g) != 0.0) {
	    	        			mul1 = mul1*round2(g);
	    	        		}
	    	        		writer.append(round2(g)+"\t");
	    	        	}else {
	    	        		double g = (iqr_glblocaddl - ovllineadd) / (iqr_glblocaddl - 0); // As minimum is always 0
	    	        		double u = round2(g);
	    	        		if(round2(g) != 0.0) {
	    	        			mul1 = mul1*round2(g);
	    	        		}
	    	        		writer.append(round2(g)+"\t");
	    	        	}
	    	        }	
	    	        
	    	        if(fileexist) {
		    	        if(ovllineadd >= iqr_authlocaddl && ovllineadd <= iqr_authlocaddh) {
		        			cnt++;
		        			writer.append("0"+"\t");
		        		}else {
		        			cnt--;
		        			if(ovllineadd > iqr_authlocaddh) {
		    	        		double g = (ovllineadd - iqr_authlocaddh) / Math.abs(Math.max(ovllineadd,auth100locadd) - iqr_authlocaddh);
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}else {
		    	        		double g = (iqr_authlocaddl - ovllineadd) / (iqr_authlocaddl - 0); // As minimum is always 0
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}
		        		} 
	    	        }else {
	    	        	writer.append("-"+"\t");
	    	        }
	    	        
	    	        
	    	        writer.append( ovllinerem + "\t" ); 
	    	        
	    	        if(ovllinerem >= iqr_glblocreml && ovllinerem <= iqr_glblocremh) {
	    	        	cnt++;
	    	        	writer.append("0"+"\t");
	    	        }else {
	    	        	cnt--;
	    	        	if(ovllinerem > iqr_glblocremh) {
	    	        		double g = (ovllinerem - iqr_glblocremh) / (glb100locrem - iqr_glblocremh);
	    	        		double u = round2(g);
	    	        		if(round2(g) != 0.0) {
	    	        			mul1 = mul1*round2(g);
	    	        		}
	    	        		writer.append(round2(g)+"\t");
	    	        	}else {
	    	        		double g = (iqr_glblocreml - ovllinerem) / (iqr_glblocreml - 0); // As minimum is always 0
	    	        		double u = round2(g);
	    	        		if(round2(g) != 0.0) {
	    	        			mul1 = mul1*round2(g);
	    	        		}
	    	        		writer.append(round2(g)+"\t");
	    	        	}
	    	        }	
	    	        
	    	        if(fileexist) {
		    	        if(ovllinerem >= iqr_authlocreml && ovllinerem <= iqr_authlocremh) {
		        			cnt++;
		        			writer.append("0"+"\t");
		        		}else {
		        			cnt--;
		        			if(ovllinerem > iqr_authlocremh) {
		    	        		double g = (ovllinerem - iqr_authlocremh) / Math.abs(Math.max(ovllinerem,auth100locrem) - iqr_authlocremh);
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}else {
		    	        		double g = (iqr_authlocreml - ovllinerem) / (iqr_authlocreml - 0); // As minimum is always 0
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}
		        		}
	    	        }else {
	    	        	writer.append("-"+"\t");
	    	        }
	    	        
	    	        
	    	        writer.append( filetypes.size() + "\t" );
	    	        
	    	        
	    	        if(filetypes.size() >= iqr_glbnofl && filetypes.size() <= iqr_glbnofh) {
	    	        	cnt++;
	    	        	writer.append("0"+"\t");
	    	        }else {
	    	        	cnt--;
	    	        	if(filetypes.size() > iqr_glbnofh) {
	    	        		double g = (filetypes.size() - iqr_glbnofh) / (glb100nof - iqr_glbnofh);
	    	        		double u = round2(g);
	    	        		if(round2(g) != 0.0) {
	    	        			mul1 = mul1*round2(g);
	    	        		}
	    	        		writer.append(round2(g)+"\t");
	    	        	}else {
	    	        		double g = (iqr_glbnofl - filetypes.size()) / (iqr_glbnofl - 0); // As minimum is always 0
	    	        		double u = round2(g);
	    	        		if(round2(g) != 0.0) {
	    	        			mul1 = mul1*round2(g);
	    	        		}
	    	        		writer.append(round2(g)+"\t");
	    	        	}
	    	        }
	        
	    	        if(fileexist) {
		    	        if(filetypes.size() >= iqr_authnofl && filetypes.size() <= iqr_authnofh) {
		    	        	cnt++;
		    	        	writer.append("0"+"\t");
		    	        }else {
		    	        	cnt--;
		    	        	if(filetypes.size() > iqr_authnofh) {
		    	        		double g = (filetypes.size() - iqr_authnofh) / Math.abs(Math.max(filetypes.size(),auth100nof) - iqr_authnofh);
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}else {
		    	        		double g = (iqr_authnofl - filetypes.size()) / (iqr_authnofl - 0); // As minimum is always 0
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}
		    	        }
	    	        }else {
	    	        	writer.append("-"+"\t");
	    	        }
	    	        
	    	        
	    	        writer.append(totalfiladd+"\t");
	    	        
	    	        /*if(totalfiladd >= iqr_glbnofaddl && totalfiladd <= iqr_glbnofaddh) {
	    	        	cnt++;
	    	        	writer.append("0"+"\t");
	    	        }else {
	    	        	cnt--;
	    	        	if(totalfiladd > iqr_glbnofaddh) {
	    	        		double g = (totalfiladd - iqr_glbnofaddh) / (glb100nofadd - iqr_glbnofaddh);
	    	        		writer.append(g+"\t");
	    	        	}else {
	    	        		double g = (iqr_glbnofaddl - totalfiladd) / (iqr_glbnofaddl - 0); // As minimum is always 0
	    	        		writer.append(g+"\t");
	    	        	}
	    	        }*/
	        
	    	        if(fileexist) {
		    	        if(totalfiladd >= iqr_authnofaddl && totalfiladd <= iqr_authnofaddh) {
		    	        	cnt++;
		    	        	writer.append("0"+"\t");
		    	        }else {
		    	        	cnt--;
		    	        	if(totalfiladd > iqr_authnofaddh) {
		    	        		double g = (totalfiladd - iqr_authnofaddh) / Math.abs(Math.max(totalfiladd,auth100nofadd) - iqr_authnofaddh);
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}else {
		    	        		double g = (iqr_authnofaddl - totalfiladd) / (iqr_authnofaddl - 0); // As minimum is always 0
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}
		    	        }
	    	        }else {
	    	        	writer.append("-"+"\t");
	    	        }
	    	        
	    	        
	    	        writer.append(totalfilrem+"\t");
	    	        
	    	        /*if(totalfilrem >= iqr_glbnofreml && totalfilrem <= iqr_glbnofremh) {
	    	        	cnt++;
	    	        	writer.append("0"+"\t");
	    	        }else {
	    	        	cnt--;
	    	        	if(totalfilrem > iqr_glbnofremh) {
	    	        		double g = (totalfilrem - iqr_glbnofremh) / (glb100nofrem - iqr_glbnofremh);
	    	        		writer.append(g+"\t");
	    	        	}else {
	    	        		double g = (iqr_glbnofreml - totalfilrem) / (iqr_glbnofreml - 0); // As minimum is always 0
	    	        		writer.append(g+"\t");
	    	        	}
	    	        }*/
	        
	    	        if(fileexist) {
		    	        if(totalfilrem >= iqr_authnofreml && totalfilrem <= iqr_authnofremh) {
		    	        	cnt++;
		    	        	writer.append("0"+"\t");
		    	        }else {
		    	        	cnt--;
		    	        	if(totalfilrem > iqr_authnofremh) {
		    	        		double g = (totalfilrem - iqr_authnofremh) / Math.abs(Math.max(totalfilrem,auth100nofrem) - iqr_authnofremh);
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}else {
		    	        		double g = (iqr_authnofreml - totalfilrem) / (iqr_authnofreml - 0); // As minimum is always 0
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}
	    	        }
	    	        }else {
	    	        	writer.append("-"+"\t");
	    	        }

	        		String h = "", w = "";
	        		Long sum = 0L;
	        		
	        		for(String file_type : filecount.keySet()) {
	        			sum += filecount.get(file_type);	
	        		}
	        		
	        		for(String file_type : filecount.keySet()) {
	        			Double g = (Double)(filecount.get(file_type)*100.0/sum);
	        			w = w + file_type + " , " + (round2(g)) + "--";
	        		}
	        		
	        		writer.append(filecount.size()+"\t");
	        		 
	        		for(String file_type : filecount.keySet()) {
	        			if(mn.containsKey(file_type)) {
	        				if(mn.get(file_type) < 0.5) {
	        					h = h + file_type + " , " + mn.get(file_type).toString() + " -- ";
	        				}
	        			}else {
	        				h = h + file_type + " , " + "NC" + " -- ";
	        			}
	        		}
	        		
	        		writer.append(h+"\t");
	        		
	        		System.out.println(count);
	        		 
	        		String h1 = "";
	        		mn1 = fc.authfilecount(p.getEmailAddress());
	        		if(mn1.isEmpty()) {
	        			writer.append("--"+"\t");
	        		}else {
	        		for(String file_type : filecount.keySet()) {
	        			if(mn1.containsKey(file_type)) {
	        				if(mn1.get(file_type) < 0.5) {
	        					h1 = h1 + file_type + " , " + mn1.get(file_type).toString() + " --- ";
	        				}
	        			}else {
	        				h1 = h1 + file_type + " , " + "NC" + " --- ";
	        			}		
	        		}
	        		writer.append(h1+"\t");
	        		}

	        	     
	        		String tiofcommit = formatter.format(p.getWhen());
	        		double tiofcommi = Double.parseDouble(tiofcommit);
	        		writer.append(tiofcommit+"\t");
	        		
	        		if(fileexist) {
		        		if(tiofcommi >= iqr_authtimel && tiofcommi <= iqr_authtimeh) {
		        			cnt++;
		        			writer.append("0"+"\t");
		        		}else {
		        			cnt--;
		        			if(tiofcommi > iqr_authtimeh) {
		    	        		double g = (tiofcommi - iqr_authtimeh) / Math.abs(Math.max(tiofcommi,auth100time) - iqr_authtimeh);
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}else {
		    	        		double g = (iqr_authtimel - tiofcommi) / (iqr_authtimel - 0); // As minimum is always 0
		    	        		double u = round2(g);
		    	        		if(round2(g) != 0.0) {
		    	        			mul1 = mul1*round2(g);
		    	        		}
		    	        		writer.append(round2(g)+"\t");
		    	        	}
		        			
		        		}
	        		}else {
	    	        	writer.append("--"+"\t");
	    	        }
	        		
	        		
	        		if(cnt >= 0) {
	        			writer.append("Normal"+"\t");
	        			
	        			writer.append("0"+"\t");
		        		writer.append("--\n");
	        			normal++;
	        		}else {
	        			writer.append("Anamoly"+"\t");
	        			writer.append(mul1+"\t");
		        		//extractcomment ec = new extractcomment();
		        		//writer.append(ec.noofcomment(rev.getName().substring(0,11))+"\n");
	        			writer.append("--\n");
	        			anamoly++;
	        		}	
	        		
	        			

	     
	        	} 
	        }
	        	
	        
		    writer.flush();
		    writer.close();
		    
	        System.out.println("Had " + count + " commits overall in repository");
	        System.out.println("Anamoly: " + anamoly + " " + "Normal: " + normal);
	        System.out.println("Done");
	       
	        repository.close();
		} catch (IOException ex) {
	        // The repository exists, but is inaccessible!
		}
	}
}
