package it.polito.tdp.porto.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		System.out.println("TODO: write a Model class and test it!");
		
		model.creaGrafo();
		
		System.out.println("Grafo creato con: "+model.getVertexSize()+
				" autori e "+model.getEdgeSize()+" collegamenti tra autori");
	}

}
