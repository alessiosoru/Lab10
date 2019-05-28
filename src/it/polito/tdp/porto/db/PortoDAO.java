package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.porto.model.Adiacenza;
import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public List<Author> loadAllAuthors(Map<Integer, Author> authorsMap) {
		final String sql = "SELECT * FROM author";
		List<Author> autori = new ArrayList<Author>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				authorsMap.put(autore.getId(), autore);
				autori.add(autore);
			}

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		return autori;
	}

	public List<Adiacenza> getAutoriAdiacenti() {
		
		final String sql = "SELECT c1.authorid id1, c2.authorid id2 " + 
				"FROM creator c1, creator c2 " + 
				"WHERE c1.authorid < c2.authorid " + 
				" AND c1.eprintid = c2.eprintid ";
		List<Adiacenza> autoriAdiacenti = new ArrayList<Adiacenza>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Adiacenza a = new Adiacenza(rs.getInt("id1"), rs.getInt("id2"));
				autoriAdiacenti.add(a);
				System.out.println(a.toString());
			}

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		return autoriAdiacenti;
	}

	public Paper getArticoloInComune(int idAuthor1, int idAuthor2) {
		final String sql = "SELECT paper.eprintid,  title, issn, publication, type, types " + 
				"FROM paper, creator c1, creator c2 " + 
				"WHERE c1.authorid = ? AND c2.authorid = ? AND " + 
				"paper.eprintid=c1.eprintid AND " + 
				"paper.eprintid=c2.eprintid AND " + 
				"c1.eprintid=c2.eprintid";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, idAuthor1);
			st.setInt(2, idAuthor2);
			
			ResultSet rs = st.executeQuery();

			if (rs.next()) { // mi basta prendere un solo articolo in comune
				Paper paper = new Paper(rs.getInt("paper.eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
}