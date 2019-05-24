package it.polito.tdp.porto.model;

public class Adiacenza {
	
	private int idAuthor1;
	private int idAuthor2;
	
	public Adiacenza(int idAuthor1, int idAuthor2) {
		this.idAuthor1 = idAuthor1;
		this.idAuthor2 = idAuthor2;
	}
	
	public int getIdAuthor1() {
		return idAuthor1;
	}
	public void setIdAuthor1(int idAuthor1) {
		this.idAuthor1 = idAuthor1;
	}
	public int getIdAuthor2() {
		return idAuthor2;
	}
	public void setIdAuthor2(int idAuthor2) {
		this.idAuthor2 = idAuthor2;
	}

	@Override
	public String toString() {
		return "Adiacenza [idAuthor1=" + idAuthor1 + ", idAuthor2=" + idAuthor2 + "]";
	}
	
	

}
