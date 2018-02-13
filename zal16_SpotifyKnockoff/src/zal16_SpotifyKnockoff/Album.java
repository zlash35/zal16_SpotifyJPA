package zal16_SpotifyKnockoff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * The Class Album.
 */
// TODO: Auto-generated Javadoc
public class Album {
	

	private String albumID;
	private String title;
	private String releaseDate;
	private String coverImagePath;
	private String recordingCompany;
	private int numberOfTracks;
	private String pmrcRating;
	private int length;
    Map <String, Song> albumSongs;
	
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
		
		String sql = "INSERT INTO album (album_id,title,release_date,cover_image_path,recording_company_name,number_of_tracks,PMRC_rating,length) ";
        sql += "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		
		System.out.println(sql);
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.albumID);
			ps.setString(2, this.title);
			ps.setString(3, this.releaseDate);
			ps.setString(4, " ");
			ps.setString(5, this.recordingCompany);
			ps.setInt(6, this.numberOfTracks);
			ps.setString(7, this.pmrcRating);
			ps.setInt(8, this.length);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
		
		String sql = "DELETE FROM album WHERE album_id = '" + albumID + "';";
		
		DbUtilities db = new DbUtilities();
		db.executeQuery(sql);
		db.closeDbConnection();
		db = null;
		
		
		
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
	 * Sets the cover image path.
	 *
	 * @param coverImagePath the new cover image path
	 */
	
	//You are only allowed to set the Cover Image Path, recording date, title, recording company. 
	public void setCoverImagePath(String coverImagePath) {
		this.coverImagePath = coverImagePath;
		
		String sql = "UPDATE album SET cover_image_path = ? WHERE album_id = ?;";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, coverImagePath);
			ps.setString(2,  this.albumID);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sets the recording company.
	 *
	 * @param recordingCompany the new recording company
	 */
	public void setRecordingCompany(String recordingCompany) {
		this.recordingCompany = recordingCompany;
		
		String sql = "UPDATE album SET recording_company_name = ? WHERE album_id = ?;";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, recordingCompany);
			ps.setString(2,  this.albumID);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		
		String sql = "UPDATE album SET title = ? WHERE album_id = ?;";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2,  this.albumID);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
		
		String sql = "UPDATE album SET release_date = ? WHERE album_id = ?;";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, releaseDate);
			ps.setString(2,  this.albumID);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
