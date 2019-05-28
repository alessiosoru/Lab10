package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private Graph<Author, DefaultEdge> grafo;
	private List<Author> authors;
	private Map<Integer, Author> authorsMap;
	PortoDAO dao;
	
	public Model() {
		this.authorsMap = new HashMap<>();
		this.authors = new ArrayList<Author>();
		dao = new PortoDAO();
	}
	
	public void creaGrafo() {
		
		// creo grafo
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		
		
		
		
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

	public List<Author> getNonCoautori(Author a1) {
		// prendo gli autori che non sono coautori di a1
		List<Author> listNonCoAutori = new ArrayList<>();
		listNonCoAutori = this.authors;
		listNonCoAutori.removeAll(this.trovaCoAutoriDi(a1));
		listNonCoAutori.remove(a1);
		return listNonCoAutori;
	}
	
	public List<Author> trovaCamminoMinimo(Author source, Author target){
		// trovo il cammino minimo di Dijkstra tra gli autori
		DijkstraShortestPath<Author, DefaultEdge> dijkstra = new DijkstraShortestPath<>(this.grafo);
		GraphPath<Author, DefaultEdge> path = dijkstra.getPath(source, target);
		return path.getVertexList();
	}
	
	public List<Paper> articoliDaCamminoMinimo(Author source, Author target){
		List<Paper> articoli = new ArrayList<>();
		List<Adiacenza> adiacenze = new ArrayList<>();
		List<Author> camminoAutori = this.trovaCamminoMinimo(source, target);
		
		for(Author a : camminoAutori) {
			System.out.println(a.toString());
		}
		
		// dalla lista di vertici seleziono le adiacenze
		Author precedente=null;
		for(Author a : camminoAutori) {
			if(precedente == null)
				precedente = a;
			else {
				Adiacenza ad = new Adiacenza(precedente.getId(), a.getId());
				adiacenze.add(ad);
				precedente = a;
			}
			
		}
		
		// seleziono gli articoli in comune e ritorno la lista di articoli (paper)
		for(Adiacenza ad : adiacenze) {
			Paper p = this.dao.getArticoloInComune(ad.getIdAuthor1(), ad.getIdAuthor2());
			articoli.add(p);
		}
		
		for(Paper p : articoli) {
			System.out.println(p.toString());
		}
		return articoli;
	}

}
