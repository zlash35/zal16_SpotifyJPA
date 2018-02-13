package zal16_SpotifyKnockoff;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

import java.awt.SystemColor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SpotifyGUI {

	private JFrame frame;
	private JLabel lblViewSelector;
	private JTextField txtSearch;
	private JRadioButton radioSelectAlbum;
	private JRadioButton radioSelectArtist;
	private JRadioButton radioSelectSong;
	private JButton btnSearch;
	private JTable tblData;
	private DefaultTableModel musicData;
	private JScrollPane scrollPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SpotifyGUI window = new SpotifyGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SpotifyGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Spotify");
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setForeground(new Color(0, 0, 255));
		frame.getContentPane().setForeground(new Color(255, 255, 0));
		frame.setBounds(100, 100, 1100, 600);
		frame.getContentPane().setLayout(null); // Here we set the layout absolutely in terms of pixels. 
		
		lblViewSelector = new JLabel("Select View");
		lblViewSelector.setForeground(SystemColor.text);
		lblViewSelector.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblViewSelector.setBounds(55, 49, 82, 22);
		frame.getContentPane().add(lblViewSelector);
		
		radioSelectAlbum = new JRadioButton("Albums");
		radioSelectAlbum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(radioSelectAlbum.isSelected()) {
					radioSelectSong.setSelected(false);
					radioSelectArtist.setSelected(false);
					
					musicData = getAlbumData();
					tblData.setModel(musicData);
					//System.out.println("ALbum selected");
				}
				
			}
		});
		radioSelectAlbum.setForeground(Color.GREEN);
		radioSelectAlbum.setFont(new Font("Tahoma", Font.BOLD, 12));
		radioSelectAlbum.setBackground(Color.DARK_GRAY);
		radioSelectAlbum.setBounds(56, 94, 109, 22);
		radioSelectAlbum.setSelected(true);
		frame.getContentPane().add(radioSelectAlbum);
		
		radioSelectArtist = new JRadioButton("Artists");
		radioSelectArtist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(radioSelectArtist.isSelected()) {
					radioSelectAlbum.setSelected(false);
					radioSelectSong.setSelected(false);
					
					musicData = getArtistData();
					tblData.setModel(musicData);
					//System.out.println("Artist selected");
				}
				
			}
		});
		radioSelectArtist.setBackground(Color.DARK_GRAY);
		radioSelectArtist.setForeground(Color.GREEN);
		radioSelectArtist.setFont(new Font("Tahoma", Font.BOLD, 12));
		radioSelectArtist.setBounds(56, 127, 109, 23);
		frame.getContentPane().add(radioSelectArtist);
		
		radioSelectSong = new JRadioButton("Songs");
		radioSelectSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(radioSelectSong.isSelected()) {
					radioSelectAlbum.setSelected(false);
					radioSelectArtist.setSelected(false);
					
					musicData = getSongData();
					tblData.setModel(musicData);
					//System.out.println("Song selected");
				}
				
			}
		});
		radioSelectSong.setBackground(Color.DARK_GRAY);
		radioSelectSong.setForeground(Color.GREEN);
		radioSelectSong.setFont(new Font("Tahoma", Font.BOLD, 12));
		radioSelectSong.setBounds(56, 165, 109, 23);
		frame.getContentPane().add(radioSelectSong);
		
		txtSearch = new JTextField();
		txtSearch.setBackground(SystemColor.inactiveCaptionBorder);
		txtSearch.setBounds(46, 371, 177, 31);
		frame.getContentPane().add(txtSearch);
		txtSearch.setColumns(10);
		
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(radioSelectSong.isSelected()) {
					
					musicData = Spotify.searchSongs(txtSearch.getText());
					tblData.setModel(musicData);
					//System.out.println("Search was clicked");
				}else if(radioSelectAlbum.isSelected()) {
					musicData = Spotify.searchAlbums(txtSearch.getText());
					tblData.setModel(musicData);
				}else if(radioSelectArtist.isSelected()) {
					musicData = Spotify.searchArtists(txtSearch.getText());
					tblData.setModel(musicData);
				}
				
			}
		});
		btnSearch.setToolTipText("Search by song, artist, or album name");
		btnSearch.setBounds(134, 413, 89, 23);
		frame.getContentPane().add(btnSearch);
		
		musicData = getAlbumData();
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(334, 72, 650, 400);
		frame.getContentPane().add(scrollPane);
		tblData = new JTable(musicData);
		scrollPane.setViewportView(tblData);
		tblData.setFillsViewportHeight(true);
		tblData.setShowGrid(true);
		tblData.setGridColor(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
	
	private DefaultTableModel getSongData(){
		String sql = "SELECT * from song;";
		 
		
		try {
			
			DbUtilities db = new DbUtilities();
			
			return db.getDataTable(sql);  
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
		}
		return null;
		
		
	}	
	
	private DefaultTableModel getArtistData(){
		String sql = "SELECT * from artist;";
		 
		
		try {
			
			DbUtilities db = new DbUtilities();
			
			return db.getDataTable(sql);  
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
		}
		return null;
		
		
	}	
	
	private DefaultTableModel getAlbumData(){
		String sql = "SELECT * from album;";
		 
		
		try {
			
			DbUtilities db = new DbUtilities();
			
			return db.getDataTable(sql);  
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
		}
		return null;
		
		
	}	
	//*******I wasn't sure how to make the Prepared Statement work in the Spotify class. **************
	
	/*
	private DefaultTableModel getSongData(String searchTerm){
		String sql = "SELECT song_id, title, length, release_date, record_date FROM song ";
		//DefaultTableModel dataTable; You don't have to instantiate it.
		if(!searchTerm.equals("")){
				sql += " WHERE title LIKE '%?%';";
		}
		
		try {
			System.out.println(sql);
			DbUtilities db = new DbUtilities();
			ResultSet rs;
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, searchTerm);
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
		        String SongTitle = rs.getString(1);
		        String[] columnNames = {"Song ID", "Title", "Length", "Release Date", "Record Date"};
		        return db.getDataTable(SongTitle, columnNames);
			}
			
			//String[] columnNames = {"Song ID", "Title", "Length", "Release Date", "Record Date"};
			//dataTable = db.getDataTable(sql);  part of instantiation.

			db.closeDbConnection();
			//return db.getDataTable(SongTitle, columnNames); // You can just use shortcut here. 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
		}
		return null;
	}
	*/
	
	
}
