package html;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class htmldata {
	public static void addheader(StringBuilder sb) throws IOException {
		File header = new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\src\\html\\header.txt");
		String heade = FileUtils.readFileToString(header);
		sb.append(heade); 
	}
	
	public static void addfooter(StringBuilder sb) throws IOException {
		File header = new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\src\\html\\footer.txt");
		String heade = FileUtils.readFileToString(header);
		sb.append(heade); 
	}
	
	public static void addend(StringBuilder sb) throws IOException {
		File header = new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\src\\html\\end.txt");
		String heade = FileUtils.readFileToString(header);
		sb.append(heade); 
	}
	
	public static void addtag(StringBuilder sb, String[] fil) throws IOException {
		File header = new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\src\\html\\data.txt");
		String heade = FileUtils.readFileToString(header);
		heade = heade.replace("$author", "joyent");
		heade = heade.replace("$repository", "node");
		int i = 0;
		heade = heade.replace("$id", fil[i++]);
		heade = heade.replace("$email",fil[i++]);
		heade = heade.replace("$locvalue",fil[i++]);
		heade = heade.replace("$locglb",fil[i++]);
		heade = heade.replace("$locauth",fil[i++]);
		heade = heade.replace("$locaddvalue",fil[i++]);
		heade = heade.replace("$locaddglb",fil[i++]);
		heade = heade.replace("$locaddauth",fil[i++]);
		heade = heade.replace("$locremvalue",fil[i++]);
		heade = heade.replace("$locremglb",fil[i++]);
		heade = heade.replace("$locremauth",fil[i++]);
		heade = heade.replace("$nofvalue",fil[i++]);
		heade = heade.replace("$nofglb",fil[i++]);
		heade = heade.replace("$nofauth",fil[i++]);
		heade = heade.replace("$nofaddvalue",fil[i++]);
		heade = heade.replace("$nofaddauth",fil[i++]);
		heade = heade.replace("$nofremvalue",fil[i++]);
		heade = heade.replace("$nofremauth",fil[i++]);
		//heade = heade.replace("$tofvalue",fil[i++]);
		//heade = heade.replace("$tofglb",fil[i++]);
		//heade = heade.replace("$tofauth",fil[i++]);
		heade = heade.replace("$tocvalue",fil[i++]);
		heade = heade.replace("$tocauth",fil[i++]);
		heade = heade.replace("$decision",fil[i++]);
		heade = heade.replace("$decvalue",fil[i++]);
		heade = heade.replace("$comment",fil[i++]);
		
		sb.append(heade); 
		
	}
	
	public static void call(File data) throws IOException {
		StringBuilder sb = new StringBuilder();
		addheader(sb);
		List<String> h = FileUtils.readLines(data);
		h.remove(0);
		/*for(String dat: h) {
			System.out.println(dat);
		}*/
		for(String dat: h) {
			String[] fil = dat.split("\t");
			addtag(sb,fil);
		}
		addfooter(sb);
		addend(sb);
		File HtmlFile = new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\src\\html\\htmlfile.html");
		FileUtils.writeStringToFile(HtmlFile, sb.toString());
	}
	
	public static void main(String[] args) throws IOException {
		File res = new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\Results\\node.git\\result.tsv");
		call(res);
		/*StringBuilder sb = new StringBuilder();
		addheader(sb);
		String[] fil = new String[26];
		for(int i = 0; i < 26; i++) {
			fil[i] = "check";
		}
		addtag(sb,fil);
		addtag(sb,fil);
		addfooter(sb);
		addend(sb);
		File HtmlFile = new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\src\\html\\htmlfile.html");
		FileUtils.writeStringToFile(HtmlFile, sb.toString());*/
		
	}
	
}