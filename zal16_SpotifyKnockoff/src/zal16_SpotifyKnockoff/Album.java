package zal16_SpotifyKnockoff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

// TODO: Auto-generated Javadoc
/**
 * The Class Album.
 */
// TODO: Auto-generated Javadoc
@Entity 
@Table (name = "album")
public class Album {
	
	/** The album ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	@Column(name = "album_id")
	private String albumID;
	
	/** The title. */
	@Column(name = "title")
	private String title;
	
	/** The release date. */
	@Column(name = "release_date")
	private String releaseDate;
	
	/** The cover image path. */
	@Column(name = "cover_image_path")
	private String coverImagePath;
	
	/** The recording company. */
	@Column(name = "recording_company_name")
	private String recordingCompany;
	
	/** The number of tracks. */
	@Column(name = "number_of_tracks")
	private int numberOfTracks;
	
	/** The pmrc rating. */
	@Column(name = "PMRC_rating")
	private String pmrcRating;
	
	/** The length. */
	@Column(name = "length")
	private int length;
	
	/** The album songs. */
	@Transient
    Map <String, Song> albumSongs;
	
	/** The emfactory. */
	@Transient
	EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("zal16_SpotifyKnockoff");
	
	/** The emanager. */
	@Transient
	EntityManager emanager = emfactory.createEntityManager();
    
    /**
     * Instantiates a new album.
     */
    public Album() {
		
		super();
	}
	
	/**
	 * Instantiates a new album.
	 *
	 * 1.. Also declares new Instantiates new hashtable to store Song. 
	 * @param title the title
	 * @param releaseDate the release date
	 * @param recordingCompany the recording company
	 * @param numberOfTracks the number of tracks
	 * @param pmrcRating the pmrc rating
	 * @param length the length
	 */
	public Album(String title, String releaseDate, String recordingCompany, int numberOfTracks, String pmrcRating, int length) {
		albumSongs = new Hashtable<String, Song>();
		this.title = title; 
		this.releaseDate = releaseDate; 
		this.recordingCompany = recordingCompany; 
		this.numberOfTracks = numberOfTracks; 
		this.pmrcRating = pmrcRating; 
		this.length = length; 
		this.albumID = UUID.randomUUID().toString();
		
		
		emanager.getTransaction().begin();
		
		Album al = new Album();
		al.setAlbumID(this.albumID);
		al.setTitle(this.title);
		al.setReleaseDate(this.releaseDate);
		al.setRecordingCompany(this.recordingCompany);
		al.setNumberOfTracks(this.numberOfTracks);
		al.setPmrcRating(this.pmrcRating);
		al.setLength(this.length);
		
		emanager.persist(al);
		emanager.getTransaction().commit();
		
		al.closeEmanagerConnection();  // These two methods are to wait until there is nothing else in the emanager and emfactory.
		al.closeEmfactoryConnection();
		//emanager.close();
		//emfactory.close();
		
	}

	/**
	 * Instantiates a new album.
	 * 1. Based on the Album ID
	 * 2. Stores the album data in a result set. 
	 * 3. Also declares new Instantiates new hashtable to store Song. 
	 * @param albumID the album ID
	 */
	public Album(String albumID) {
		albumSongs = new Hashtable<String, Song>();
		String sql = "SELECT * FROM album WHERE album_id = '" + albumID + "';";
		//System.out.println(sql);
		
		DbUtilities db = new DbUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()){
				this.albumID = rs.getString("album_id");
				// System.out.println("Album ID from database: " + this.albumID);
				 
				this.title = rs.getString("title");
				this.releaseDate = rs.getString("release_date");
				this.coverImagePath = rs.getString("cover_image_path");
				this.recordingCompany = rs.getString("recording_company_name");
				this.numberOfTracks = rs.getInt("number_of_tracks");
				this.pmrcRating = rs.getString("PMRC_rating");
				this.length = rs.getInt("length");
				//System.out.println("The album title from database: " + this.title);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Pulls a album from the DB.
	 *
	 * @param albumID the album ID
	 * @param title the title
	 * @param releaseDate the release date
	 * @param recordingCompany the recording company
	 * @param numberOfTracks the number of tracks
	 * @param pmrcRating the pmrc rating
	 * @param length the length
	 */
	public Album(String albumID, String title, String releaseDate, String recordingCompany, int numberOfTracks, String pmrcRating, int length) {
		
		this.albumID = albumID;
		this.title = title; 
		this.releaseDate = releaseDate; 
		this.recordingCompany = recordingCompany; 
		this.numberOfTracks = numberOfTracks; 
		this.pmrcRating = pmrcRating; 
		this.length = length; 
		
		albumSongs = new Hashtable<String, Song>();
	}
	
	/**
	 * Gets the album record.
	 *
	 * @return the album record
	 */
	Vector<String> getAlbumRecord() {
		
		Vector<String> albumRecord = new Vector<>(8); // We declare it 6 because that's how many columns we have. 
		albumRecord.add(this.albumID);
		albumRecord.add(this.title);
		albumRecord.add(this.releaseDate);
		albumRecord.add(this.coverImagePath);
		albumRecord.add(this.recordingCompany);
		albumRecord.add(String.valueOf(this.numberOfTracks));
		albumRecord.add(this.pmrcRating);
		albumRecord.add(String.valueOf(this.length));
		return albumRecord;
	}
	/**
	 * Delete album from DB.
	 *
	 * @param albumID the album ID
	 */
	public void deleteAlbum(String albumID) {
		
		this.albumID = albumID;
		
		emanager.getTransaction().begin();
		
		Album al = emanager.find(Album.class, this.albumID);
		emanager.remove(al);
		
		
		emanager.getTransaction().commit();
		
		al.closeEmanagerConnection();
		al.closeEmfactoryConnection();
		
		
		
	}
	
	/**
	 * Adds the song to the album_song table. 
	 *
	 * @param song the song
	 */
	public void addSong(Song song) { 
		
        albumSongs.put(song.getSongID(), song);
		
        String sql = "INSERT INTO album_song (fk_album_id, fk_song_id) ";
        sql += "VALUES (?, ?);";
        
       // System.out.println(sql);
        
        try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.albumID);
			ps.setString(2,  song.getSongID());
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	/**
	 * Delete song from album_song table based on songID. 
	 *
	 * @param songID the song ID
	 */
	public void deleteSong(String songID) {
		
		String sql = "DELETE FROM album_song WHERE fk_album_id = '" + this.albumID + "' and fk_song_id = '" + songID + "';";
		// System.out.println(sql);
		
		DbUtilities db = new DbUtilities();		
		db.executeQuery(sql);
		db.closeDbConnection();
		db = null;
		
		
	}
	
	/**
	 * Delete song from album_song table based on song object. 
	 *
	 * @param song the song
	 */
	public void deleteSong(Song song) {
		
		albumSongs = new Hashtable<String, Song>();
        albumSongs.put(song.getSongID(), song);
        
        String sql = "DELETE FROM album_song WHERE fk_album_id = '" + this.albumID + "' and fk_song_id = '" + song.getSongID() + "';";   
        
       // System.out.println(sql);
        
        DbUtilities db = new DbUtilities();
		db.executeQuery(sql);
		db.closeDbConnection();
		db = null;
        song = null; 
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
	 * Update cover image path.
	 *
	 * @param albumID the album ID
	 * @param coverImagePath the cover image path
	 */
	public void updateCoverImagePath(String albumID, String coverImagePath) {
		this.coverImagePath = coverImagePath; 
		this.albumID = albumID;
		
		emanager.getTransaction().begin();
		
		Album al = emanager.find(Album.class, this.albumID);
		
		al.setCoverImagePath(coverImagePath);
		
		emanager.persist(al);
		emanager.getTransaction().commit();
		
		al.closeEmanagerConnection();
		al.closeEmfactoryConnection();
		
	}
	
	/**
	 * Update release date.
	 *
	 * @param albumID the album ID
	 * @param releaseDate the release date
	 */
	public void updateReleaseDate(String albumID, String releaseDate) {
		this.releaseDate = releaseDate; 
		this.albumID = albumID;
		
		emanager.getTransaction().begin();
		
		Album al = emanager.find(Album.class, this.albumID);
		
		al.setReleaseDate(releaseDate);
		
		emanager.persist(al);
		emanager.getTransaction().commit();
		
		al.closeEmanagerConnection();
		al.closeEmfactoryConnection();
		
	}
	
	/**
	 * Update title.
	 *
	 * @param albumID the album ID
	 * @param title the title
	 */
	public void updateTitle(String albumID, String title) {
		this.title = title; 
		this.albumID = albumID;
		
		emanager.getTransaction().begin();
		
		Album al = emanager.find(Album.class, this.albumID);
		
		al.setTitle(title);
		
		emanager.persist(al);
		emanager.getTransaction().commit();
		
		al.closeEmanagerConnection();
		al.closeEmfactoryConnection();
		
	}
	
	/**
	 * Update recording company.
	 *
	 * @param albumID the album ID
	 * @param recordingCompany the recording company
	 */
	public void updateRecordingCompany(String albumID, String recordingCompany) {
		this.recordingCompany = recordingCompany; 
		this.albumID = albumID;
		
		emanager.getTransaction().begin();
		
		Album al = emanager.find(Album.class, this.albumID);
		
		al.setRecordingCompany(recordingCompany);
		
		emanager.persist(al);
		emanager.getTransaction().commit();
		
		al.closeEmanagerConnection();
		al.closeEmfactoryConnection();
		
	}
	
	/**
	 * Sets the album ID.
	 *
	 * @param albumID the new album ID
	 */
	public void setAlbumID(String albumID) {
		this.albumID = albumID;
	}
	
	/**
	 * Sets the number of tracks.
	 *
	 * @param numberOfTracks the new number of tracks
	 */
	public void setNumberOfTracks(int numberOfTracks) {
		this.numberOfTracks = numberOfTracks;
	}

	/**
	 * Sets the pmrc rating.
	 *
	 * @param pmrcRating the new pmrc rating
	 */
	public void setPmrcRating(String pmrcRating) {
		this.pmrcRating = pmrcRating;
	}

	/**
	 * Sets the length.
	 *
	 * @param length the new length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Sets the cover image path.
	 *
	 * @param coverImagePath the new cover image path
	 */
	
	//You are only allowed to set the Cover Image Path, release date, title, recording company. 
	public void setCoverImagePath(String coverImagePath) {
		this.coverImagePath = coverImagePath;
		
	}

	/**
	 * Sets the recording company.
	 *
	 * @param recordingCompany the new recording company
	 */
	public void setRecordingCompany(String recordingCompany) {
		this.recordingCompany = recordingCompany;
		
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
	 * Gets the release date.
	 *
	 * @return the release date
	 */
	public String getReleaseDate() {
		return releaseDate;
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
	 * Gets the album ID.
	 *
	 * @return the album ID
	 */
	public String getAlbumID() {
		return albumID;
	}

	/**
	 * Gets the cover image path.
	 *
	 * @return the cover image path
	 */
	public String getCoverImagePath() {
		return coverImagePath;
	}

	/**
	 * Gets the recording company.
	 *
	 * @return the recording company
	 */
	public String getRecordingCompany() {
		return recordingCompany;
	}

	/**
	 * Gets the number of tracks.
	 *
	 * @return the number of tracks
	 */
	public int getNumberOfTracks() {
		return numberOfTracks;
	}

	/**
	 * Gets the pmrc rating.
	 *
	 * @return the pmrc rating
	 */
	public String getPmrcRating() {
		return pmrcRating;
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Gets the album songs.
	 *
	 * @return the album songs
	 */
	public Map<String, Song> getAlbumSongs() {
		return albumSongs;
	}	
	
	
	
	
	
	
	
}
