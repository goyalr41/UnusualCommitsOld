package stats;

import java.io.*;
import java.awt.Frame;
import java.awt.FileDialog;

import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RMainLoopCallbacks;

import settings.Settings;

class TextConsole implements RMainLoopCallbacks
{
    public void rWriteConsole(Rengine re, String text, int oType) {
        System.out.print(text);
    }
    
    public void rBusy(Rengine re, int which) {
        System.out.println("rBusy("+which+")");
    }
    
    public String rReadConsole(Rengine re, String prompt, int addToHistory) {
        System.out.print(prompt);
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            String s=br.readLine();
            return (s==null||s.length()==0)?s:s+"\n";
        } catch (Exception e) {
            System.out.println("jriReadConsole exception: "+e.getMessage());
        }
        return null;
    }
    
    public void rShowMessage(Rengine re, String message) {
        System.out.println("rShowMessage \""+message+"\"");
    }
	
    public String rChooseFile(Rengine re, int newFile) {
	FileDialog fd = new FileDialog(new Frame(), (newFile==0)?"Select a file":"Select a new file", (newFile==0)?FileDialog.LOAD:FileDialog.SAVE);
	//fd.show();
	String res=null;
	if (fd.getDirectory()!=null) res=fd.getDirectory();
	if (fd.getFile()!=null) res=(res==null)?fd.getFile():(res+fd.getFile());
	return res;
    }
    
    public void   rFlushConsole (Rengine re) {
    }
	
    public void   rLoadHistory  (Rengine re, String filename) {
    }			
    
    public void   rSaveHistory  (Rengine re, String filename) {
    }			
}

public class Datastatistics {
    public static void main(String[] args) {
    	
	// Making sure we have the right version of everything
	if (!Rengine.versionCheck()) {
	    System.err.println("** Version mismatch - Java files don't match library version.");
	    System.exit(1);
	}
	
	Settings s  = new Settings();
	s.initiate();
	
		Rengine re = new Rengine(args, false, new TextConsole());
        System.out.println("Rengine created, waiting for R");
		// the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            return;
        }
        
        String Repositoryname = Settings.Repositoryname;
		
		try {
			String Datapath = Settings.Datapath;
			String DatapathforR = Datapath.replace("//", "/");
			String Statspath = Settings.Statspath;
			String read1 = "Data <- read.delim(\"" + DatapathforR + Repositoryname + "/Global/Training_data.tsv\")";
	
			//System.out.println(read1);
			
			//String read11 = "Data <- read.delim(\"C:/Users/Raman Workstation/workspace/UnusualCommits/Data/"+Repository+"/Global/Training_filescount.tsv\")";
			
			File dir = new File(Statspath + Repositoryname + "//Global//quartile.txt");
			dir.getParentFile().mkdirs();
			dir.createNewFile();
			
			File dir1 = new File(Statspath + Repositoryname + "//Author//quartile.txt");
			dir1.getParentFile().mkdirs();
			dir1.createNewFile();
			
			String StatspathforR = Statspath.replace("//", "/");
			String write1 = "pdf(\"" + StatspathforR + Repositoryname + "/Global/Training_LOCaFiles.pdf\")";
			//String write11 = "pdf(\"C:/Users/Raman Workstation/workspace/UnusualCommits/Stats/"+Repository+"/Global/Training_TypeFiles.pdf\")";
			
			String write2 = "pdf(\"" + StatspathforR + Repositoryname + "/Author/Training_LOCaFiles.pdf\", height=4, width=5)";
			
			re.eval(read1);
			re.eval(write1);
			re.eval("hist(Data$Total.LOC.affected)");
			re.eval("hist(Data$LOC.added)");
			re.eval("hist(Data$LOC.removed)");
			re.eval("hist(Data$Nos..of.Files.affected)");
			re.eval("hist(Data$Nos..of.Files.added)");
			re.eval("hist(Data$Nos..of.Files.removed)");
			re.eval("dev.off()");
			
			
			REXP y = re.eval("quantile(Data$Total.LOC.affected, probs = c(0,.25,.5,.75,.9,.95,.99,1))");
		    String g = y.toString();
		     
			FileWriter writer = new FileWriter(dir);
			writer.append("Total LOC Quartile\n" + g + "\n\n");
			
			y = re.eval("quantile(Data$LOC.added, probs = c(0,.25,.5,.75,.9,.95,.99,1))");
		    g = y.toString();
		    writer.append("LOC added Quartile\n" + g + "\n\n");
		    
		    y = re.eval("quantile(Data$LOC.removed, probs = c(0,.25,.5,.75,.9,.95,.99,1))");
		    g = y.toString();
		    writer.append("LOC removed Quartile\n" + g + "\n\n");
		    
		    y = re.eval("quantile(Data$Nos..of.Files.affected, probs = c(0,.25,.5,.75,.9,.95,.99,1))");
		    g = y.toString();
		    writer.append("Nos. of files Quartile\n" + g + "\n\n");
			
			y = re.eval("quantile(Data$Nos..of.Files.added, probs = c(0,.25,.5,.75,.9,.95,.99,1))");
		    g = y.toString();
		    writer.append("Nos. of files Added Quartile\n" + g + "\n\n");
		    
		    y = re.eval("quantile(Data$Nos..of.Files.removed, probs = c(0,.25,.5,.75,.9,.95,.99,1))");
		    g = y.toString();
		    writer.append("Nos. of files removed Quartile\n" + g + "\n\n");
			
		    writer.flush();
		    writer.close();
		    
		    FileWriter writer1 = new FileWriter(dir1);
			
		    re.eval(write2);
		    
		    File authordir =  new File( Datapath + Repositoryname + "//Author//TimeFiles");
		    File[] files = authordir.listFiles();
		    for(File f: files){
		    	String path = f.getAbsolutePath();
		    	//System.out.println(path);
		    	int p = path.lastIndexOf("\\");
		    	int h = path.lastIndexOf(".");
                String fil_name = path.substring(p+1, path.length());
                fil_name = "`" + fil_name + "`"; 
                
                path = path.replace("\\", "/");
                //System.out.println(path);
                String authread = fil_name +" <- read.delim(\""+path+"\")";
		    	re.eval(authread);
		    	String hj = "hist("+fil_name+"$Time)";
		    	String hj1 = "hist("+fil_name+"$LOCChanged)";
		    	String hj2 = "hist("+fil_name+"$LOCADD)";
		    	String hj3 = "hist("+fil_name+"$LOCRem)";
		    	String hj4 = "hist("+fil_name+"$Totalfiles)";
		    	String hj5 = "hist("+fil_name+"$Filesadd)";
		    	String hj6 = "hist("+fil_name+"$Filesrem)";
		    	
				re.eval(hj);
				re.eval(hj1);
				re.eval(hj2);
				re.eval(hj3);
				re.eval(hj4);
				re.eval(hj5);
				re.eval(hj6);
				
				FileWriter writer3 = new FileWriter(Statspath + Repositoryname +"//Author//"+path.substring(p+1, h)+".txt");

				String ev = "quantile("+fil_name+"$V2, probs = c(0,.25,.5,.75,.9,.95,.99,1))";
				y = re.eval(ev);
			    g = y.toString();
				writer3.append("Author Time Graph\n" + g + "\n\n");
				
				ev = "quantile("+fil_name+"$V3, probs = c(0,.25,.5,.75,.9,.95,.99,1))";
				y = re.eval(ev);
			    g = y.toString();
				writer3.append("Author LOC Changed\n" + g + "\n\n");
				
				ev = "quantile("+fil_name+"$V4, probs = c(0,.25,.5,.75,.9,.95,.99,1))";
				y = re.eval(ev);
			    g = y.toString();
				writer3.append("Author LOC Added\n" + g + "\n\n");
				
				ev = "quantile("+fil_name+"$V5, probs = c(0,.25,.5,.75,.9,.95,.99,1))";
				y = re.eval(ev);
			    g = y.toString();
				writer3.append("Author LOC Removed\n" + g + "\n\n");
				
				ev = "quantile("+fil_name+"$V6, probs = c(0,.25,.5,.75,.9,.95,.99,1))";
				y = re.eval(ev);
			    g = y.toString();
				writer3.append("Author Files Changed\n" + g + "\n\n");
				
				ev = "quantile("+fil_name+"$V7, probs = c(0,.25,.5,.75,.9,.95,.99,1))";
				y = re.eval(ev);
			    g = y.toString();
				writer3.append("Author Files Added\n" + g + "\n\n");
				
				ev = "quantile("+fil_name+"$V8, probs = c(0,.25,.5,.75,.9,.95,.99,1))";
				y = re.eval(ev);
			    g = y.toString();
				writer3.append("Author Files Removed\n" + g + "\n\n");
				
				writer3.flush();
				writer3.close();
		    }
		    re.eval("dev.off()");
		    writer1.flush();
		    writer1.close();
			
		} catch (Exception e) {
			System.out.println("EX:"+e);
			e.printStackTrace();
		}
		
		/*

        // Example: how to create a named list or data.frame
        double da[] = {1.2, 2.3, 4.5};
        double db[] = {1.4, 2.6, 4.2};
        long xp3 = re.rniPutDoubleArray(da);
        long xp4 = re.rniPutDoubleArray(db);
        
        // now build a list (generic vector is how that's called in R)
        long la[] = {xp3, xp4};
        long xp5 = re.rniPutVector(la);

        // now let's add names
        String sa[] = {"a","b"};
        long xp2 = re.rniPutStringArray(sa);
        re.rniSetAttr(xp5, "names", xp2);

        // ok, we have a proper list now
        // we could use assign and then eval "b<-data.frame(b)", but for now let's build it by hand:       
        String rn[] = {"1", "2", "3"};
        long xp7 = re.rniPutStringArray(rn);
        re.rniSetAttr(xp5, "row.names", xp7);
        
        long xp6 = re.rniPutString("data.frame");
        re.rniSetAttr(xp5, "class", xp6);
        
        // assign the whole thing to the "b" variable
        re.rniAssign("b", xp5, 0);
        String s22 = "LOCAffected <- read.table(\"E:/LOCAffected.txt\", quote=\"\\\"\")";
        System.out.println(s22);
        re.eval(s22);
        String s11 = "pdf(\"D:/trying190.pdf\")";
        System.out.println(s11);
        //long vf = re.rniPutString(s);
        re.eval(s11);
       //String s33 = "postscript(file=\"D:/graph2.pdf\",  onefile=FALSE, horizontal=FALSE)";
        //re.eval(s33);
       System.out.println("hist(LOCAffected$V1)");
        re.eval("hist(LOCAffected$V1)");
        String hyy = "sink(\"trying190.pdf\", append=TRUE, split=TRUE)";
        re.eval(hyy);
        REXP y = re.eval("quantile(LOCAffected$V1, probs = c(0,1,.25,.5,.75,.9,.95,.99))");
        String g = y.toString();
        System.out.println(g);
        //re.eval("hist(5)");
        re.eval("sink()");
        re.eval("dev.off()");
        {
            System.out.println("Parsing");
            long e=re.rniParse("data(iris)", 1);
            
            //re.eval("hist(5)");
            System.out.println("Result = "+e+", running eval");
            long r=re.rniEval(e, 0);
            System.out.println("Result = "+r+", building REXP");
            REXP x=new REXP(re, r);
            System.out.println("REXP result = "+x);
        }
        {
            System.out.println("Parsing");
            long e=re.rniParse("iris", 1);
            System.out.println("Result = "+e+", running eval");
            long r=re.rniEval(e, 0);
            System.out.println("Result = "+r+", building REXP");
            REXP x=new REXP(re, r);
            System.out.println("REXP result = "+x);
        }
        {
            System.out.println("Parsing");
            long e=re.rniParse("names(iris)", 1);
            System.out.println("Result = "+e+", running eval");
            long r=re.rniEval(e, 0);
            System.out.println("Result = "+r+", building REXP");
            REXP x=new REXP(re, r);
            System.out.println("REXP result = "+x);
            String s[]=x.asStringArray();
            if (s!=null) {
                int i=0; while (i<s.length) { System.out.println("["+i+"] \""+s[i]+"\""); i++; }
            }
        }
        {
            System.out.println("Parsing");
            long e=re.rniParse("rnorm(10)", 1);
            System.out.println("Result = "+e+", running eval");
            long r=re.rniEval(e, 0);
            System.out.println("Result = "+r+", building REXP");
            REXP x=new REXP(re, r);
            System.out.println("REXP result = "+x);
            double d[]=x.asDoubleArray();
            if (d!=null) {
                int i=0; while (i<d.length) { System.out.print(((i==0)?"":", ")+d[i]); i++; }
                System.out.println("");
            }
            System.out.println("");
        }
        {
            REXP x=re.eval("1:10");
            System.out.println("REXP result = "+x);
            int d[]=x.asIntArray();
            if (d!=null) {
                int i=0; while (i<d.length) { System.out.print(((i==0)?"":", ")+d[i]); i++; }
                System.out.println("");
            }
        }

        re.eval("print(1:10/3)");*/
		
		 re.end();
		 System.out.println("end");
        
	/*if (true) {
	    // so far we used R as a computational slave without REPL
	    // now we start the loop, so the user can use the console
	    System.out.println("Now the console is yours ... have fun");
	    re.startMainLoop();
	} else {
	   
	}*/
    
}
}
