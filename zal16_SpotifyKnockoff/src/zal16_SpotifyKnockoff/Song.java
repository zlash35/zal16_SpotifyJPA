package zal16_SpotifyKnockoff;

import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Song.
 */
public class Song {
	private String songID;
	private String title;
	private double length;
	private String filePath;
	private String releaseDate;
	private String recordDate;
	Map<String, Artist> songArtists;  // Check whether should be private or not. 
	
	/**
	 * Instantiates a new song.
	 * 1. Also gives a new sondID a random UUID. 
	 * @param title the title
	 * @param length the length
	 * @param releaseDate the release date
	 * @param recordDate the record date
	 */
	public Song(String title, double length, String releaseDate, String recordDate){
		songArtists = new Hashtable<String, Artist>();
		this.title = title;
		this.length = length;
		this.releaseDate = releaseDate;
		this.recordDate = recordDate;
		this.songID = UUID.randomUUID().toString();
		
		// System.out.println(this.songID);
		// String sql = "INSERT INTO song (song_id,title,length,file_path,release_date,record_date,fk_album_id) ";
		// sql += "VALUES ('" + this.songID + "', '" + this.title + "', " + this.length + ", '', '" + this.releaseDate + "', '" + this.recordDate + "', '" + this.albumID + "');";
		String sql = "INSERT INTO song (song_id,title,length,file_path,release_date,record_date) ";
		sql += "VALUES (?, ?, ?, ?, ?, ?);";
		
		System.out.println(sql);
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.songID);
			ps.setString(2,  this.title);
			ps.setDouble(3, this.length);
			ps.setString(4, "");
			ps.setString(5, this.releaseDate);
			ps.setString(6, this.recordDate);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Instantiates a new song.
	 * 1. Based on song ID 
	 * 2. Puts data for object in a results set. 
	 * @param songID the song ID
	 */
	public Song(String songID){
		songArtists = new Hashtable<String, Artist>();
		String sql = "SELECT * FROM song WHERE song_id = '" + songID + "';";
		// System.out.println(sql);
		DbUtilities db = new DbUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()){
				this.songID = rs.getString("song_id");
				// System.out.println("Song ID from database: " + this.songID);
				this.title = rs.getString("title");
				this.releaseDate = rs.getDate("release_date").toString();
				this.recordDate = rs.getDate("record_date").toString();
				this.length = rs.getDouble("length");
				// System.out.println("Song title from database: " + this.title);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	/**
	 * Delete song from DB.
	 *
	 * @param songID the song ID
	 */
	public void  deleteSong(String songID) {
		String sql = "DELETE FROM song WHERE song_id = '" + songID + "';";
		// System.out.println(sql);
			//I'm pretty sure we don't need the try catch black with SQLexception because we are not updating the DB with Strings. 
			//Am I correct?
			DbUtilities db = new DbUtilities();
			db.executeQuery(sql);
			db.closeDbConnection();
			db = null;
		
	}	
	
	/**
	 * Adds the artist to the song_artist table.
	 *
	 * @param artist the artist
	 */
	public void addArtist(Artist artist) {                
		
		//songArtists = new Hashtable<String, Artist>();  We don't need this because we call it in the constructor so it saves with the songID.//songArtists = new Hashtable<String, Artist>();
        songArtists.put(artist.getArtistID(), artist);
		
        String sql = "INSERT INTO song_artist (fk_song_id, fk_artist_id) ";
        sql += "VALUES (?, ?);";
        
       // System.out.println(sql);
        
        try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.songID);
			ps.setString(2,  artist.getArtistID());
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete artist from song_artist table based on artistID.
	 *
	 * @param artistID the artist ID
	 */
	public void deleteArtist(String artistID) {
		
		
		String sql = "DELETE FROM song_artist WHERE fk_song_id = '" + this.songID + "' and fk_artist_id = '" + artistID + "';"; 
		
		System.out.println(sql);
		
		DbUtilities db = new DbUtilities();
		db.executeQuery(sql);
		db.closeDbConnection();
		db = null;
		
	}
	
	/**
	 * Delete artist from song_artist table based on artist object.
	 *
	 * @param artist the artist
	 */
	public void deleteArtist(Artist artist) {
		
		//songArtists = new Hashtable<String, Artist>();  We don't need this because we call it in the constrctor so it saves with the songID.
        songArtists.put(artist.getArtistID(), artist);
		
        String sql = "DELETE FROM song_artist WHERE fk_song_id = '" + this.songID + "' and fk_artist_id = '" + artist.getArtistID() + "';";   
        
       // System.out.println(sql);
        
        DbUtilities db = new DbUtilities();
		db.executeQuery(sql);
		db.closeDbConnection();
		db = null;
		artist = null; // THis is how you destroy the object. 
		
	}
	
	/**
	 * Gets the release date.
	 *
	 * @return the release date
	 */
	// You are only allowed to set song Title and Filepath in song class. 
	public String getReleaseDate() {
		return releaseDate;
	}

	/**
	 * Gets the song ID.
	 *
	 * @return the song ID
	 */
	public String getSongID() {
		return songID;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;             
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		
		this.title = title; 
		
		String sql = "UPDATE song SET title = ? WHERE song_id = ?;";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2,  this.songID);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public double getLength() {
		return length;
	}

	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Gets the record date.
	 *
	 * @return the record date
	 */
	public String getRecordDate() {
		return recordDate;
	}

	/**
	 * Gets the song artists.
	 *
	 * @return the song artists
	 */
	public Map<String, Artist> getSongArtists() {
		return songArtists;
	}
	
	/**
	 * Sets the file path.
	 *
	 * @param filePath the new file path
	 */
	public void setFilePath(String filePath) {
		
		this.filePath = filePath;
		
		String sql = "UPDATE song SET file_path = ? WHERE song_id = ?;";
				
				try {
					DbUtilities db = new DbUtilities();
					Connection conn = db.getConn();
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setString(1, filePath);
					ps.setString(2,  this.songID);
					ps.executeUpdate();
					db.closeDbConnection();
					db = null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	}
}
