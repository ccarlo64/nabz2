package com.homeproject.nabz;

import java.util.ArrayList;

public class Globals{
	   private static Globals instance;
	 
	   // Global variable
	   private int data=0;//test
	   private String token="";
	   private String server="";
	   //
	   private ArrayList<String> conigliNome = new ArrayList<String>();
	   private ArrayList<String> conigliMac = new ArrayList<String>();
	   private ArrayList<Boolean> conigliOnLine = new ArrayList<Boolean>();
	   // DEBUG ***********
	   private String lastURL="none";
	   private String lastResponse="none";
	   //
	   // Restrict the constructor from being instantiated
	   private Globals(){}
	   //**********************
	   public void setData(int d){
	     this.data=d;
	   }
	   public int getData(){
	     return this.data;
	   }
       //******************************	 
	   public void setToken(String d){
		 this.token=d;
	   }
	   public String getToken(){
	     return this.token;
	   }
       //******************************	 
	   public void setServer(String d){
		 this.server=d;
	   }
	   public String getServer(){
	     return this.server;
	   }

       //*********************************
	   public void setConigliOnLine(Boolean d){
		 this.conigliOnLine.add(d);
	   }
	   public ArrayList<Boolean> getConigliOnLine(){
	     return this.conigliOnLine;
	   }

	   
	   
	   //*********************************
	   public void setConigliNome(String d){
		 this.conigliNome.add(d);
	   }
	   public ArrayList<String> getConigliNome(){
	     return this.conigliNome;
	   }
       //*********************************
	   public void setConigliMac(String d){
		 this.conigliMac.add(d);
	   }
	   public ArrayList<String> getConigliMac(){
	     return this.conigliMac;
	   }
	   //****************
	   public void clearConigli(){
			 this.conigliNome.clear();
			 this.conigliMac.clear();
			 this.conigliOnLine.clear();
	   }
	   	   
	   
/*
 * per debug	   
 */
	   public void setLastURL(String d){
		 this.lastURL=d;
	   }
	   public String getLastURL(){
		     return this.lastURL;
	   }
	   public void setLastResponse(String d){
		 this.lastResponse=d;
	   }
	   public String getLastResponse(){
		     return this.lastResponse;
	   }
	   

	   
	   
	   public static synchronized Globals getInstance(){
	     if(instance==null){
	       instance=new Globals();
	     }
	     return instance;
	   }
	}