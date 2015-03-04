package detection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import settings.Settings;

public class naivemethod {
	
	public static SortedMap<Double,Double> totallocmap = new TreeMap<>();
	public static SortedMap<Double,Double> lineaddmap = new TreeMap<>();
	public static SortedMap<Double,Double> lineremmap = new TreeMap<>();
	public static SortedMap<Double,Double> totalfilesmap = new TreeMap<>();
	//public static SortedMap<Double,Double> fileaddmap = new TreeMap<>();
	//public static SortedMap<Double,Double> fileremmap = new TreeMap<>();
	
	public static SortedMap<Double,Double> authtimemap = new TreeMap<>();
	public static SortedMap<Double,Double> authtotallocmap = new TreeMap<>();
	public static SortedMap<Double,Double> authtotalfilesmap = new TreeMap<>();
	public static SortedMap<Double,Double> authlineaddmap = new TreeMap<>();
	public static SortedMap<Double,Double> authlineremmap = new TreeMap<>();
	public static SortedMap<Double,Double> authfileaddmap = new TreeMap<>();
	public static SortedMap<Double,Double> authfileremmap = new TreeMap<>();
	
	public static List<Double> totalloc = new ArrayList<Double>();
	public static List<Double> lineadd = new ArrayList<Double>();
	public static List<Double> linerem = new ArrayList<Double>();
	public static List<Double> totalfiles = new ArrayList<Double>();
	//public static List<Double> fileadd = new ArrayList<Double>();
	//public static List<Double> filerem = new ArrayList<Double>();
	
	public static List<Double> authtime = new ArrayList<Double>();
	public static List<Double> authtotalloc = new ArrayList<Double>();
	public static List<Double> authlineadd = new ArrayList<Double>();
	public static List<Double> authlinerem = new ArrayList<Double>();
	public static List<Double> authtotalfiles = new ArrayList<Double>();
	public static List<Double> authfileadd = new ArrayList<Double>();
	public static List<Double> authfilerem = new ArrayList<Double>();

	
	public void map_data(List<Double> lis, SortedMap<Double,Double> ma) {
		for(double d: lis) {
			double p;
			if(ma.containsKey(d)) {
				p = ma.get(d);
				p = p + 1;
			} else {
				p = 1;
			}
			ma.put(d,p);
		}
		
		double sum = 0;
		
		for(double d: ma.keySet()) {
			sum += ma.get(d);
		}
		
		double h = ma.lastKey();
		
		for(double i = 0; i <= h; i++) {
			if(ma.containsKey(i)) {
				double x = ma.get(i);
				ma.put(i, sum);
				sum -= x;
			}else {
				ma.put(i, sum);
			}
		}
		//System.out.println(ma.get(23.0));
	}
	
	public void updateglobal(SortedMap<Double,Double> ma, List<Double> lis, double val) {
		double p = ma.lastKey();
		if(val <= p) {
			for(double i = 0; i <= val; i++) {
				double g = ma.get(i);
				ma.put(i, g+1);
			}
		}else {
			for(double i = 0; i <= p; i++) {
				double g = ma.get(i);
				ma.put(i, g+1);
			}
			for(double i = p+1; i <= val; i++) {
				ma.put(i, 1.0);
			}
		}
		lis.add(val);
	}
	
	public void buildglobal() throws IOException {
		
		String Repositoryname = Settings.Repositoryname;
		String Datapath = Settings.Datapath;
		File dat = new File(Datapath + Repositoryname + "//Global//Training_data.tsv");
		
		List<String> h = FileUtils.readLines(dat);
		h.remove(0);
		for(String line: h) {
			String[] lines = line.split("\t");
			totalloc.add(Double.parseDouble(lines[2]));
			lineadd.add(Double.parseDouble(lines[3]));
			linerem.add(Double.parseDouble(lines[4]));
			totalfiles.add(Double.parseDouble(lines[5]));
			//fileadd.add(Double.parseDouble(lines[6]));
			//filerem.add(Double.parseDouble(lines[7]));
		}
		map_data(totalloc,totallocmap);
		map_data(lineadd,lineaddmap);
		map_data(linerem,lineremmap);
		map_data(totalfiles,totalfilesmap);
		//map_data(fileadd,fileaddmap);
		//map_data(filerem,fileremmap);
	}
	

	public boolean buildupdateauthor(String email, String commitid, String time, int totallo, int loadd, int lorem, int files, int filesadd, int filesrem) throws IOException {
		
		  authtime.clear();
		  authtotalloc.clear();
		  authlineadd.clear();
		  authlinerem.clear();
		  authtotalfiles.clear();
		  authfileadd.clear();
		  authfilerem.clear();
		  
		  authtimemap.clear();
		  authtotallocmap.clear();
		  authlineaddmap.clear();
		  authlineremmap.clear();
		  authtotalfilesmap.clear();
		  authfileaddmap.clear();
		  authfileremmap.clear();
		  
		  String Repository = Settings.Repositoryname; 
		  String Datapath = Settings.Datapath; 
		  File dat = new File(Datapath + Repository+"//Author//TimeFiles//"+email+".tsv");
		 // dat.getParentFile().mkdirs();
		  //boolean fileexist = true;	
			//System.out.println(email);

		  if(dat.exists()) {
			List<String> h = FileUtils.readLines(dat);
			
			FileWriter writer = new FileWriter(dat,true);
			writer.append(commitid + "\t");
			writer.append(time + "\t");
			writer.append(totallo + "\t");
			writer.append(loadd + "\t");
			writer.append(lorem + "\t");
			writer.append(files + "\t");
			writer.append(filesadd + "\t");
			writer.append(filesrem + "\n");
			writer.flush();
			writer.close();
			//System.out.println(email);
			h.remove(0);
			if(h.size() < 3) {
				return false;
			}
	        //System.out.println("Tada");

			for(String line: h) {
				String[] lines = line.split("\t");
				authtime.add(Double.parseDouble(lines[1]));
				authtotalloc.add(Double.parseDouble(lines[2]));
				authlineadd.add(Double.parseDouble(lines[3]));
				authlinerem.add(Double.parseDouble(lines[4]));
				authtotalfiles.add(Double.parseDouble(lines[5]));
				authfileadd.add(Double.parseDouble(lines[6]));
				authfilerem.add(Double.parseDouble(lines[7]));
			}
			map_data(authtime,authtimemap);
			map_data(authtotalloc,authtotallocmap);
			map_data(authlineadd,authlineaddmap);
			map_data(authlinerem,authlineremmap);
			map_data(authtotalfiles,authtotalfilesmap);
			map_data(authfileadd,authfileaddmap);
			map_data(authfilerem,authfileremmap);
			return true;
		  }else {
			  FileWriter writer = new FileWriter(dat,true);
				writer.append(commitid + "\t");
				writer.append(time + "\t");
				writer.append(totallo + "\t");
				writer.append(loadd + "\t");
				writer.append(lorem + "\t");
				writer.append(files + "\t");
				writer.append(filesadd + "\t");
				writer.append(filesrem + "\n");
				writer.flush();
				writer.close();
		        //System.out.println("Tada1");

			  return false;
		  }
		  
	}
	
	public void meth() throws IOException {
		
	}

}
