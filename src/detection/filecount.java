package detection;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;;

public class filecount {
	public  Map<String, Double> glbfilecount() throws IOException{
		File ficount = new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\Data\\node.git\\Global\\Training_filescount.tsv");
		List<String> lis = FileUtils.readLines(ficount);
		Map<String,Integer> mn = new HashMap<String,Integer>();
		Map<String,Double> mn1 = new HashMap<String,Double>();
		long cnt = 0;
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			mn.put(dat[0], Integer.parseInt(dat[1]));
			cnt += Integer.parseInt(dat[1]);
		}
		
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			mn1.put(dat[0], (Double.parseDouble(dat[1])*100/(double)cnt));
		}
		return mn1;
	}

	public Map<String, Double> authfilecount(String auth_file) throws IOException {
		File ficount = new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\Data\\node.git\\Author\\FilesChanged\\"+auth_file +".tsv");
		Map<String,Integer> mn = new HashMap<String,Integer>();
		Map<String,Double> mn1 = new HashMap<String,Double>();
		if(!ficount.exists()) {
			return mn1;
		}
		List<String> lis = FileUtils.readLines(ficount);
		
		long cnt = 0;
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			mn.put(dat[0], Integer.parseInt(dat[1]));
			cnt += Integer.parseInt(dat[1]);
		}
		
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			mn1.put(dat[0], (Double.parseDouble(dat[1])*100/(double)cnt));
		}
		return mn1;
	}
}
		
	

