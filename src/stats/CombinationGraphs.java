package stats;

import java.io.*;
import java.awt.Frame;
import java.awt.FileDialog;

import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RMainLoopCallbacks;

import settings.Settings;

public class CombinationGraphs {
    public static void main(String[] args) {
    	
	// Making sure we have the right version of everything
	if (!Rengine.versionCheck()) {
	    System.err.println("** Version mismatch - Java files don't match library version.");
	    System.exit(1);
	}
	
	Settings s = new Settings();
	Settings.Repositorypath = "C://Users//Raman Workstation//Documents//GitHub//node.git";
	Settings.Repositoryname = "Node9";
	Settings.owner = "joyent";
	Settings.repo = "node";
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
			String write1 = "pdf(\"" + StatspathforR + Repositoryname + "/Global/CombinationFiles.pdf\")";
			//String write11 = "pdf(\"C:/Users/Raman Workstation/workspace/UnusualCommits/Stats/"+Repository+"/Global/Training_TypeFiles.pdf\")";
			
		//	String write2 = "pdf(\"" + StatspathforR + Repositoryname + "/Author/Training_LOCaFiles.pdf\", height=4, width=5)";
						
		    re.eval(write1);
		    
		    File combdir =  new File( Datapath + Repositoryname + "//Global//FileCombinations");
		    File[] files = combdir.listFiles();
		    for(File f: files){
		    	String path = f.getAbsolutePath();
		    	//System.out.println(path);
		    	int p = path.lastIndexOf("\\");
		    	int h = path.lastIndexOf(".");
                String fil_name = path.substring(p+1, path.length());
                fil_name = "`" + fil_name + "`"; 
                path = path.replace("\\", "/");
                //System.out.println(path);
                String authread = fil_name +" <- read.delim(\""+path+"\", header=FALSE)";
		    	re.eval(authread);
		    	//System.out.println(authread);
		    	String hj = "hist(log10("+fil_name+"$V1/"+fil_name+"$V2), xlim = c(-5,5))";
		    	
				re.eval(hj);
				
				String mea = "s <- mean(log10("+fil_name+"$V1/"+fil_name+"$V2))";
				String sd = "d <- sd(log10("+fil_name+"$V1/"+fil_name+"$V2))";
				//String val = "abline(v=c("+mean+" - 3*" + sdt+ " , " + mean + ", "+ mean + " + 3*" + sdt + "), col=\"red\")"; 
				String val = "abline(v=c(s-3*d, s, s+3*d), col=\"red\")"; 
				re.eval(mea);
				re.eval(sd);
				
				re.eval(val);
				System.out.println(val);
				
				
		    }
		    re.eval("dev.off()");
			
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
