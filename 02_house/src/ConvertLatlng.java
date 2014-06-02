import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.LatLng;


public class ConvertLatlng {
	static final String convertFileName="convertTemp.html"; 
	
	public static String convert(String s){
		try {
			// get URL content
			URL url;			
			url = new URL("http://buy.sinyi.com.tw/house/"+s+".html?3");
			//http://buy.sinyi.com.tw/house/91864E.html?3
			URLConnection conn = url.openConnection();
			
			conn.addRequestProperty("User-Agent", "Mozilla/6.0 (Windows NT 6.2; WOW64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1");
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
 
			String inputLine;
 
			//save to this filename
			String fileName = convertFileName;
			File file = new File(fileName);
 
			if (!file.exists()) {
				file.createNewFile();
			}
 
			//use FileWriter to write file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
 
			while ((inputLine = br.readLine()) != null) {		
				bw.write(inputLine);
			}
 
			bw.close();
			br.close();
 
			System.out.println("Download convertTemp.html Done");
			
			//-- find latLng
			//center: new google.maps.LatLng(25.026016,121.298613),
			String convertStr = readFile();
			int start = convertStr.indexOf("center: new google.maps.LatLng");
			int end = convertStr.substring(start).indexOf("scaleControl");
//			System.out.println(start+","+end);
			String latLng = convertStr.substring(start+31, start+end-4);	// target item's latLng
			String lat = latLng.substring(0,latLng.indexOf(',')-1);
			String lng = latLng.substring(latLng.indexOf(',')+1,latLng.length());
//			System.out.println("latLng="+latLng);
//			System.out.println("lat="+lat);
//			System.out.println("lng="+lng);			
			
			String str = getLocationInfo(latLng);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	
	
	public static String getLocationInfo(String latLng) {

	    HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng="+latLng+"&language=zh-TW");
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	    String responseBody = new String();
	    try {
	        response = client.execute(httpGet);
	        HttpEntity entity = response.getEntity();

	        responseBody = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
//	        System.out.println("responseBody="+responseBody);
	    } catch (ClientProtocolException e) {
	        } catch (IOException e) {
	    }

	    String addr= new String(responseBody);
	    int start = addr.indexOf("formatted_address");
//	    System.out.println("start="+start);
	    
	    int end = addr.substring(start).indexOf("geometry");
//	    System.out.println("end="+end);
	    
	    String retStr = addr.substring(start, start+end);
	    System.out.println("retStr="+retStr);
	    return retStr;
	}
	
	
//	center: new google.maps.LatLng	
	static String readFile() throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(convertFileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}

		

	
	
	
}
