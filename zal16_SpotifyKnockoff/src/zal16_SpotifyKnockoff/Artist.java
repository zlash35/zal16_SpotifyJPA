package zal16_SpotifyKnockoff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Artist.
 */
public class Artist {


	
	/** The artist ID. */
	private String artistID;
	
	/** The first name. */
	private String firstName;
	
	/** The last name. */
	private String lastName;
	
	/** The band name. */
	private String bandName;
	
	/** The bio. */
	private String bio;
	
	/**
	 * Instantiates a new artist.
	 *
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param bandName the band name
	 * 1. Also gives the artistID a random UUID. 
	 */
	public Artist(String firstName, String lastName, String bandName) {
		this.firstName = firstName; 
		this.lastName = lastName; 
		this.bandName = bandName; 
		this.artistID = UUID.randomUUID().toString();
		
		//System.out.println(this.artistID);
		String sql = "INSERT INTO artist (artist_id, first_name, last_name, band_name, bio) ";
		sql += "VALUES (?, ?, ?, ?, ?);";
		
		System.out.println(sql);
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.artistID);
			ps.setString(2, this.firstName);
			ps.setString(3, this.lastName);
			ps.setString(4, this.bandName);
			ps.setString(5, "");
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Instantiates a new artist.
	 * 1. Based on artist ID 
	 * 2. Puts data for object in a results set. 
	 *
	 * @param artistID the artist ID
	 */
	public Artist(String artistID) {
		String sql = "SELECT * FROM artist WHERE artist_id = '" + artistID + "';";
		//System.out.println(sql);
		
		DbUtilities db = new DbUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()){
				this.artistID = rs.getString("artist_id");
				// System.out.println("Artist ID from database: " + this.artistID); 
				this.firstName = rs.getString("first_name");
				this.lastName = rs.getString("last_name");
				this.bandName = rs.getString("band_name");
				this.bio = rs.getString("bio");
				//System.out.println("The band title from database: " + this.bandName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Pulls a artist from DB.
	 *
	 * @param artistID the artist ID
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param bandName the band name
	 * @param b 
	 */
	public Artist(String artistID, String firstName, String lastName, String bandName) {
		this.artistID = artistID;
		this.firstName = firstName; 
		this.lastName = lastName; 
		this.bandName = bandName; 
		
	}
	
	Vector<String> getArtistRecord() {
		
		Vector<String> artistRecord = new Vector<>(5); // We declare it 5 because that's how many columns we have. 
		artistRecord.add(this.artistID);
		artistRecord.add(this.firstName);
		artistRecord.add(this.lastName);
		artistRecord.add(this.bandName);
		artistRecord.add(this.bio);
		return artistRecord;					
	}
	
	/**
	 * Deletes artist from DB.
	 *
	 * @param artistID the artist ID
	 */
	public void deleteArtist(String artistID) {
		
		String sql = "DELETE FROM artist WHERE artist_id = '" + artistID + "';";
		
		DbUtilities db = new DbUtilities();
		db.executeQuery(sql);
		db.closeDbConnection();
		db = null;
		
		
	}
	
	/**
	 * Sets the band name.
	 *
	 * @param bandName the new band name
	 */
	//You are only allowed to set FirstName, LastName, and BandName in artist class.
	public void setBandName(String bandName) {
		this.bandName = bandName;
		
		String sql = "UPDATE artist SET band_name = ? WHERE artist_id = ?;";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, bandName);
			ps.setString(2,  this.artistID);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Sets the bio.
	 *
	 * @param bio the new bio
	 */
	public void setBio(String bio) {
		this.bio = bio;
		
		String sql = "UPDATE artist SET bio = ? WHERE artist_id = ?;";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, bio);
			ps.setString(2,  this.artistID);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
		
		String sql = "UPDATE artist SET first_name = ? WHERE artist_id = ?;";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, firstName);
			ps.setString(2,  this.artistID);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
		
		String sql = "UPDATE artist SET last_name = ? WHERE artist_id = ?;";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, lastName);
			ps.setString(2,  this.artistID);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets the artist ID.
	 *
	 * @return the artist ID
	 */
	public String getArtistID() {
		return artistID;
	}

	/**
	 * Gets the band name.
	 *
	 * @return the band name
	 */
	public String getBandName() {
		return bandName;
	}

	/**
	 * Gets the bio.
	 *
	 * @return the bio
	 */
	public String getBio() {
		return bio;
	}
	
	
}
