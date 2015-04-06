package extract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import main.maincla;

import org.apache.commons.io.FileUtils;

import detection.MapUtil;
import settings.Settings;

public class writedata {
	
	public static FileWriter writer;
	public static FileWriter writer1, writer2;
	public static List<List<String>> Filetypeslist;
	public static List<String> commits;
	public static List<String> emails;
	public static String Repositoryname;
	
	public void writeforfiles(String commitid, String email, List<String> filetypes) throws IOException {
	       File file_global = new File(Settings.Datapath + Repositoryname +"//Global//Training_filescount.tsv");
	       File file_global_matrix = new File(Settings.Datapath + Repositoryname +"//Global//Training_filestypescountcommit.tsv");
	  
	       Map<String,Long> mapfilecount = new HashMap<String,Long>();
	        	        
	        if(file_global.exists()) {
	        	List<String> files = FileUtils.readLines(file_global);
	        	for(String filetype : files) {
	        		String[] temp = filetype.split("\t");
	        		mapfilecount.put(temp[0], Long.parseLong(temp[1]));
	        	}
	        }
	        
	     	for(String filetype : filetypes) {
	    		if(mapfilecount.containsKey(filetype)) {
	    			mapfilecount.put(filetype, mapfilecount.get(filetype) + 1);
	    		}else {
	    			mapfilecount.put(filetype, (long)1);
	    		}    		
	    	}
	        
	        mapfilecount = MapUtil.sortByValue(mapfilecount);
	        
	        writer1 = new FileWriter(file_global); 
	        
	        for (String filtype : mapfilecount.keySet()){
		    	writer1.append(filtype + "\t" + mapfilecount.get(filtype) + "\n");
		    }
	        
	        writer1.flush();
	        writer1.close();
	        
	        Map<String,Long> filecounts = new HashMap<>();
	    	//This is for current commit only
	    	for(String filetype : filetypes) {
	    		if(filecounts.containsKey(filetype)) {
	    			filecounts.put(filetype, filecounts.get(filetype) + 1);
	    		}else {
	    			filecounts.put(filetype, (long)1);
	    		}    		
	    	}
	    	
	    	writer2 = new FileWriter(file_global_matrix, true);
	    	
	    	writer2.append(commitid);
	    	for(String filetype: filecounts.keySet()){
	    		writer2.append("\t" + filetype + "," + filecounts.get(filetype));
	    	}
	    	writer2.append("\n");
	    	
	    	writer2.flush();
	    	writer2.close();
	    	
	    	   
	        File authfiles = new File(Settings.Datapath + Repositoryname +"//Author//FilesChanged//" + email +".tsv"); 
	        File auth_file_global_matrix = new File(Settings.Datapath + Repositoryname +"//Author//FilesChanged//" + email + "_filecount.tsv");

	        
	        Map<String,Long> mapauthorfilecount = new HashMap<String,Long>();
		    
		    if(authfiles.exists()) {
		    	List<String> files = FileUtils.readLines(file_global);
		    	
		    	for(String filetype : files) {
		    		String[] temp = filetype.split("\t");
		    		mapauthorfilecount.put(temp[0], Long.parseLong(temp[1]));
		    	}
		    }
		    
		    for(String filetype : filetypes) {
	    		if(mapauthorfilecount.containsKey(filetype)) {
	    			mapauthorfilecount.put(filetype, mapauthorfilecount.get(filetype) + 1);
	    		}else {
	    			mapauthorfilecount.put(filetype, (long)1);
	    		}
	    		
	    	}
		    
		    authfiles.getParentFile().mkdirs();
			FileWriter writer_authfiles = new FileWriter(authfiles); 
			FileWriter authwriter2 = new FileWriter(auth_file_global_matrix, true);
	        mapauthorfilecount = MapUtil.sortByValue(mapauthorfilecount);    	

	        for (String filtype : mapauthorfilecount.keySet()){
		    	writer_authfiles.append(filtype + "\t" + mapauthorfilecount.get(filtype) + "\n");
		    }
	       
	        writer_authfiles.flush();
			writer_authfiles.close();
				    	
	    	authwriter2.append(commitid);
	    	for(String filetype: filecounts.keySet()){
	    		authwriter2.append("\t" + filetype + "," + filecounts.get(filetype));
	    	}
	    	authwriter2.append("\n");
	    	
	    	authwriter2.flush();
	    	authwriter2.close();
			
	     
	        
	}
	
	public void writematrix(String commitid, String email, List<String> filetypes) throws IOException{
		
	       File file_global = new File(Settings.Datapath + Repositoryname +"//Global//Training_filescount.tsv");
	        File file_global_matrix = new File(Settings.Datapath + Repositoryname +"//Global//Training_filescount_matrix.tsv");

	        Map<String,Long> mapfilecount = new HashMap<String,Long>();
	        List<SortedMap<String, Long> > mapfilecountmatrix = new ArrayList<>();
	        
	        List<String> typesallpre = new ArrayList<>(); //Extensions already present
	        List<String> commitids = new ArrayList<>();
	        
	        if(file_global.exists()) {
	        	List<String> files = FileUtils.readLines(file_global);
	        	for(String filetype : files) {
	        		String[] temp = filetype.split("\t");
	        		mapfilecount.put(temp[0], Long.parseLong(temp[1]));
	        	}
	        }
	        
	        if(file_global_matrix.exists()) {
	        	List<String> files = FileUtils.readLines(file_global_matrix);
	        	String fileextension = files.get(0);
	        	files.remove(0);
	        	String[] extensions = fileextension.split("\t");
	        	
	        	for(int i = 1; i < extensions.length; i++) {
	        		typesallpre.add(extensions[i]);
	        	}
	        	
	        	
	        	for(String filecount : files) {
	            	SortedMap<String, Long> smtemp = new TreeMap<>();
	        		String[] temp = filecount.split("\t");
	        		commitids.add(temp[0]);
	        		for(int i = 1; i < extensions.length; i++) {
	            		smtemp.put(extensions[i],Long.parseLong(temp[i]));
	            	}
	        		mapfilecountmatrix.add(smtemp);
	        	}
	        }else {
	        	file_global_matrix.createNewFile();
	        }
	        
	    	for(String filetype : filetypes) {
	    		if(mapfilecount.containsKey(filetype)) {
	    			mapfilecount.put(filetype, mapfilecount.get(filetype) + 1);
	    		}else {
	    			mapfilecount.put(filetype, (long)1);
	    		}    		
	    	}
	    	
	    	
	    	Map<String,Long> filecounts = new HashMap<>();
	    	//This is for current commit only
	    	for(String filetype : filetypes) {
	    		if(filecounts.containsKey(filetype)) {
	    			filecounts.put(filetype, filecounts.get(filetype) + 1);
	    		}else {
	    			filecounts.put(filetype, (long)1);
	    		}    		
	    	}
	    	
			SortedMap<String, Long> smtemp = new TreeMap<>();
			
			for(String str: typesallpre){
				smtemp.put(str, (long)0);
			}

	    	for(String filtype : filecounts.keySet()) {
	    		smtemp.put(filtype, filecounts.get(filtype));
	    		
	    		if(!typesallpre.contains(filtype)) {
	    	        List<SortedMap<String, Long> > newmapfilecountmatrix = new ArrayList<>();
	    			typesallpre.add(filtype);
	    			
	    			for(SortedMap<String, Long> smtemp1: mapfilecountmatrix) {
	    				smtemp1.put(filtype, (long)0);
	    				newmapfilecountmatrix.add(smtemp1);
	    			}
	    			
	    			mapfilecountmatrix = newmapfilecountmatrix;
	    		}
	    		
	    	}
	    	
	    	commitids.add(commitid);
			mapfilecountmatrix.add(smtemp);
	        mapfilecount = MapUtil.sortByValue(mapfilecount);
	        
	        writer1 = new FileWriter(file_global); 
	        
	        for (String filtype : mapfilecount.keySet()){
		    	writer1.append(filtype + "\t" + mapfilecount.get(filtype) + "\n");
		    }
	        
	        writer1.flush();
	        writer1.close();
	        
	        writer2 = new FileWriter(file_global_matrix);
	        

	        SortedMap<String, Long> smtem = mapfilecountmatrix.get(0);
			writer2.append("Commit ID" + "\t");
	        for(String filtype: smtem.keySet()) {
	        	if(filtype != smtem.lastKey()) {
			    	writer2.append(filtype + "\t");
				  }else {
				    writer2.append(filtype + "\n");
				  }	
	        }
	        
	        int cnn = 0;
			for(SortedMap<String, Long> smtemp1: mapfilecountmatrix) {
				
				writer2.append(commitids.get(cnn) + "\t");
				cnn++;

				 for (String filtype : smtemp1.keySet()) {
					  if(filtype != smtemp1.lastKey()) {
				    	writer2.append(smtemp1.get(filtype) + "\t");
					  }else {
					    writer2.append(smtemp1.get(filtype) + "\n");
					  }
				 }
				 
		    }
	        
	        writer2.flush();
	        writer2.close();

	        
	        File authfiles = new File(Settings.Datapath + Repositoryname +"//Author//FilesChanged//" + email +".tsv"); 
	        File auth_file_global_matrix = new File(Settings.Datapath + Repositoryname +"//Author//FilesChanged//" + email + "_matrix.tsv");

	        
	        Map<String,Long> mapauthorfilecount = new HashMap<String,Long>();
	        List<SortedMap<String, Long> > mapauthfilecountmatrix = new ArrayList<>();
	        
	        List<String> authtypesallpre = new ArrayList<>(); //Extensions already present
	        List<String> authcommitids = new ArrayList<>();
	        
		    
		    if(authfiles.exists()) {
		    	List<String> files = FileUtils.readLines(file_global);
		    	
		    	for(String filetype : files) {
		    		String[] temp = filetype.split("\t");
		    		mapauthorfilecount.put(temp[0], Long.parseLong(temp[1]));
		    	}
		    }
		    
		    
	        if(auth_file_global_matrix.exists()) {
	        	List<String> files = FileUtils.readLines(auth_file_global_matrix);
	        	String fileextension = files.get(0);
	        	files.remove(0);
	        	String[] extensions = fileextension.split("\t");
	        	
	        	for(int i = 1; i < extensions.length; i++) {
	        		authtypesallpre.add(extensions[i]);
	        	}
	        	
	        	
	        	for(String filecount : files) {
	            	SortedMap<String, Long> authsmtemp = new TreeMap<>();
	        		String[] temp = filecount.split("\t");
	        		commitids.add(temp[0]);
	        		for(int i = 1; i < extensions.length; i++) {
	            		authsmtemp.put(extensions[i],Long.parseLong(temp[i]));
	            	}
	        		mapfilecountmatrix.add(authsmtemp);
	        	}
	        }else {
	        	//auth_file_global_matrix.createNewFile();
	        }
		    
			for(String filetype : filetypes) {
	    		if(mapauthorfilecount.containsKey(filetype)) {
	    			mapauthorfilecount.put(filetype, mapauthorfilecount.get(filetype) + 1);
	    		}else {
	    			mapauthorfilecount.put(filetype, (long)1);
	    		}
	    		
	    	}
			
			SortedMap<String, Long> authsmtemp = new TreeMap<>();
			
			for(String str: authtypesallpre){
				authsmtemp.put(str, (long)0);
			}

	    	for(String filtype : filecounts.keySet()) {
	    		authsmtemp.put(filtype, filecounts.get(filtype));
	    		
	    		if(!authtypesallpre.contains(filtype)) {
	    	        List<SortedMap<String, Long> > newauthmapfilecountmatrix = new ArrayList<>();
	    			authtypesallpre.add(filtype);
	    			
	    			for(SortedMap<String, Long> authsmtemp1: mapauthfilecountmatrix) {
	    				authsmtemp1.put(filtype, (long)0);
	    				newauthmapfilecountmatrix.add(authsmtemp1);
	    			}
	    			
	    			mapauthfilecountmatrix = newauthmapfilecountmatrix;
	    		}
	    		
	    	}
	    	
	    	authcommitids.add(commitid);
			mapauthfilecountmatrix.add(authsmtemp);
	    	
			authfiles.getParentFile().mkdirs();
			FileWriter writer_authfiles = new FileWriter(authfiles); 
			FileWriter authwriter2 = new FileWriter(auth_file_global_matrix);
	        mapauthorfilecount = MapUtil.sortByValue(mapauthorfilecount);    	

	        for (String filtype : mapauthorfilecount.keySet()){
		    	writer_authfiles.append(filtype + "\t" + mapauthorfilecount.get(filtype) + "\n");
		    }
	       
	        writer_authfiles.flush();
			writer_authfiles.close();
			
	        SortedMap<String, Long> authsmtem = mapauthfilecountmatrix.get(0);
			authwriter2.append("Commit ID" + "\t");
	        for(String filtype: authsmtem.keySet()) {
	        	if(filtype != authsmtem.lastKey()) {
			    	authwriter2.append(filtype + "\t");
				  }else {
				    authwriter2.append(filtype + "\n");
				  }	
	        }
	        
	        cnn = 0;
			for(SortedMap<String, Long> smtemp1: mapauthfilecountmatrix) {
				
				authwriter2.append(authcommitids.get(cnn) + "\t");
				cnn++;

				 for (String filtype : smtemp1.keySet()) {
					  if(filtype != smtemp1.lastKey()) {
				    	authwriter2.append(smtemp1.get(filtype) + "\t");
					  }else {
					    authwriter2.append(smtemp1.get(filtype) + "\n");
					  }
				 }
				 
		    }
	        
	        authwriter2.flush();
	        authwriter2.close();
		
	}

	public boolean write(String commitid, String email, int totalloc, int locadd, int locrem, int nof, int nofadd, int nofrem, int timeofcommit, List<String> filetypes, int commsgsize) throws IOException{
		writer.append(commitid + "\t");
		writer.append(email + "\t");
		writer.append(totalloc + "\t");
		writer.append(locadd + "\t");
		writer.append(locrem + "\t");
		writer.append(nof + "\t");
		writer.append(nofadd + "\t");
		writer.append(nofrem + "\t");
		writer.append(commsgsize + "\n");
		
		writer.flush();
		
		boolean sizeauth = false;
		File authtime = new File(Settings.Datapath + Repositoryname +"//Author//TimeFiles//"+ email+".tsv"); 
    	if(authtime.exists()) {
    		FileWriter writer_authtime = new FileWriter(authtime, true);
            List<String> sizech = FileUtils.readLines(authtime);
            if(sizech.size() > 5) {
            	sizeauth = true;
            }
        	writer_authtime.append(commitid + "\t");
            writer_authtime.append(timeofcommit + "\t");
            writer_authtime.append(totalloc + "\t");
            writer_authtime.append(locadd + "\t");
            writer_authtime.append(locrem + "\t");
            writer_authtime.append(nof + "\t");
            writer_authtime.append(nofadd + "\t");
            writer_authtime.append(nofrem + "\t");
    		writer_authtime.append(commsgsize + "\n");
            
            writer_authtime.flush();
    	    writer_authtime.close();
    		
    	}else {
    		authtime.getParentFile().mkdirs();
        	FileWriter writer_authtime = new FileWriter(authtime, true);
        	
        	writer_authtime.append("Commit Id\t");
            writer_authtime.append("Time\t");
            writer_authtime.append("Total LOC\t");
            writer_authtime.append("LOC added\t");
            writer_authtime.append("LOC removed\t");
            writer_authtime.append("Files affected\t");
            writer_authtime.append("Files added\t");
            writer_authtime.append("Files removed\t");
            writer_authtime.append("Commit Msg");
            writer_authtime.append("\n");
            
            writer_authtime.flush();
    	    writer_authtime.close();
    	}
    	
    
    		writeforfiles(commitid, email, filetypes);
    	
		return sizeauth;

	}
	
	public void writerpointer() throws IOException {
		File repo_global = new File(Settings.Datapath + Repositoryname + "//Global//Training_data.tsv");
        repo_global.getParentFile().mkdirs();
        
        writer = new FileWriter(repo_global,true); 
	}
	
	public void init() throws IOException{
		
		Filetypeslist = new ArrayList<>();
		commits = new ArrayList<>();
		emails = new ArrayList<>();
		
        Repositoryname = Settings.Repositoryname;
 	
		File repo_global = new File(Settings.Datapath + Repositoryname + "//Global//Training_data.tsv");
        repo_global.getParentFile().mkdirs();
        
        if(repo_global.exists()) {
        	repo_global.delete();
        }
        
        repo_global.createNewFile();
        writer = new FileWriter(repo_global,true); 
        
        writer.append("Commit Id\t");
        writer.append("Author E-Mail\t");
        writer.append("Total LOC\t");
        writer.append("LOC added\t");
        writer.append("LOC removed\t");
        writer.append("Files affected\t");
        writer.append("Files added\t");
        writer.append("Files removed\t");
        writer.append("Commit Msg");
        writer.append("\n");
        //writer.append("Time of Commit\t");
        //writer.append("Types of File changed");
     
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
