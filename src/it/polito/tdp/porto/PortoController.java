package it.polito.tdp.porto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	this.txtResult.clear();
    	try{
    		Author autore = this.boxPrimo.getValue();
    		List<Author> coAutori;
        	coAutori = model.trovaCoAutoriDi(autore);
        	String coAutoriString="Lista coautori di "+autore.toString()+":\n";
        	for(Author a : coAutori) {
        		coAutoriString = coAutoriString+a.toString()+"\n";
        	}
        	this.txtResult.appendText(coAutoriString);
        	this.boxSecondo.getItems().addAll(this.model.getNonCoautori(autore));
    	}catch(NullPointerException e) {
    		this.txtResult.appendText("Selezionare un autore per trovare i coautori");
    	}
    	
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	Author a1 = this.boxPrimo.getValue();
    	Author a2 = this.boxSecondo.getValue();
    	List<Paper> papers = model.articoliDaCamminoMinimo(a1, a2);
    	String articoli = "";
    	this.txtResult.clear();
    	try{
    		if(papers.isEmpty()) {
    		this.txtResult.appendText("Non ci sono articoli che collegano i due autori");
    	} else {
    		for(Paper p : papers) {
    			articoli = articoli + p.toString()+"\n";
       		}
    	 	this.txtResult.appendText("Tramite altri coautori, i seguenti articoli\ncollegano gli autori "+a1.toString()+
    			" e "+a2.toString()+":\n"+articoli);
    	}
    	}catch(Exception e) {
    		this.txtResult.appendText("Non ci sono articoli che collegano i due autori"+
    					"O uno degli autori non ha ancora scritto pubblicazioni");
        }
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";
       
        

    }

	public void setModel(Model model) {
		this.model = model;
		model.creaGrafo();
		this.boxPrimo.getItems().addAll(this.model.getAuthors());
	}
}
