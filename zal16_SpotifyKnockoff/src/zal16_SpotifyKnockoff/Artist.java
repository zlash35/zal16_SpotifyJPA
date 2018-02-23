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
 * The Class Artist.
 */
@Entity 
@Table (name = "artist")
public class Artist {
	
	/** The artist ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	
	
	@Column(name = "artist_id")
	private String artistID;
	
	/** The first name. */
	@Column(name = "first_name")
	private String firstName;
	
	/** The last name. */
	@Column(name = "last_name")
	private String lastName;
	
	/** The band name. */
	@Column(name = "band_name")
	private String bandName;
	
	/** The bio. */
	@Column(name = "bio")
	private String bio;
	
	/** The emfactory. */
	@Transient
	EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("zal16_SpotifyKnockoff");
	
	/** The emanager. */
	@Transient
	EntityManager emanager = emfactory.createEntityManager();
	
	
	/**
	 * Instantiates a new artist.
	 */
	public Artist() {
		
		super();
	}

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
		
		emanager.getTransaction().begin();
		
		Artist a = new Artist();
		a.setArtistID(this.artistID);
		a.setFirstName(this.firstName);
		a.setLastName(this.lastName);
		a.setBandName(this.bandName);

		
		emanager.persist(a);
		emanager.getTransaction().commit();
		
		a.closeEmanagerConnection();
		a.closeEmfactoryConnection(); // These two methods are to wait until there is nothing else in the emanager and emfactory.
		
		//emanager.close();
		//emfactory.close();
		
		
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
	 */
	public Artist(String artistID, String firstName, String lastName, String bandName) {
		this.artistID = artistID;
		this.firstName = firstName; 
		this.lastName = lastName; 
		this.bandName = bandName; 
		
	}
	
	/**
	 * Gets the artist record.
	 *
	 * @return the artist record
	 */
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
		this.artistID = artistID;

		emanager.getTransaction().begin();
		
		Artist a = emanager.find(Artist.class, this.artistID);
		emanager.remove(a);
		
		
		emanager.getTransaction().commit();
		
		a.closeEmanagerConnection();
		a.closeEmfactoryConnection();
		
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
	 * Update first name.
	 *
	 * @param artistID the artist ID
	 * @param firstName the first name
	 */
	public void updateFirstName(String artistID, String firstName) {
		this.firstName = firstName; 
		this.artistID = artistID;
		
		emanager.getTransaction().begin();
		
		Artist a = emanager.find(Artist.class, this.artistID);
		
		a.setFirstName(firstName);
		
		emanager.persist(a);
		emanager.getTransaction().commit();
		
		a.closeEmanagerConnection();
		a.closeEmfactoryConnection();
		
	}
	
	/**
	 * Update last name.
	 *
	 * @param artistID the artist ID
	 * @param lastName the last name
	 */
	public void updateLastName(String artistID, String lastName) {
		this.lastName = lastName; 
		this.artistID = artistID;
		
		emanager.getTransaction().begin();
		
		Artist a = emanager.find(Artist.class, this.artistID);
		
		a.setLastName(lastName);
		
		emanager.persist(a);
		emanager.getTransaction().commit();
		
		a.closeEmanagerConnection();
		a.closeEmfactoryConnection();
		
	}
	
	/**
	 * Update band name.
	 *
	 * @param artistID the artist ID
	 * @param bandName the band name
	 */
	public void updateBandName(String artistID, String bandName) {
		this.bandName = bandName; 
		this.artistID = artistID;
		
		emanager.getTransaction().begin();
		
		Artist a = emanager.find(Artist.class, this.artistID);
		
		a.setBandName(bandName);
		
		emanager.persist(a);
		emanager.getTransaction().commit();
		
		a.closeEmanagerConnection();
		a.closeEmfactoryConnection();		
	}
	
	/**
	 * Sets the artist ID.
	 *
	 * @param artistID the new artist ID
	 */
	public void setArtistID(String artistID) {
		this.artistID = artistID;
	}
	
	/**
	 * Sets the band name.
	 *
	 * @param bandName the new band name
	 */
	//You are only allowed to set FirstName, LastName, and BandName in artist class.
	public void setBandName(String bandName) {
		this.bandName = bandName;
		
	}

	/**
	 * Sets the bio.
	 *
	 * @param bio the new bio
	 */
	public void setBio(String bio) {
		this.bio = bio;
	
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
