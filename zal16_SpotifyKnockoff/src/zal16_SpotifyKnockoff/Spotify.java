package zal16_SpotifyKnockoff;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Spotify {

	/**
	 * @wbp.parser.entryPoint
	 */
	public static DefaultTableModel searchSongs(String searchTerm){
		String sql = "SELECT song_id, title, length, release_date, record_date FROM song ";
		if(!searchTerm.equals("")){
				sql += " WHERE title LIKE '%" + searchTerm + "%';";
		}
		
		try {
			DbUtilities db = new DbUtilities();
			String[] columnNames = {"Song ID", "Title", "Length", "Release Date", "Record Date"};
			return db.getDataTable(sql, columnNames);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
		}
		return null;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public static DefaultTableModel searchAlbums(String searchTerm){
		String sql = "SELECT album_id, title, release_date, recording_company_name, number_of_tracks, PMRC_rating, length FROM album ";
		if(!searchTerm.equals("")){
				sql += " WHERE title LIKE '%" + searchTerm + "%';";
		}
		
		try {
			DbUtilities db = new DbUtilities();
			String[] columnNames = {"Album ID", "title", "release_date", "recording_company_name", "numnber_of_tracks", "PMRC_rating", "length"};
			return db.getDataTable(sql, columnNames);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to connect to database");  //JOptionPane.showMessageDialog(frame, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
		}
		return null;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
		
	public static DefaultTableModel searchArtists(String searchTerm){
		String sql = "SELECT artist_id, first_name, last_name, band_name FROM artist ";
		if(!searchTerm.equals("")){
			sql += " WHERE first_name LIKE '%" + searchTerm + "%' OR last_name LIKE '%" + searchTerm + "%' OR band_name LIKE '%" + searchTerm + "%';";  
					
		}
		
		try {
			DbUtilities db = new DbUtilities();
			String[] columnNames = {"Artist ID", "first_name", "last_name", "band_name"};
			return db.getDataTable(sql, columnNames);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
		}
		return null;
	}
	
	
}
