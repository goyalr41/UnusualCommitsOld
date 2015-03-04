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
import java.util.SortedMap;
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

import settings.Settings;
//import comment.extractcomment;

public class lognaivemethodtest {
	
	public static double round2(double d) {
	     return Math.round(d * 1000) / 1000.0;
	}
	
	public static double aggregate(double a, double b) {
		return (a + b - a*b);
	}
	public static void writeit(SortedMap<Double,Double> ma, List<Double> lis, List<Double> val, naivemethod nm, double value, double reference, double weight,  FileWriter writer) throws IOException{

	     double j = Math.log(value)/Math.log(reference);
	     j = round2(j);
	     writer.append(j +"\t");
	     val.add(j*weight);   
		 nm.updateglobal(ma, lis, (double)value);
	}
	
	public static void writeitauthor(SortedMap<Double,Double> ma, List<Double> lis, List<Double> val, naivemethod nm, double value,  double reference, double weight, FileWriter writer) throws IOException {

	     double j = Math.log(value)/Math.log(reference);
	     j = round2(j);
	     writer.append(j +"\t");
	     val.add(j*weight); 
	}
	
	public static void main(String[] args) throws NoHeadException, GitAPIException {
		
		//Map<String,Double> mn = new HashMap<String,Double>();
		//Map<String,Double> mn1 = new HashMap<String,Double>();
		
		Settings s = new Settings();
		s.initiate();
		
		try {
	        String Repositoryname = Settings.Repositoryname;
	        String Repositorypath = Settings.Repositorypath;
	        
			File gitDir = new File(Repositorypath);
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
	        writer.append("Time of commit\t");
	        writer.append("Author time quartile?\t");
	       /* writer.append("TOF Changed\t");
	        writer.append("Global TOF Changed Quartile?\t");
	        writer.append("Author TOF Changed Quartile\t");*/
	        writer.append("Decision\t");
	        writer.append("Value\t");
	        writer.append("Comment\n");
	        	        
	        naivemethod nm = new naivemethod();
	        nm.buildglobal();
	        
	        //To count total number of Commits.
	        int count = 0; 
	        
	        //filecount fc = new filecount();
	        //mn = fc.glbfilecount();
	       
	       
	        int total = 0; 
	        for (@SuppressWarnings("unused") RevCommit rev : logs) {
	        	total++;
	        }
	        total = total/2;
	        

	        
	        Iterable<RevCommit> logs1 = new Git(repository).log().all().call();
	        for (RevCommit rev : logs1) {
	    		count++;
	        	/*if(count != 186) {
	        		//System.out.println(count);
	        		continue;
	        	}*/
	        	
	        	if(count > total) {
	        		break;
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
	    	        String tiofcommit = formatter.format(p.getWhen());
	    	        
	    	        writer.append(rev.getName().substring(0,11)+"\t");
	    	        
	    	        String email = p.getEmailAddress();
	    	        if(email.contains("://") || email.contains("//")) {
	    	        	email = email.replace(":", "");
	    	        	email = email.replace("//", "");
	    	        }
	    	        writer.append(email+"\t");

	    	        //System.out.println("Tada");
	    	        
	        		boolean exists = nm.buildupdateauthor(email, rev.getName().substring(0,11), tiofcommit, totallinechanged, ovllineadd, ovllinerem, filetypes.size(), totalfiladd, totalfilrem);
	        		    
	    	        List<Double> val = new ArrayList<Double>();
	    	        
	    	       
	    	        
	    	        Parsestats ps = new Parsestats();
	    	        ps.calcglb();
	    	        	    	        
	    	        if(exists) {
	    	        	exists = ps.calcauth(email);
	    	        }
	    	        
	    	        //System.out.println("Com");
	    	        
	        		writer.append( totallinechanged + "\t" ); 
	    	        writeit(naivemethod.totallocmap, naivemethod.totalloc, val, nm, totallinechanged+1, Parsestats.glbloc90, 0.2, writer);

	    	        if(exists) {
	    	        	writeitauthor(naivemethod.authtotallocmap, naivemethod.authtotalloc, val, nm, totallinechanged+1, Parsestats.authloc90, 0.05, writer);
	    	        }else {
	    	        	writer.append("-\t");
	    	        }
	    	        
	    	        writer.append( ovllineadd + "\t" ); 
	    	        writeit(naivemethod.lineaddmap, naivemethod.lineadd, val, nm, ovllineadd+1, Parsestats.glblocadd90, 0.1, writer);
	    	        if(exists) {	
	    	        	writeitauthor(naivemethod.authlineaddmap, naivemethod.authlineadd, val, nm, ovllineadd+1, Parsestats.authlocadd90, 0.025, writer);
	        		}else {
	    	        	writer.append("-\t");
	    	        }
	        	
	    	        writer.append( ovllinerem + "\t" ); 
	    	        writeit(naivemethod.lineremmap, naivemethod.linerem, val, nm, ovllinerem+1, Parsestats.glblocrem90, 0.1, writer);
	    	        if(exists) {	
	    	        	writeitauthor(naivemethod.authlineremmap, naivemethod.authlinerem, val, nm, ovllinerem+1, Parsestats.authlocrem90, 0.025, writer);
	        		}else {
	        			writer.append("-\t");
	        		}
	        
	    	        writer.append( filetypes.size() + "\t" );
	    	        writeit(naivemethod.totalfilesmap, naivemethod.totalfiles, val, nm, filetypes.size()+1, Parsestats.glbfile90, 0.2, writer);
	    	        if(exists) {
	    	        	writeitauthor(naivemethod.authtotalfilesmap, naivemethod.authtotalfiles, val, nm, filetypes.size()+1, Parsestats.authfile90, 0.05, writer);
	    	        }else {
	        			writer.append("-\t");
	        		}
	    	        
	    	        writer.append(totalfiladd+"\t");
	    	        if(exists) {
	    	        	writeitauthor(naivemethod.authfileaddmap, naivemethod.authfileadd, val, nm, totalfiladd+1, Parsestats.authfiladd90, 0.1, writer);
	        		}else {
	        			writer.append("-\t");
	        		}
	    	        
	    	        writer.append(totalfilrem+"\t");
	    	        if(exists) {
	    	        	writeitauthor(naivemethod.authfileremmap, naivemethod.authfilerem, val, nm, totalfilrem+1, Parsestats.authfilrem90, 0.1,  writer);
	    	        }else {
	        			writer.append("-\t");
	        		}
	    	        
	        		double tiofcommi = Double.parseDouble(tiofcommit);
	        		writer.append(tiofcommit+"\t");
	    	        writeitauthor(naivemethod.authtimemap, naivemethod.authtime, val, nm, tiofcommi+1, Parsestats.authtime90, 0.05, writer);
	    	        
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
	    	        
	        		System.out.println(count);


	    	        double ff = val.get(0);
	    	        for(double j : val) {
	    	        	ff = aggregate(ff,j);
	    	        }
	        		
	        		if(ff <= 0.8) {
	        			writer.append("Normal"+"\t");
	        			writer.append(ff+"\t");
		        		writer.append("--\n");
	        			normal++;
	        		}else {
	        			writer.append("Anamoly"+"\t");
	        			writer.append(ff+"\t");
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
