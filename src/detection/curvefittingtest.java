package detection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;

import main.maincla;

import org.apache.commons.io.FileUtils;
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

import extract.writedata;
import settings.Settings;
import stats.Curvefitting;
//import comment.extractcomment;

public class curvefittingtest {
	
	public static double round2(double d) {
	     return Math.round(d * 10000) / 10000.0;
	}
	
	public static double aggregate(double a, double b) {
		return (a + b - a*b);
	}
	
	public static double mapping(double x) {
		return (1-Math.pow((1.0-x),(1.0/15.0)));
	}
	
	public static void writeit(List<Double> val, double value, double alpha, double beta,  FileWriter writer) throws IOException{

	     double j = (1- Math.pow((alpha/value),beta));
	     if(j < 0) {
	    	 j = 0.0;
	     }
	     double g = mapping(j);
	     val.add(g); 
	     
	     j = round2(j);
	     g = round2(g);
	     writer.append(j + " , " + g +"\t");
	       
	}
	
	public static void writeitauthor(List<Double> val, double value, double alpha, double beta,  FileWriter writer) throws IOException{


	     double j = (1- Math.pow((alpha/value),beta));
	     if(j < 0) {
	    	 j = 0.0;
	     }
	     double g = mapping(j);
	     val.add(g); 
	     
	     j = round2(j);
	     g = round2(g);
	     writer.append(j + " , " + g +"\t");
	}
	
	public void test() throws NoHeadException, GitAPIException, IOException {
		
		//Map<String,Double> mn = new HashMap<String,Double>();
		//Map<String,Double> mn1 = new HashMap<String,Double>();
		
		writedata wd = maincla.wd;
		Curvefitting cf = maincla.cf;
		
		try {
	        String Repositoryname = Settings.Repositoryname;
	        String Repositorypath = Settings.Repositorypath;
	        
			File gitDir = new File(Repositorypath);
	        Repository repository = new FileRepository(gitDir);
	        Git git = new Git(repository);
	        	        
	        //ObjectId lastCommitId = repository.resolve(Constants.MASTER);
	        ObjectId parentid = null;
	        ObjectId currid = null;
	        
	        //a new reader to read objects from getObjectDatabase()
	        ObjectReader reader = repository.newObjectReader();
	        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
	        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
	        
	        List<DiffEntry> diffs = null;
	        
	        String Resultpath = Settings.Resultpath;
	        
	        File dir = new File(Resultpath + Repositoryname);
	        dir.mkdirs();
	        FileWriter writer = new FileWriter(dir+"//result.tsv");
	        int normal = 0, anamoly = 0;
	        
	        writer.append("Commit id\t");
	        writer.append("Author E-mail\t");
	        writer.append("LOC Changed\t");
	        writer.append("Global LOC?\t");
	        writer.append("Author LOC?\t");
	        writer.append("LOC Added\t");
	        writer.append("Global LOC Added?\t");
	        writer.append("Author LOC  Added?\t");
	        writer.append("LOC Removed\t");
	        writer.append("Global LOC Removed?\t");
	        writer.append("Author LOC  Removed?\t");
	        writer.append("NOF Changed\t");
	        writer.append("Global NOF Change?\t");
	        writer.append("Author NOF Changed?\t");
	        writer.append("NOF Added\t");
	        writer.append("Author NOF Added?\t");
	        writer.append("NOF Removed\t");
	        writer.append("Author NOF Removed?\t");  
	        writer.append("Commit Msg\t");
	        writer.append("Global Commit Msg?\t");
	        writer.append("Author Commit Msg?\t");
	        writer.append("Time of commit\t");
	        writer.append("Author time quartile?\t");
	        writer.append("File Percent Type\t");
	        writer.append("File percent changed\t");
	        writer.append("File Per Commit Type\t");
	        writer.append("File per commit\t");
	        writer.append("File Comb Type\t");
	        writer.append("Combination frequency\t");
	        
	        writer.append("Author File Percent Type\t");
	        writer.append("Author File percent changed\t");
	        writer.append("Author File Per Commit Type\t");
	        writer.append("Author File per commit\t");
	        writer.append("Author File Comb Type\t");
	        writer.append("Author Combination frequency\t");
	       /* writer.append("TOF Changed\t");
	        writer.append("Global TOF Changed Quartile?\t");
	        writer.append("Author TOF Changed Quartile\t");*/
	        writer.append("Decision\t");
	        writer.append("Value");
	       // writer.append("Comment\t");
	        writer.append("\n");
	        	        
	        naivemethod nm = new naivemethod();
	        nm.buildglobal();
	        
	        //To count total number of Commits.
	        int count = 0; 
	        
	        //filecount fc = new filecount();
	        //mn = fc.glbfilecount();
	       
	       
	        int total = 0; 
	        Iterable<RevCommit> logs = new Git(repository).log().all().call();

	        List<RevCommit> logs1 = new ArrayList<RevCommit>();
	        for(RevCommit rev: logs){
	        	logs1.add(rev);
	        	total++;
	        }
	        
	        total = total/2;
	        Collections.reverse(logs1);
	        
	        for (RevCommit rev : logs1) {
	    		count++;
	        	
	        	if(count <= total) {
	        		continue;
	        	}
	        		
	        	if(rev.getParentCount() == 1) { //Not taking Merge commits, for taking them make it >= .
	    	       
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
		    	                				filetype = filetype.replace(" ", "");
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
		    	                				filetype = filetype.replace(" ", "");

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
	    	        String tiofcommit = formatter.format(p.getWhen());
	    	        
	    	        writer.append(rev.getName().substring(0,11)+"\t");
	    	        
	    	        String email = p.getEmailAddress();
	    	        if(email.contains("://") || email.contains("//")) {
	    	        	email = email.replace(":", "");
	    	        	email = email.replace("//", "");
	    	        }
	    	        writer.append(email.substring(0, 8)+"\t"); //To get shorter result

	    	        //System.out.println("Tada");
	    	        
		    	    String[] commsgwords = rev.getFullMessage().split(" ");
	    	        
	    	        //This represents enough size of author data
		    	    boolean exists = wd.write(rev.getName().substring(0,11), email, totallinechanged, ovllineadd, ovllinerem, filetypes.size(), totalfiladd, totalfilrem, Integer.parseInt(formatter.format(p.getWhen())), filetypes, commsgwords.length);
	        		    
	    	        List<Double> val = new ArrayList<Double>();
	    	        
	    	        //System.out.println("Com");
	    	        
	    	        cf.calcglobal();
	    	        if(exists) {
	    	        	cf.calculateauthor(email);
	    	        }
	    	        
	    	        filetypetest ftt = new filetypetest();
	    	        ftt.calculateglb(filetypes);
	    	        if(exists) {
	    	        	ftt.calculateauth(filetypes, email);
	    	        }
	    	        
	        		writer.append( totallinechanged + "\t" ); 
	    	        writeit(val, totallinechanged, Curvefitting.totallocalpha, Curvefitting.totallocbeta, writer);

	    	        if(exists) {
	    	        	writeitauthor(val, totallinechanged+1, Curvefitting.totallocauthalpha, Curvefitting.totallocauthbeta, writer);
	    	        }else {
	    	        	val.add(mapping(0.5));
	    	        	writer.append(round2(mapping(0.5))+"\t");
	    	        }
	    	        
	    	        writer.append( ovllineadd + "\t" ); 
	    	        writeit(val, ovllineadd+1, Curvefitting.locaddalpha, Curvefitting.locaddbeta, writer);
	    	        if(exists) {	
	    	        	writeitauthor(val, ovllineadd+1, Curvefitting.locaddauthalpha, Curvefitting.locaddauthbeta, writer);
	        		}else {
	        			val.add(mapping(0.5));
	    	        	writer.append(round2(mapping(0.5))+"\t");
	    	        }
	        	
	    	        writer.append( ovllinerem + "\t" ); 
	    	        writeit(val,ovllinerem+1, Curvefitting.locremalpha, Curvefitting.locrembeta, writer);
	    	        if(exists) {	
	    	        	writeitauthor(val,ovllinerem+1, Curvefitting.locremauthalpha, Curvefitting.locremauthbeta, writer);
	        		}else {
	        			val.add(mapping(0.5));
	    	        	writer.append(round2(mapping(0.5))+"\t");
	        		}
	        
	    	        writer.append( filetypes.size() + "\t" );
	    	        writeit(val,filetypes.size()+1, Curvefitting.nofalpha, Curvefitting.nofbeta, writer);
	    	        if(exists) {
	    	        	writeitauthor(val, filetypes.size()+1, Curvefitting.nofauthalpha, Curvefitting.nofauthbeta, writer);
	    	        }else {
	    	        	val.add(mapping(0.5));
	    	        	writer.append(round2(mapping(0.5))+"\t");
	        		}
	    	        
	    	        writer.append(totalfiladd+"\t");
	    	        if(exists) {
	    	        	writeitauthor(val,totalfiladd+1, Curvefitting.nofaddauthalpha, Curvefitting.nofaddauthbeta, writer);
	        		}else {
	        			val.add(mapping(0.5));
	    	        	writer.append(round2(mapping(0.5))+"\t");
	        		}
	    	        
	    	        writer.append(totalfilrem+"\t");
	    	        if(exists) {
	    	        	writeitauthor(val, totalfilrem+1,   Curvefitting.nofremauthalpha, Curvefitting.nofremauthbeta, writer);
	    	        }else {
	    	        	val.add(mapping(0.5));
	    	        	writer.append(round2(mapping(0.5))+"\t");
	        		}
	    	        
	    	        writer.append( rev.getFullMessage().length() + "\t" );
	    	        writeit(val,rev.getFullMessage().length()+1, Curvefitting.commsgalpha, Curvefitting.commsgbeta, writer);
	    	        if(exists) {
	    	        	writeitauthor(val, rev.getFullMessage().length()+1, Curvefitting.commsgauthalpha, Curvefitting.commsgauthbeta, writer);
	    	        }else {
	    	        	val.add(mapping(0.5));
	    	        	writer.append(round2(mapping(0.5))+"\t");
	        		}
	    	        
	        		double tiofcommi = Double.parseDouble(tiofcommit);
	        		writer.append(tiofcommit+"\t");
	    	        if(exists) {
	    	        	Map<Double, Double> timemap = new HashMap<>();
	    	        	for(double i = 0; i <= 23; i++) {
	    	        		timemap.put(i, 0.0);
	    	        	}
	    	        	File authtime = new File(Settings.Datapath + Repositoryname +"//Author//TimeFiles//"+ email+".tsv"); 
	    	            List<String> sizech = FileUtils.readLines(authtime);
	    	            sizech.remove(0);
	    	            int v = 0;
	    	            for(String qwer: sizech) {
	    	            	if(v >= sizech.size()-1){
	    	            		break;
	    	            	}
	    	            	String[] asd = qwer.split("\t");
	    	            	double zxc =  Double.parseDouble(asd[1]);
	    	            	if(timemap.containsKey(zxc)) {
	    	            		timemap.put(zxc, timemap.get(zxc)+1.0);
	    	            	}else {
	    	            		timemap.put(zxc,1.0);
	    	            	}
	    	            }
	    	            double mnb1 =  Double.parseDouble(tiofcommit) - 1;
	    	            if(mnb1 < 0) {
	    	            	mnb1 = mnb1 + 24.0;
	    	            }
	    	            double mnb2 =  Double.parseDouble(tiofcommit) + 1;
	    	            if(mnb2 >= 24) {
	    	            	mnb2 = mnb1 % 24.0;
	    	            }
	    	            double mnb = (timemap.get(Double.parseDouble(tiofcommit)) + timemap.get(mnb1) + timemap.get(mnb2))/3;
	    	            if(mnb < 0.05*(sizech.size()-1)) {
	    	            	val.add(mapping(0.995));
	    	            	writer.append(round2(mnb/(sizech.size()-1)) + " , " + round2(mapping(0.995))+"\t");
	    	            }else {
	    	            	val.add(mapping(0));
	    	            	writer.append(round2(mnb/(sizech.size()-1)) + " , " + round2(mapping(0))+"\t");
	    	            }
	    	        }else {
	    	        	val.add(mapping(0.5));
	    	        	writer.append(round2(mapping(0.5))+"\t");
	        		}
	        		/*String h = "", w = "";
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
	        		}*/
	    	        
	    	        String[] num;
	    	        
	    	        writer.append(ftt.minpercentfiltyp+"\t");
	    	        //writer.append(ftt.filepercent+"\t");
	    	        if(!ftt.filepercent.equals("NA")) {
	    	        	num = ftt.filepercent.split(",");
	    	        	writer.append(num[0] + " , ");
	    	        	writer.append(round2(mapping(Double.parseDouble(num[1].trim())))+"\t");
	    	        	val.add(mapping(Double.parseDouble(num[1].trim())));
	    	        }else {
	    	        	writer.append("NA\t");
	    	        }
	    	        
	    	        writer.append(ftt.minpercommitfiltyp+"\t");
	    	        //writer.append(ftt.filepercommit+"\t");
	    	        if(!ftt.filepercommit.equals("NA")) {
		    	        num = ftt.filepercommit.split(",");
		    	        writer.append(num[0] + " , ");
		    	        writer.append(round2(mapping(Double.parseDouble(num[1].trim())))+"\t");
		    	        val.add(mapping(Double.parseDouble(num[1].trim())));
	        	 	}else {
	    	        	writer.append("NA\t");
	    	        }
	    	        
	    	        writer.append(ftt.mincombfrefiltyp+"\t");
	    	        //writer.append(ftt.filecombfre+"\t");
	    	        if(!ftt.filecombfre.equals("NA")) {
		    	        num = ftt.filecombfre.split(",");
		    	        writer.append(num[0] + " , ");
		    	        writer.append(round2(mapping(Double.parseDouble(num[1].trim())))+"\t");
		    	        val.add(mapping(Double.parseDouble(num[1].trim())));
	        		}else {
	        			writer.append("NA\t");
	        		}
	    	        
	    	        if(exists) {
	    	        	writer.append(ftt.authminpercentfiltyp+"\t");
		    	        if(!ftt.authfilepercent.equals("NA")) {
			    	        //writer.append(ftt.filepercent+"\t");
			    	        num = ftt.authfilepercent.split(",");
			    	        writer.append(num[0] + " , ");
			    	        writer.append(round2(mapping(Double.parseDouble(num[1].trim())))+"\t");
			    	        val.add(mapping(Double.parseDouble(num[1].trim())));
		    	        }else {
		        			writer.append("NA\t");
		    	        }
		    	    }else {
	        			val.add(mapping(0.5));
	    	        	writer.append(" NA \t" +round2(mapping(0.5))+"\t");
	    	        }
	    	        
	    	        if(exists) {
	    	        	writer.append(ftt.authminpercommitfiltyp+"\t");
		    	        if(!ftt.authfilepercommit.equals("NA")) {
			    	        //writer.append(ftt.filepercommit+"\t");
			    	        num = ftt.authfilepercommit.split(",");
			    	        writer.append(num[0] + " , ");
			    	        writer.append(round2(mapping(Double.parseDouble(num[1].trim())))+"\t");
			    	        val.add(mapping(Double.parseDouble(num[1].trim())));
		    	        }else {
		        			writer.append("NA\t");
		    	        }
		    	    }else {
	        			val.add(mapping(0.5));
	    	        	writer.append(" NA \t" + round2(mapping(0.5))+"\t");
	    	        }
	    	        
	    	        if(exists) {
	    	        	writer.append(ftt.authmincombfrefiltyp+"\t");
			    	        //writer.append(ftt.filecombfre+"\t");
		    	        if(!ftt.authfilecombfre.equals("NA")) {
			    	        num = ftt.authfilecombfre.split(",");
			    	        writer.append(num[0] + " , ");
			    	        writer.append(round2(mapping(Double.parseDouble(num[1].trim())))+"\t");
			    	        val.add(mapping(Double.parseDouble(num[1].trim())));
		    	        }else {
		        			writer.append("NA\t");
		    	        }
	    	        }else {
	    	        	val.add(mapping(0.5));
	    	        	writer.append(" NA \t" + round2(mapping(0.5))+"\t");
	    	        }
	    	          
	        		System.out.println(count);

	    	        double ff = val.get(0);
	    	        for(double j : val) {
	    	        	ff = aggregate(ff,j);
	    	        }
	        		
	        		if(ff <= 0.8) {
	        			writer.append("Normal"+"\t");
	        			writer.append(round2(ff)+"\n");
		        		//writer.append("--\n");
	        			normal++;
	        		}else {
	        			writer.append("Anamoly"+"\t");
	        			writer.append(round2(ff)+"\n");
		        		//extractcomment ec = new extractcomment();
		        		//writer.append(ec.noofcomment(rev.getName().substring(0,11))+"\n");
	        			//writer.append("--\n");
	        			anamoly++;
	        		}	
	    		    writer.flush();

	        	} 
	        }
	        	
	     		    writer.close();
		    
	        System.out.println("Had " + count + " commits overall in repository");
	        System.out.println("Anamoly: " + anamoly + " " + "Normal: " + normal);
	        System.out.println("Done");
	       
	        repository.close();
		} catch (IOException ex) {
	        // The repository exists, but is inaccessible!
			System.out.println("Exception");
		}
	}
}
