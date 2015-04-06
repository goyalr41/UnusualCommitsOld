package stats;

import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;

import settings.Settings;

public class Curvefitting {
	
	public static double totallocbeta, totallocalpha, totallocauthbeta, totallocauthalpha;
	public static double locaddbeta, locaddalpha, locaddauthbeta, locaddauthalpha;
	public static double locrembeta, locremalpha, locremauthbeta, locremauthalpha;
	public static double nofbeta, nofalpha, nofauthbeta, nofauthalpha;
	public static double nofaddbeta, nofaddalpha, nofaddauthbeta, nofaddauthalpha;
	public static double nofrembeta, nofremalpha, nofremauthbeta, nofremauthalpha;
	public static double commsgalpha, commsgbeta, commsgauthbeta, commsgauthalpha;
	public static double timebeta, timealpha;
	public static Rengine re;
	
	public void init() {
		// Making sure we have the right version of everything
		if (!Rengine.versionCheck()) {
		    System.err.println("** Version mismatch - Java files don't match library version.");
		    System.exit(1);
		}
		
		String[] args = null;
		re = new Rengine(args, false, new TextConsole());
        //System.out.println("Rengine created, waiting for R");
		// the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            return;
        }
        
    	Settings s  = new Settings();
    	s.initiate();
		
	}
	
    public static void calcglobal() {
        String Repositoryname = Settings.Repositoryname;
		
		try {
			String Datapath = Settings.Datapath;
			String DatapathforR = Datapath.replace("//", "/");
			String read1 = "Data <- read.delim(\"" + DatapathforR + Repositoryname + "/Global/Training_data.tsv\")";
	
			//System.out.println(read1);
			
			//String read11 = "Data <- read.delim(\"C:/Users/Raman Workstation/workspace/UnusualCommits/Data/"+Repository+"/Global/Training_filescount.tsv\")";
			
						
			re.eval(read1);
			re.eval("p = Data$Total.LOC");
			re.eval("Y = ecdf(p)");
			re.eval("h <- c(0,max(p))");
			re.eval("p = p[ -which(p %in% h)]");
			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			String temp = "b = -fit$coef[[\"x\"]]";
			REXP b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			REXP a = re.eval("a = exp(a/b)");			
			
		    totallocbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    totallocalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));

		    
		    re.eval("p = Data$LOC.added");
			re.eval("Y = ecdf(p)");
			re.eval("h <- c(0,max(p))");
			re.eval("p = p[ -which(p %in% h)]");
			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			locaddbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    locaddalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		     
		    re.eval("p = Data$LOC.removed");
			re.eval("Y = ecdf(p)");
			re.eval("h <- c(0,max(p))");
			re.eval("p = p[ -which(p %in% h)]");
			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			locrembeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    locremalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    re.eval("p = Data$Files.affected");
			re.eval("Y = ecdf(p)");
			re.eval("h <- c(0,max(p))");
			re.eval("p = p[ -which(p %in% h)]");
			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			nofbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    nofalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    re.eval("p = Data$Files.added");
			re.eval("Y = ecdf(p)");
			re.eval("h <- c(0,max(p))");
			re.eval("p = p[ -which(p %in% h)]");
			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			nofaddbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    nofaddalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    re.eval("p = Data$LOC.removed");
			re.eval("Y = ecdf(p)");
			re.eval("h <- c(0,max(p))");
			re.eval("p = p[ -which(p %in% h)]");
			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			nofrembeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    nofremalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    re.eval("p = Data$Commit.Msg");
			re.eval("Y = ecdf(p)");
			re.eval("h <- c(0,max(p))");
			re.eval("p = p[ -which(p %in% h)]");
			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			commsgbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    commsgalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
			
		} catch (Exception e) {
			System.out.println("EX:"+e);
			e.printStackTrace();
		}
		
		 re.end();
		 
    }
    
    public static void calculateauthor(String email){
            
            String Repositoryname = Settings.Repositoryname;
    		
    		try {
    			String Datapath = Settings.Datapath;
    			String DatapathforR = Datapath.replace("//", "/");
    			String read1 = "Data <- read.delim(\"" + DatapathforR + Repositoryname + "/Author/Timefiles/"+ "ry@tinyclouds.org" +".tsv\")";
    	
    			//System.out.println(read1);
    			
    			//String read11 = "Data <- read.delim(\"C:/Users/Raman Workstation/workspace/UnusualCommits/Data/"+Repository+"/Global/Training_filescount.tsv\")";
    			
    						
    			re.eval(read1);
    			
    			re.eval("p = Data$Time");
    			re.eval("Y = ecdf(p)");
    			re.eval("h <- c(0,max(p))");
    			re.eval("p = p[ -which(p %in% h)]");
    			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
    			re.eval("fit <- lm(y~x,df)");
    			String temp = "b = -fit$coef[[\"x\"]]";
    			REXP b = re.eval(temp);
    			temp = "a = fit$coef[[\"(Intercept)\"]]";
    			re.eval(temp);
    			REXP a = re.eval("a = exp(a/b)");			
    			
    		    timebeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
    		    timealpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
    		    
    		    
    			re.eval("p = Data$Total.LOC");
    			re.eval("Y = ecdf(p)");
    			re.eval("h <- c(0,max(p))");
    			re.eval("p = p[ -which(p %in% h)]");
    			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
    			re.eval("fit <- lm(y~x,df)");
    			temp = "b = -fit$coef[[\"x\"]]";
    			b = re.eval(temp);
    			temp = "a = fit$coef[[\"(Intercept)\"]]";
    			re.eval(temp);
    			a = re.eval("a = exp(a/b)");			
    			
    		    totallocauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
    		    totallocauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));

    		    
    		    re.eval("p = Data$LOC.added");
    			re.eval("Y = ecdf(p)");
    			re.eval("h <- c(0,max(p))");
    			re.eval("p = p[ -which(p %in% h)]");
    			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
    			re.eval("fit <- lm(y~x,df)");
    			temp = "b = -fit$coef[[\"x\"]]";
    			b = re.eval(temp);
    			temp = "a = fit$coef[[\"(Intercept)\"]]";
    			re.eval(temp);
    			a = re.eval("a = exp(a/b)");			
    			
    			locaddauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
    		    locaddauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
    		     
    		    re.eval("p = Data$LOC.removed");
    			re.eval("Y = ecdf(p)");
    			re.eval("h <- c(0,max(p))");
    			re.eval("p = p[ -which(p %in% h)]");
    			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
    			re.eval("fit <- lm(y~x,df)");
    			temp = "b = -fit$coef[[\"x\"]]";
    			b = re.eval(temp);
    			temp = "a = fit$coef[[\"(Intercept)\"]]";
    			re.eval(temp);
    			a = re.eval("a = exp(a/b)");			
    			
    			locremauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
    		    locremauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
    		    
    		    re.eval("p = Data$Files.affected");
    			re.eval("Y = ecdf(p)");
    			re.eval("h <- c(0,max(p))");
    			re.eval("p = p[ -which(p %in% h)]");
    			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
    			re.eval("fit <- lm(y~x,df)");
    			temp = "b = -fit$coef[[\"x\"]]";
    			b = re.eval(temp);
    			temp = "a = fit$coef[[\"(Intercept)\"]]";
    			re.eval(temp);
    			a = re.eval("a = exp(a/b)");			
    			
    			nofauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
    		    nofauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
    		    
    		    re.eval("p = Data$Files.added");
    			re.eval("Y = ecdf(p)");
    			re.eval("h <- c(0,max(p))");
    			re.eval("p = p[ -which(p %in% h)]");
    			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
    			re.eval("fit <- lm(y~x,df)");
    			temp = "b = -fit$coef[[\"x\"]]";
    			b = re.eval(temp);
    			temp = "a = fit$coef[[\"(Intercept)\"]]";
    			re.eval(temp);
    			a = re.eval("a = exp(a/b)");			
    			
    			nofaddauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
    		    nofaddauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
    		    
    		    re.eval("p = Data$LOC.removed");
    			re.eval("Y = ecdf(p)");
    			re.eval("h <- c(0,max(p))");
    			re.eval("p = p[ -which(p %in% h)]");
    			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
    			re.eval("fit <- lm(y~x,df)");
    			temp = "b = -fit$coef[[\"x\"]]";
    			b = re.eval(temp);
    			temp = "a = fit$coef[[\"(Intercept)\"]]";
    			re.eval(temp);
    			a = re.eval("a = exp(a/b)");			
    			
    			nofremauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
    		    nofremauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
    		    
    		    re.eval("p = Data$Commit.Msg");
    			re.eval("Y = ecdf(p)");
    			re.eval("h <- c(0,max(p))");
    			re.eval("p = p[ -which(p %in% h)]");
    			re.eval("df <- data.frame(x=log(p), y = log(1 - Y(p)))");
    			re.eval("fit <- lm(y~x,df)");
    			temp = "b = -fit$coef[[\"x\"]]";
    			b = re.eval(temp);
    			temp = "a = fit$coef[[\"(Intercept)\"]]";
    			re.eval(temp);
    			a = re.eval("a = exp(a/b)");			
    			
    			commsgauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
    		    commsgauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
    		    
    			/*System.out.println(totallocbeta + "\n" + totallocalpha + "\n" + totallocauthbeta + "\n" + totallocauthalpha + "\n" +
	locaddbeta + "\n" + locaddalpha + "\n" + locaddauthbeta + "\n" + locaddauthalpha + "\n" +
	locrembeta + "\n" + locremalpha + "\n" + locremauthbeta + "\n" + locremauthalpha + "\n" +
	nofbeta + "\n" + nofalpha + "\n" + nofauthbeta + "\n" + nofauthalpha + "\n" +
	nofaddbeta + "\n" + nofaddalpha + "\n" + nofaddauthbeta + "\n" + nofaddauthalpha + "\n" +
	nofrembeta + "\n" + nofremalpha + "\n" + nofremauthbeta + "\n" + nofremauthalpha + "\n" +
timebeta + "\n" + timealpha);*/
    			
    			
    		} catch (Exception e) {
    			System.out.println("EX:"+e);
    			e.printStackTrace();
    		}
    		
    		 re.end();
        
    }
    
}
