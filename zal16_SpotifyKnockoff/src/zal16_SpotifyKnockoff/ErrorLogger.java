package zal16_SpotifyKnockoff;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorLogger {
	public static void log(String errorMessage) {
		// Save the following information to errorlog.txt
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		

		try {
	
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ");
			Date date = new Date();

			

			fw = new FileWriter("C:\\Users\\Zachary\\git\\SpotifyKnockoff\\zal16_SpotifyKnockoff\\src\\zal16_SpotifyKnockoff\\errorlog.txt", true);
			bw = new BufferedWriter(fw);
			bw.write(dateFormat.format(date) +  errorMessage + "\n");

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		
		}
		
		
	}	
		
}
