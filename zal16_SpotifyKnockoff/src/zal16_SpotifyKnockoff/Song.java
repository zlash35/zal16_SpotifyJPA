package zal16_SpotifyKnockoff;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Song.
 */

@Entity 
@Table (name = "song")
public class Song {
	
	/** The song ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	@Column(name = "song_id")
	private String songID;
	
	/** The title. */
	@Column(name = "title")
	private String title;
	
	/** The length. */
	@Column(name = "length")
	private double length;
	
	/** The file path. */
	@Column(name = "file_path")
	private String filePath;
	
	/** The release date. */
	@Column(name = "release_date")
	private String releaseDate;
	
	/** The record date. */
	@Column(name = "record_date")
	private String recordDate;
	
	/** The song artists. */
	@Transient
	Map<String, Artist> songArtists;  // Check whether should be private or not. 
	
	/** The emfactory. */
	@Transient
	EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("zal16_SpotifyKnockoff");
	
	/** The emanager. */
	@Transient
	EntityManager emanager = emfactory.createEntityManager();
	
	
	/**
	 * Instantiates a new song.
	 */
	public Song() {
		
		super();
	}
	
	
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
	
		
		emanager.getTransaction().begin();
		
		Song s = new Song();
		s.setSongID(this.songID);
		s.setTitle(this.title);
		s.setLength(this.length);
		s.setRecordDate(this.recordDate);
		s.setReleaseDate(this.releaseDate);
		s.setFilePath(this.filePath);
		
		emanager.persist(s);
		emanager.getTransaction().commit();
		
		
		s.closeEmanagerConnection();  // These two methods are to wait until there is nothing else in the emanager and emfactory.
		s.closeEmfactoryConnection();
		//emanager.close();
		//emfactory.close();
		
		
	}

	
	/**
	 * Pulls a song from the database.
	 *
	 * @param songID the song ID
	 * @param title the title
	 * @param length the length
	 * @param releaseDate the release date
	 * @param recordDate the record date
	 */
	public Song(String songID, String title, double length, String releaseDate, String recordDate){
		this.songID = songID;
		this.title = title;
		this.length = length;
		this.releaseDate = releaseDate;
		this.recordDate = recordDate;
		
		songArtists = new Hashtable<String, Artist>();
	}
	
	/**
	 * Gets the song record.
	 *
	 * @return the song record
	 */
	Vector<String> getSongRecord() {
		
		Vector<String> songRecord = new Vector<>(6); // We declare it 6 because that's how many columns we have. 
		songRecord.add(this.songID);
		songRecord.add(this.title);
		songRecord.add(this.filePath);
		songRecord.add(String.valueOf(this.length));
		songRecord.add(this.releaseDate);
		songRecord.add(this.recordDate);
		return songRecord; 
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
		this.songID = songID;
		
		emanager.getTransaction().begin();
		
		Song s = emanager.find(Song.class, this.songID);
		emanager.remove(s);
		
		
		emanager.getTransaction().commit();
		
		s.closeEmanagerConnection();
		s.closeEmfactoryConnection();
		
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
	 * Close emanager connection.
	 */
	public void closeEmanagerConnection(){
    	try {
            if(emanager != null){ // Check if connection object already exists
                emanager.close();
                emanager = null;
            }
            
        } catch (Exception e) {
        	//e.printStackTrace(); // debug
        	JOptionPane.showMessageDialog(null, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
        }
    }
	
	/**
	 * Close emfactory connection.
	 */
	public void closeEmfactoryConnection(){
    	try {
            if(emfactory != null){ // Check if connection object already exists
                emfactory.close();
                emfactory = null;
            }
            
        } catch (Exception e) {
        	//e.printStackTrace(); // debug
        	JOptionPane.showMessageDialog(null, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
        }
    }
	/**
	 * Update file path.
	 *
	 * @param songID the song ID
	 * @param filePath the file path
	 */
	public void updateFilePath(String songID, String filePath) {
		this.filePath = filePath; 
		this.songID = songID;
		
		emanager.getTransaction().begin();
		
		Song s = emanager.find(Song.class, this.songID);
		
		s.setFilePath(filePath);
		
		emanager.persist(s);
		emanager.getTransaction().commit();
		
		s.closeEmanagerConnection();
		s.closeEmfactoryConnection();
		//emanager.close();
		//emfactory.close();
		
	}
	
	/**
	 * Update title.
	 *
	 * @param songID the song ID
	 * @param title the title
	 */
	public void updateTitle(String songID, String title) {
		this.title = title; 
		this.songID = songID;
		
		emanager.getTransaction().begin();
		
		Song s = emanager.find(Song.class, this.songID);
		
		s.setTitle(title);
		
		emanager.persist(s);
		emanager.getTransaction().commit();
		
		s.closeEmanagerConnection();
		s.closeEmfactoryConnection();
		//emanager.close();  
		//emfactory.close();
		
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
	}
	
	/**
	 * Sets the song ID.
	 *
	 * @param songID the new song ID
	 */
	public void setSongID(String songID) {
		this.songID = songID;
	}

	/**
	 * Sets the length.
	 *
	 * @param length the new length
	 */
	public void setLength(double length) {
		this.length = length;
	}

	/**
	 * Sets the release date.
	 *
	 * @param releaseDate the new release date
	 */
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * Sets the record date.
	 *
	 * @param recordDate the new record date
	 */
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	/**
	 * Sets the song artists.
	 *
	 * @param songArtists the song artists
	 */
	public void setSongArtists(Map<String, Artist> songArtists) {
		this.songArtists = songArtists;
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
		
	}
}
