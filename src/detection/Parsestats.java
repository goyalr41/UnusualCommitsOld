package detection;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import settings.Settings;

public class Parsestats {
	public static double glbloc90 = 0.0;
	public static double glblocadd90 = 0.0;
	public static double glblocrem90 = 0.0;
	public static double glbfile90 = 0.0;
	public static double glbfiladd90 = 0.0;
	public static double glbfilrem90 = 0.0;
	
	public static double authtime90 = 0.0;
	public static double authloc90 = 0.0;
	public static double authlocadd90 = 0.0;
	public static double authlocrem90 = 0.0;
	public static double authfile90 = 0.0;
	public static double authfiladd90 = 0.0;
	public static double authfilrem90 = 0.0;
	
	public void calcglb() throws IOException {
		File dat = new File(Settings.Statspath + Settings.Repositoryname +"//Global//quartile.txt");
		List<String> h = FileUtils.readLines(dat);
		
		String temp = h.get(1);
		String[] temp1 = temp.split(",");
		glbloc90 = Double.parseDouble(temp1[6]);
		
		temp = h.get(4);
		temp1 = temp.split(",");
		glblocadd90 =  Double.parseDouble(temp1[6]);
		
		temp = h.get(7);
		temp1 = temp.split(",");
		glblocrem90 =  Double.parseDouble(temp1[6]);
		
		temp = h.get(10);
		temp1 = temp.split(",");
		glbfile90 =  Double.parseDouble(temp1[6]);
		
		temp = h.get(13);
		temp1 = temp.split(",");
		glbfiladd90 =  Double.parseDouble(temp1[6]);
		
		temp = h.get(16);
		temp1 = temp.split(",");
		glbfilrem90 =  Double.parseDouble(temp1[6]);		
		
	}
	
	public boolean calcauth(String email) throws IOException {
		File dat = new File(Settings.Statspath + Settings.Repositoryname +"//Author//" + email +".txt");
		if(!dat.exists()) {
			return false;
		}
		List<String> h = FileUtils.readLines(dat);
		
		String temp = h.get(1);
		String[] temp1 = temp.split(",");
		authtime90 = Double.parseDouble(temp1[6]);
		
		temp = h.get(4);
		temp1 = temp.split(",");
		authloc90 = Double.parseDouble(temp1[6]);
		
		temp = h.get(7);
		temp1 = temp.split(",");
		authlocadd90 =  Double.parseDouble(temp1[6]);
		
		temp = h.get(10);
		temp1 = temp.split(",");
		authlocrem90 =  Double.parseDouble(temp1[6]);
		
		temp = h.get(13);
		temp1 = temp.split(",");
		authfile90 =  Double.parseDouble(temp1[6]);
		
		temp = h.get(16);
		temp1 = temp.split(",");
		authfiladd90 =  Double.parseDouble(temp1[6]);
		
		temp = h.get(19);
		temp1 = temp.split(",");
		authfilrem90 =  Double.parseDouble(temp1[6]);	
		
		return true;
		
	}
	
	
}
