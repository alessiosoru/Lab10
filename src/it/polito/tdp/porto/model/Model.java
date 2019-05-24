package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private Graph<Author, DefaultEdge> grafo;
	private List<Author> authors;
	private Map<Integer, Author> authorsMap;
	
	public Model() {
		this.authorsMap = new HashMap<>();
		this.authors = new ArrayList<Author>();
	}
	
	public void creaGrafo() {
		
		// creo grafo
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		
		PortoDAO dao = new PortoDAO();
		
		
		// Aggiungo i vertici
		authors = dao.loadAllAuthors(this.authorsMap);
		Collections.sort(authors);
		Graphs.addAllVertices(this.grafo, authors);
		
		// aggiungo archi
		List<Adiacenza> archi = dao.getAutoriAdiacenti();
		for(Adiacenza a : archi) {
			this.grafo.addEdge(this.authorsMap.get(a.getIdAuthor1()),
					this.authorsMap.get(a.getIdAuthor2()));
		}
	}
	
	public int getVertexSize() {
		return this.grafo.vertexSet().size();
	}
	
	public int getEdgeSize() {
		return this.grafo.edgeSet().size();
	}

	public List<Author> trovaCoAutoriDi(Author autore) {
		List<Author> coAutori = new ArrayList<>();
		coAutori.addAll(Graphs.neighborListOf(this.grafo, autore));
		Collections.sort(coAutori);
		return coAutori;
	}

	public List<Author> getAuthors() {
		return this.authors;
	}

}
