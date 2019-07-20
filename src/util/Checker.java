package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/**
 * @author: Nikhil Bhole
 * Health checker: automatically hits URLs on scheduled time and returns feedback.
 * 		  If a URL is down, It automatically sends E-Mail to respective authority.
 * 		  Which are mentioned in other text file.
 */


public class Checker extends TimerTask { 

	final String filePath = "/home/nikhil/url.txt";
	
	public static List<String> readFile(String filePath) {
		ArrayList<String> file_contents = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				file_contents.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return file_contents;
	}
	
	public void run() {
		List<String> url = readFile(filePath);
		// List<String> receiverMails = readFile("/home/nikhil/mail.txt");//read
		// receivers mail from text file.
		try {
			Date date = new Date();
			System.out.println("Checking out at: " + date);
			for (int i = 0; i < url.size(); i++) {
				String u = url.get(i);
				if (pingURL(u, 2000)) {
					System.out.println(u + " is up.");
				} else {
					System.out.println(u + " is down.");
				}
			}
		} catch (Exception e) {

		}

	}

	/*
	 * public static boolean pingHost(String host, int port, int timeout) { try
	 * (Socket socket = new Socket()) { socket.connect(new InetSocketAddress(host,
	 * port), timeout); return true; } catch (IOException e) { return false; //
	 * Either timeout or unreachable or failed DNS lookup. } }
	 */
	
	
	public static void main(String[] args) throws ParseException {
		// the Date and time at which you want to execute
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormatter.parse("2018-03-12");
		// Now create the time and schedule it
		Timer timer = new Timer();
		// Use this if you want to execute it once
		// timer.schedule(new AutoTime(), date);
		// Use this if you want to execute it repeatedly
		int period = 300000;// 15 Mins
		timer.schedule(new Checker(), date, period);
	}
	
	/**
	 * Pings a HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in 
	 * the 200-399 range.
	 * @param url The HTTP URL to be pinged.
	 * @param timeout The timeout in millis for both the connection timeout and the response read timeout. Note that
	 * the total timeout is effectively two times the given timeout.
	 * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
	 * given timeout, otherwise <code>false</code>.
	 */
	public static boolean pingURL(String url, int timeout) {
	    url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

	    try {
	        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
	        connection.setConnectTimeout(timeout);
	        connection.setReadTimeout(timeout);
	        connection.setRequestMethod("HEAD");
	        int responseCode = connection.getResponseCode();
	        return (200 <= responseCode && responseCode <= 399);
	    } catch (IOException exception) {
	        return false;
	    }
	}

}

