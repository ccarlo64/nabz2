package com.homeproject.nabz;

public class ListBunnies {
    
	public  String mac;
	public  String nome;
    public  boolean isOnLine;
    public  boolean isSleep; 

    public ListBunnies(String nome, String mac, boolean b, boolean c) {
        this.nome = nome;
        this.mac = mac;
        this.isOnLine = b;
        this.isSleep = c;
    }

	
    //********** Set Methods ******************
    public void setMac(String mac) {
        this.mac = mac;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setOnLine(Boolean b) {
        this.isOnLine = b;
    }
    //*********** Get Methods ****************
    public String getMac() {
        return this.mac;
    }
    public String getNome() {
        return this.nome;
    }
    public Boolean getOnLine() {
        return this.isOnLine;
    }
}
