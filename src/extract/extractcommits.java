package extract;
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

import detection.Variablescall;

public class extractcommits {
	public static void main(String[] args) throws NoHeadException, GitAPIException {
		
		try {
	        String Repository = Variablescall.Repository;
			
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
	    
	        //TSV File which will be containing extracted data
	        File repo_global = new File("C://Users//Raman Workstation//workspace//UnusualCommits//DataFull//"+Repository+"//Global//Training_data.tsv");
	        repo_global.getParentFile().mkdirs();
	        repo_global.createNewFile();
	        FileWriter writer = new FileWriter(repo_global); 
	        
	        /*File repo_local = new File("C://Users//Raman Workstation//workspace//UnusualCommits/Data//"+Repository+"//Author//Training_data.tsv");
	        repo_global.getParentFile().mkdirs();
	        FileWriter writer = new FileWriter(repo_global); */
	        FileWriter writer1 = new FileWriter("C://Users//Raman Workstation//workspace//UnusualCommits//DataFull//"+Repository+"//Global//Training_filescount.tsv"); 
	       
	        writer.append("Commit Id\t");
	        writer.append("Author E-Mail\t");
	        writer.append("Total LOC affected\t");
	        writer.append("LOC added\t");
	        writer.append("LOC removed\t");
	        writer.append("Nos. of Files affected\t");
	        writer.append("Nos. of Files added\t");
	        writer.append("Nos. of Files removed");
	        
	        writer.append("\n");
	        
	        //To count total number of Commits.
	        int count = 0; 
	        Map<String, Long> filecount = new HashMap<>();
	        Map<String,List<String>> author_time = new HashMap<>();
	        Map<String, Map<String,Long>> author_files = new HashMap<>();
	        
	        int total = 0; 
	        
	        for (RevCommit rev : logs) {
	        	total++;
	        }
	       
	        total = total/2;
	       
	        Iterable<RevCommit> logs1 = new Git(repository).log().all().call();
	        for (RevCommit rev : logs1) {
	    		count++;
	        	if(count <= total) {
	        		//System.out.println(count);
	        		continue;
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
	        		
	        		//DateFormat formatter= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	        		DateFormat formatter= new SimpleDateFormat("HH");
	        		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	        		//System.out.println(formatter.format(p.getWhen()) + " " + p.getWhen());
	        		writer.append(rev.getName().substring(0,11)+"\t");
	        		//writer.append(p.getName()+"\t");
	        		writer.append(p.getEmailAddress()+"\t");
	        		
	        		//writer.append(formatter.format(p.getWhen())+"\t");
	        		writer.append( totallinechanged + "\t" );
	        		writer.append( ovllineadd + "\t" );
	        		writer.append( ovllinerem + "\t" );
	        		writer.append( filetypes.size() + "\t" );
	        		writer.append( totalfiladd +"\t" );
	        		writer.append( totalfilrem + "\t" );
	        		//writer.append( filetypes + "\n" );
	        		writer.append("\n");
	        		//System.out.println("LOC added = " + ovllineadd + "   LOC removed = " +ovllinerem + "   Total LOC = " + totallinechanged + " Filetypes = " + filetypes + " " +  totalfilrem + " " + totalfiladd);
	        		
	        		if(author_time.containsKey(p.getEmailAddress())){
            			List<String> value = author_time.get(p.getEmailAddress());
            			value.add(rev.getName().substring(0,11)+"\t" + formatter.format(p.getWhen()) + "\t"  + totallinechanged + "\t"  + ovllineadd + "\t" + ovllinerem + "\t"  + filetypes.size() + "\t" + totalfiladd + "\t" + totalfilrem );
            			author_time.put(p.getEmailAddress(), value);
            		}else {
            			List<String> value = new ArrayList<>();
            			value.add(rev.getName().substring(0,11)+"\t" + formatter.format(p.getWhen()) +  "\t" + totallinechanged + "\t"  + ovllineadd + "\t"  + ovllinerem + "\t"  + filetypes.size() + "\t" + totalfiladd + "\t" + totalfilrem );
            			author_time.put(p.getEmailAddress(), value);
            		}
	        		
	        		/*if(author_time.containsKey(p.getEmailAddress())){
	            		List<String> value = author_time.get(p.getEmailAddress());
    	                		value.add(formatter.format(p.getWhen()) + "\t" + file);
	            			}else {
	            				value.put(formatter.format(p.getWhen()), (long)1);
	            			}
	            			author_time.put(p.getEmailAddress(), value);	
	            	} else {
	            		Map<String,Long> value = new HashMap<>();
            				if(value.containsKey(formatter.format(p.getWhen()))) {
            					long value1 = value.get(formatter.format(p.getWhen()))+1;
	                			value.put(formatter.format(p.getWhen()), value1);
            				}else {
            					value.put(formatter.format(p.getWhen()), (long)1);
            				}
            			author_time.put(p.getEmailAddress(), value);	
	            	}*/
	        	
	        		
	
	        		if(author_files.containsKey(p.getEmailAddress())){
	            		Map<String,Long> value = author_files.get(p.getEmailAddress());
	            			for(String filtyp: filetypes) {
	            				if(value.containsKey(filtyp)) {
	            					long value1 = value.get(filtyp)+1;
    	                			value.put(filtyp, value1);
	            				}else {
	            					value.put(filtyp, (long)1);
	            				}
	            			}
	            			author_files.put(p.getEmailAddress(), value);	
	            	} else {
	            		Map<String,Long> value = new HashMap<>();
            			for(String filtyp: filetypes) {
            				if(value.containsKey(filtyp)) {
            					long value1 = value.get(filtyp)+1;
	                			value.put(filtyp, value1);
            				}else {
            					value.put(filtyp, (long)1);
            				}
            			}
            			author_files.put(p.getEmailAddress(), value);	
	            	}
	        	} 
	        	
	        	
	        }
		    writer.flush();
		    writer.close();
		    for (String filtyp: filecount.keySet()){
		    	writer1.append(filtyp + "\t" + filecount.get(filtyp) + "\n");
		    }
		    writer1.flush();
		    writer1.close();
		    for (String author_name: author_time.keySet()) {
		    	List<String> value = author_time.get(author_name);
		    	File authtime = new File("C://Users//Raman Workstation//workspace//UnusualCommits//DataFull//"+Repository+"//Author//TimeFiles//"+author_name+".tsv"); 
		    	authtime.getParentFile().mkdirs();
		    	FileWriter writer_authtime = new FileWriter(authtime);
		    
		    	writer_authtime.append("Commit ID \t Time \t LOCChanged \t LOCADD \t LOCRem \t Totalfiles \t Filesadd \t Filesrem \n"); 
		    	for(String time: value){
		    		writer_authtime.append(time + "\n");
		    	}
		    	writer_authtime.flush();
			    writer_authtime.close();
		    }
		    
		    for (String author_name: author_files.keySet()) {
		    	File authfiles = new File("C://Users//Raman Workstation//workspace//UnusualCommits//DataFull//"+Repository+"//Author//FilesChanged//"+author_name+".tsv"); 
		    	authfiles.getParentFile().mkdirs();
		    	FileWriter writer_authfiles = new FileWriter(authfiles); 
		    	Map<String,Long> value = author_files.get(author_name);
		    	for(String files: value.keySet()){
		    		writer_authfiles.append(files + "\t" + value.get(files) + "\n");
		    	}
		    	writer_authfiles.flush();
			    writer_authfiles.close();
		    }
		    
	        System.out.println("Had " + count + " commits overall in repository");
	        System.out.println("Done");
	       
	        repository.close();
		} catch (IOException ex) {
	        // The repository exists, but is inaccessible!
		}
	}
			
}