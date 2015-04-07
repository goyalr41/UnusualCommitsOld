package main;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

import detection.curvefittingtest;
import extract.extractcommits;
import extract.writedata;
import settings.Settings;
import stats.Curvefitting;

public class maincla {
	
	public static writedata wd;
	public static Curvefitting cf;
	public static int teststatus = 0;
	
	public static void main(String[] args) throws IOException, NoHeadException, GitAPIException {
		
		Settings s = new Settings();
		Settings.Repositorypath = "C://Users//Raman Workstation//Documents//GitHub//node.git";
		Settings.Repositoryname = "Node21";
		Settings.owner = "joyent";
		Settings.repo = "node";
		s.initiate();
			
		wd = new writedata();
		wd.init();
		//wd.writerpointer();
		
		extractcommits ec = new extractcommits();
		ec.extract();
		
		System.out.println("Commits extracted");
		
		cf = new Curvefitting();
		cf.init();
		
		System.out.println("Curve fitting done");
		
		curvefittingtest cft = new curvefittingtest();
		cft.test();
		
	}
}
