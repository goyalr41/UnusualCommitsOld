package detection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import settings.Settings;
import stats.Curvefitting;

public class filetypetest {
	//chance of file changed = percentage of file
	// chance of file in commit = if file in commit cnt++
	// combination of top 10 files
	// combination of top 10 files inclusive
	
	public static Map<String,Double> topcombinations;
	public static Map<String,Double> topcombinationssubset = new HashMap<String,Double>();
	public static Map<String,List<String> > twocombinations = new HashMap<String,List<String>>();
	public static Map<String,List<String> > authtwocombinations = new HashMap<String,List<String>>();
	public static List<String> topfiles;
	public static double totalcommits, authortotalcommits;
	
	public static String filepercent, authfilepercent;
	public static String filepercommit, authfilepercommit;
	public static String filecombfre, authfilecombfre;
	public static Double fileunusualcomb, authfileunusualcomb;
	public static String minpercentfiltyp, authminpercentfiltyp;
	public static String minpercommitfiltyp, authminpercommitfiltyp;
	public static String mincombfrefiltyp, authmincombfrefiltyp;
	public static String unusualcombprobfiltyp, authunusualcombprobfiltyp;

	public static double round2(double d) {
	     return Math.round(d * 10000) / 10000.0;
	}
	
	
	public Map<String,Double> fileperchanged() throws IOException{
        File file_global = new File(Settings.Datapath + Settings.Repositoryname +"//Global//Training_filescount.tsv");
		List<String> lis = FileUtils.readLines(file_global);
		Map<String,Double> mn = new HashMap<String,Double>();
		long cnt = 0;
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			cnt += Integer.parseInt(dat[1]);
		}
		
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			mn.put(dat[0], (Double.parseDouble(dat[1])*100.0/(double)cnt));
		}
		return mn;
	}
	
	public Map<String,Double> authfileperchanged(String email) throws IOException{
        File file_author = new File(Settings.Datapath + Settings.Repositoryname +"//Author//FilesChanged//"+email+".tsv");
		List<String> lis = FileUtils.readLines(file_author);
		Map<String,Double> mn = new HashMap<String,Double>();
		long cnt = 0;
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			cnt += Integer.parseInt(dat[1]);
		}
		
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			mn.put(dat[0], (Double.parseDouble(dat[1])*100.0/(double)cnt));
		}
		return mn;
	}
	
	
	public Map<String,Double> filecommitchanged() throws IOException {
        File file_global_matrix = new File(Settings.Datapath + Settings.Repositoryname +"//Global//Training_filestypescountcommit.tsv");

		List<String> files = FileUtils.readLines(file_global_matrix);
		totalcommits = files.size();
    	
		Map<String,Double> mn = new HashMap<String,Double>();
    	
    	for(String filecount : files) {
    		String[] temp = filecount.split("\t");
    		for(int i = 1; i < temp.length; i++) {
    				String[] temp1 = temp[i].split(",");
    				if(mn.containsKey(temp1[0])) {
    					mn.put(temp1[0],mn.get(temp1[0])+1.0);
    				}else {
    					mn.put(temp1[0],1.0);
    				}			
    		}	
        }
    	
    	
		return mn;	
	}
	

	public Map<String,Double> authfilecommitchanged(String email) throws IOException {
        File file_author_matrix = new File(Settings.Datapath + Settings.Repositoryname +"//Author//FilesChanged//"+email+"_filecount.tsv");

		List<String> files = FileUtils.readLines(file_author_matrix);
		authortotalcommits = files.size();
    	
		Map<String,Double> mn = new HashMap<String,Double>();
    	
    	for(String filecount : files) {
    		String[] temp = filecount.split("\t");
    		for(int i = 1; i < temp.length; i++) {
    				String[] temp1 = temp[i].split(",");
    				if(mn.containsKey(temp1[0])) {
    					mn.put(temp1[0],mn.get(temp1[0])+1.0);
    				}else {
    					mn.put(temp1[0],1.0);
    				}			
    		}	
        }
    	
    	
		return mn;	
	}
	
	public void filecombinations() throws IOException {
		twocombinations.clear();
		File file_global_matrix = new File(Settings.Datapath + Settings.Repositoryname +"//Global//Training_filestypescountcommit.tsv");

		List<String> files = FileUtils.readLines(file_global_matrix);
		totalcommits = files.size();
    	
		SortedMap<String,String> mn = new TreeMap<String,String>();
    			
    	for(String filecount : files) {
    		String[] temp = filecount.split("\t");
    		SortedMap<String,String> tempmap = new TreeMap<String,String>();
    		List<String> templist = new ArrayList<>();
    		for(int i = 1; i < temp.length; i++) { //0 being the commit id
    				String[] temp1 = temp[i].split(",");
    				tempmap.put(temp1[0],(temp1[1]));
    				templist.add(temp1[0]);
    		}	
    		Collections.sort(templist);
    	    		
	    	for(int s = 0; s + 1 < templist.size(); s++) {
		    	for(int u = s + 1; u < templist.size(); u++) {
		    		String combkey = templist.get(s) + "," + templist.get(u);
		    		if(twocombinations.containsKey(combkey)){
		    			List<String> temp1 = twocombinations.get(combkey);
		    			temp1.add(tempmap.get(templist.get(s)) + "," + tempmap.get(templist.get(u)));
		    			twocombinations.put(combkey,temp1);
		    		}else {
		    			List<String> temp1 = new ArrayList<String>();
		    			temp1.add(tempmap.get(templist.get(s)) + "," + tempmap.get(templist.get(u)));
		    			twocombinations.put(combkey,temp1);		    		
		    		}
		    	}
    		}
    		
        }
    	
    	
    	for(String comb: twocombinations.keySet()) {
    		try {
    		String[] temp = comb.split(",");
    		//System.out.println(temp[0]);
    		
    		List<String> listem = twocombinations.get(comb);
    		
    		if(listem.size() < 20) {
    			continue;
    		}
    		
    		File twocombcount = new File(Settings.Datapath + Settings.Repositoryname +"//Global//FileCombinations//"+ temp[0] + temp[1]+".tsv");
    		twocombcount.getParentFile().mkdirs();
    		FileWriter writ = new FileWriter(twocombcount);
    		//writ.append(temp[0] + "\t" + temp[1] + "\n");
    		
    		for(String num: listem){
    			temp = num.split(",");
    			writ.append(temp[0] + "\t" + temp[1] +"\n");
    		}
    		
    		writ.flush();
    		writ.close();
    		}catch(Exception e){
    			
    		}
    		
    	}
	}
	
	
	public void authfilecombinations(String email) throws IOException {
		authtwocombinations.clear();
        File file_author_matrix = new File(Settings.Datapath + Settings.Repositoryname +"//Author//FilesChanged//"+email+"_filecount.tsv");

		List<String> files = FileUtils.readLines(file_author_matrix);
		authortotalcommits = files.size();
    	
		SortedMap<String,String> mn = new TreeMap<String,String>();
    
    	for(String filecount : files) {
    		String[] temp = filecount.split("\t");
    		SortedMap<String,String> tempmap = new TreeMap<String,String>();
    		List<String> templist = new ArrayList<>();
    		for(int i = 1; i < temp.length; i++) {
    				String[] temp1 = temp[i].split(",");
    				tempmap.put(temp1[0],(temp1[1]));
    				templist.add(temp1[0]);
    		}	
    		Collections.sort(templist);
    	    		
	    	for(int s = 0; s + 1 < templist.size(); s++) {
		    	for(int u = s + 1; u < templist.size(); u++) {
		    		String combkey = templist.get(s) + "," + templist.get(u);
		    		if(authtwocombinations.containsKey(combkey)){
		    			List<String> temp1 = authtwocombinations.get(combkey);
		    			temp1.add(tempmap.get(templist.get(s)) + "," + tempmap.get(templist.get(u)));
		    			authtwocombinations.put(combkey,temp1);
		    		}else {
		    			List<String> temp1 = new ArrayList<String>();
		    			temp1.add(tempmap.get(templist.get(s)) + "," + tempmap.get(templist.get(u)));
		    			authtwocombinations.put(combkey,temp1);		    		
		    		}
		    	}
    		}
    		
        }
    	
    	
    	for(String comb: authtwocombinations.keySet()) {
    		try {
    		String[] temp = comb.split(",");
    		//System.out.println(temp[0]);
    		
    		List<String> listem = authtwocombinations.get(comb);
    		
    		if(listem.size() < 10) {
    			continue;
    		}
    		
    		File twocombcount = new File(Settings.Datapath + Settings.Repositoryname +"//Author//FileCombinations//"+ email + "//" + temp[0] + temp[1]+".tsv");
    		twocombcount.getParentFile().mkdirs();
    		FileWriter writ = new FileWriter(twocombcount);
    		//writ.append(temp[0] + "\t" + temp[1] + "\n");
    		
    		for(String num: listem){
    			temp = num.split(",");
    			writ.append(temp[0] + "\t" + temp[1] +"\n");
    		}
    		
    		writ.flush();
    		writ.close();
    		}catch(Exception e){
    			
    		}
    		
    	}
	}
	
	/*public Map<String,Double> topfilecombinations() throws IOException{
		File file_global = new File(Settings.Datapath + Settings.Repositoryname +"//Global//Training_filescount.tsv");
		List<String> lis = FileUtils.readLines(file_global);
		topcombinations = new HashMap<String,Double>();
		topcombinationssubset = new HashMap<String,Double>();

		topfiles = new ArrayList<String> ();
		long cnt = 0;
		int i = 0;
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			if(i < 15) {
				topfiles.add(dat[0]);
			}else {
				break;
			}
			i++;
		}
		
		 File file_global_matrix = new File(Settings.Datapath + Settings.Repositoryname +"//Global//Training_filescount_matrix.tsv");

			List<String> files = FileUtils.readLines(file_global_matrix);
	    	String fileextension = files.get(0);
	    	files.remove(0);
	    	String[] extensions = fileextension.split("\t");
	    		    	
	    	for(String filecount : files) {
	    		List<String> combinations = new ArrayList<String>();
	    		String[] temp = filecount.split("\t");
	    		int j = 0;
	    		for(i = 1; i < extensions.length; i++) {
	    			if(topfiles.contains(extensions[i]) && Long.parseLong(temp[i]) != 0 && j < 15) {
	    				combinations.add(extensions[i]);
	    				j++;
	    			}else{
	    				if(!combinations.contains("others")){
	    					combinations.add("others");
	    				}
	    			}
	        	}
	    		Collections.sort(combinations);
	    		
	    		String combkey = "";
	    		for(String h: combinations){
	    			combkey = combkey + h;
	    		}
	    		
	    		if(topcombinations.containsKey(combkey)){
	    			topcombinations.put(combkey, topcombinations.get(combkey) + 1.0);
	    		}else {
	    			topcombinations.put(combkey, 1.0);
	    		}
	    		
	    		for(int t = 0; t < combinations.size(); t++) {
		    		for(int s = 0; s + t < combinations.size(); s++) {
		    			combkey = "";
			    		for(int u = s; u <= s + t; u++) {
			    			combkey = combkey + combinations.get(u);
			    		}
			    		if(topcombinationssubset.containsKey(combkey)){
			    			topcombinationssubset.put(combkey, topcombinationssubset.get(combkey) + 1.0);
			    		}else {
			    			topcombinationssubset.put(combkey, 1.0);
			    		}
		    		}
	    		}
	    	}
	    	
			return topcombinationssubset;	
	}*/
	
	public void calculateglb(List<String> filetypes) throws IOException{
		
		filepercent = "NA";
		filepercommit = "NA"; 
		filecombfre = "NA";
		minpercentfiltyp = "NA";
		minpercommitfiltyp = "NA";
		mincombfrefiltyp = "NA";
		unusualcombprobfiltyp = "NA";
		
		filetypetest fs= new filetypetest();
		fs.filecombinations(); //Two combinations
		Map<String,Double> percommitmap = fs.filecommitchanged();
		Map<String,Double> percentmap = fs.fileperchanged();
		Curvefitting.combinationgraphglobal();	
		
    	Map<String,Long> filecounts = new HashMap<>();
    	//This is for current commit only
    	for(String filetype : filetypes) {
    		if(filecounts.containsKey(filetype)) {
    			filecounts.put(filetype, filecounts.get(filetype) + 1);
    		}else {
    			filecounts.put(filetype, (long)1);
    		}    		
    	}
    	
		List<String> combinations = new ArrayList<String>();
		
    	for(String filetyp: filecounts.keySet()){
    			combinations.add(filetyp);
    	}
    	
    	Collections.sort(combinations);
    	
    	double minpercent = 100.0;
    	double minpercommit = 1.0;
    	for(String filetype: filecounts.keySet()){
    		if(percentmap.containsKey(filetype)){
    			if(minpercent > Math.min(percentmap.get(filetype), minpercent)) {
    				minpercent = Math.min(percentmap.get(filetype), minpercent);
    				minpercentfiltyp = filetype;
    			}
    		}else {
    			minpercent  = 0.0;
    			minpercentfiltyp = filetype;
    		}
    		if(percommitmap.containsKey(filetype)){
    			if(minpercommit > Math.min(percommitmap.get(filetype)/totalcommits,minpercommit)) {
    				minpercommit = Math.min(percommitmap.get(filetype)/totalcommits,minpercommit);
    				minpercommitfiltyp = filetype;
    			}
    		}else {
    			minpercommit = 0.0;
    			minpercommitfiltyp = filetype;

    		}
    	}
    	
    	double combfre = 1.0;
    	double unusualcombprob = 1.0;
    	
    	for(int s = 0; s + 1 < combinations.size(); s++) {
	    	for(int u = s + 1; u < combinations.size(); u++) {
	    		String combkey = combinations.get(s) + "," + combinations.get(u);
	    		if(twocombinations.containsKey(combkey)){
	    			List<String> temp1 = twocombinations.get(combkey);
	    			if(combfre > Math.min(combfre,((double)temp1.size())/totalcommits)) {
	    				combfre = Math.min(combfre,((double)temp1.size())/totalcommits);
	    				mincombfrefiltyp = combkey;
	    			}
	    		}else {
	    			combfre = 0.0;	 
	    			mincombfrefiltyp = combkey;
	    		}
	    		
	    		//System.out.println(Curvefitting.meanmap);
	    		//System.out.println(Curvefitting.sdtmap);
	    		
	    		if(Curvefitting.meanmap.containsKey(combkey) && Curvefitting.sdtmap.containsKey(combkey)) {
	    			double prob;
	    			double chck = Math.log10(filecounts.get(s)/filecounts.get(u));
	    			if(chck-Curvefitting.meanmap.get(combkey) != 0) {
	    				prob = (Math.pow(Curvefitting.sdtmap.get(combkey),2.0))/(Math.pow((chck-Curvefitting.meanmap.get(combkey)), 2.0));
	    			}else {
	    				prob = 1.0;
	    			}
	    			
	    			if(unusualcombprob >= prob) {
	    				unusualcombprob = prob;
	    				unusualcombprobfiltyp = combkey;
	    			}
	    		}
	    	}
		}
    	
    	
    	minpercent = round2(minpercent);
    	minpercommit = round2(minpercommit);
    	combfre = round2(combfre);
    	unusualcombprob = round2(unusualcombprob);
    	
    	
		if(minpercent < 0.02){
			filepercent = minpercent  + " , " + 0.995;
		}else {
			filepercent = minpercent  + " , " + 0;
		}
		
		if(minpercommit < 0.001){
			filepercommit = minpercommit + " , " + 0.995;
		}else {
			filepercommit = minpercommit + " , " + 0;
		}
		
		if(combfre < 0.001) {
			filecombfre = combfre + " , " + 0.995;
		}else {
			filecombfre = combfre + " , " + 0;
		}
		
		fileunusualcomb = unusualcombprob;
		//return (Double) null;
	}
	
	public void calculateauth(List<String> filetypes, String email) throws IOException{
		
		authfilepercent = "NA";
		authfilepercommit = "NA";
		authfilecombfre = "NA";
		authminpercentfiltyp = "NA";
		authminpercommitfiltyp = "NA";
		authmincombfrefiltyp = "NA";
		authunusualcombprobfiltyp = "NA";
		
		authfilecombinations(email);
		Map<String,Double> percommitmap = authfilecommitchanged(email);
		Map<String,Double> percentmap = authfileperchanged(email);
		Curvefitting.combinationgraphauthor(email);	
		
    	Map<String,Long> filecounts = new HashMap<>();
    	//This is for current commit only
    	for(String filetype : filetypes) {
    		if(filecounts.containsKey(filetype)) {
    			filecounts.put(filetype, filecounts.get(filetype) + 1);
    		}else {
    			filecounts.put(filetype, (long)1);
    		}    		
    	}
    	
		List<String> combinations = new ArrayList<String>();
		
    	for(String filetyp: filecounts.keySet()){
    			combinations.add(filetyp);
    	}
    	
    	Collections.sort(combinations);
    	
    	double minpercent = 100.0;
    	double minpercommit = 1.0;
    	for(String filetype: filecounts.keySet()){
    		if(percentmap.containsKey(filetype)){
    			if(minpercent > Math.min(percentmap.get(filetype), minpercent)) {
    				minpercent = Math.min(percentmap.get(filetype), minpercent);
    				authminpercentfiltyp = filetype;
    			}
    		}else {
    			minpercent  = 0.0;
    			authminpercentfiltyp = filetype;
    		}
    		if(percommitmap.containsKey(filetype)){
    			if(minpercommit > Math.min(percommitmap.get(filetype)/authortotalcommits,minpercommit)) {
    				minpercommit = Math.min(percommitmap.get(filetype)/authortotalcommits,minpercommit);
    				authminpercommitfiltyp = filetype;
    			}
    		}else {
    			minpercommit = 0.0;
    			authminpercommitfiltyp = filetype;

    		}
    	}
    	
    	double combfre = 1.0;
    	double unusualcombprob = 1.0;
    	
    	for(int s = 0; s + 1 < combinations.size(); s++) {
	    	for(int u = s + 1; u < combinations.size(); u++) {
	    		String combkey = combinations.get(s) + "," + combinations.get(u);
	    		if(authtwocombinations.containsKey(combkey)){
	    			List<String> temp1 = authtwocombinations.get(combkey);
	    			if(combfre > Math.min(combfre,((double)temp1.size())/authortotalcommits)) {
	    				combfre = Math.min(combfre,((double)temp1.size())/authortotalcommits);
	    				authmincombfrefiltyp = combkey;
	    			}
	    		}else {
	    			combfre = 0.0;	 
	    			authmincombfrefiltyp = combkey;
	    		}
	    		
	    		//System.out.println(Curvefitting.authormeanmap);
	    		//System.out.println(Curvefitting.authorsdtmap);
	    		
	    		if(Curvefitting.authormeanmap.containsKey(combkey) && Curvefitting.authorsdtmap.containsKey(combkey)) {
	    			double prob;
	    			double chck = Math.log1p(filecounts.get(s)/filecounts.get(u));
	    			if(chck-Curvefitting.authormeanmap.get(combkey) != 0) {
	    				prob = (Math.pow(Curvefitting.authorsdtmap.get(combkey),2.0))/(Math.pow((chck-Curvefitting.authormeanmap.get(combkey)), 2.0)); //Chebyshev's Inequality
	    			}else {
	    				prob = 1.0;
	    			}
	    			
	    			if(unusualcombprob >= prob) {
	    				unusualcombprob = prob;
	    				authunusualcombprobfiltyp = combkey;
	    			}
	    		}
	    	}
		}
    	
    	
    	minpercent = round2(minpercent);
    	minpercommit = round2(minpercommit);
    	combfre = round2(combfre);
    	unusualcombprob = round2(unusualcombprob);
    	
		if(minpercent < 0.02){
			authfilepercent = minpercent  + " , " + 0.995;
		}else {
			authfilepercent = minpercent  + " , " + 0;
		}
		
		if(minpercommit < 0.01){
			authfilepercommit = minpercommit + " , " + 0.995;
		}else {
			authfilepercommit = minpercommit + " , " + 0;
		}
		
		if(combfre < 0.01) {
			authfilecombfre = combfre + " , " + 0.995;
		}else {
			authfilecombfre = combfre + " , " + 0;
		}
		
		authfileunusualcomb = unusualcombprob;
		//return (Double) null;
	}
	
	
	public static void main(String[] args){
		Settings s = new Settings();
		Settings.Repositorypath = "C://Users//Raman Workstation//Documents//GitHub//node.git";
		Settings.Repositoryname = "Node3";
		Settings.owner = "joyent";
		Settings.repo = "node";
		s.initiate();
		
		filetypetest fs= new filetypetest();
		/*File f = new File("E://Temp//combinationssubset.tsv");
		FileWriter writer = new FileWriter(f);
		Map<String,Double> m = MapUtil.sortByValue(fs.topfilecombinations());
		System.out.println(m);
		
		for(String str: m.keySet()){
			writer.append(str + "\t" + m.get(str)  + "\n");
		}
		writer.flush();
		writer.close();
		
		f = new File("E://Temp//combinations.tsv");
		writer = new FileWriter(f);
		m = MapUtil.sortByValue(topcombinations);
		System.out.println(m);
		
		for(String str: m.keySet()){
			writer.append(str + "\t" + m.get(str)  + "\n");
		}
		writer.flush();
		writer.close();
		
		f = new File("E://Temp//filepercommit.tsv");
		writer = new FileWriter(f);
		m = MapUtil.sortByValue(fs.filecommitchanged());
		System.out.println(m);
		
		for(String str: m.keySet()){
			writer.append(str + "\t" + m.get(str)  + "\n");
		}
		writer.flush();
		writer.close();
		
		f = new File("E://Temp//filepercentage.tsv");
		writer = new FileWriter(f);
		m = MapUtil.sortByValue(fs.fileperchanged());
		System.out.println(m);
		
		for(String str: m.keySet()){
			writer.append(str + "\t" + m.get(str) + "\n");
		}
		writer.flush();
		writer.close();*/
		//fs.filecombinations();
		List<String> lis = new ArrayList<>();
		lis.add(".js");
		lis.add(".c");
		lis.add(".c");
		try {
			fs.calculateglb(lis);
			fs.calculateauth(lis, "bertbelder@gmail.com");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(fs.twocombinations);
		//System.out.println(fs.filecombfre);
		//System.out.println(Double.parseDouble(" 0.007 "));
		

	}
	
}
