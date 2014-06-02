import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;





public class House{	
	static final int ItemPerPage = 32;
	

	public static void main(String[] args) {
		try {
			// get URL content
			URL url;
			url = new URL("http://buy.sinyi.com.tw/index.php/j3g8t4");
			URLConnection conn = url.openConnection();
			
			conn.addRequestProperty("User-Agent", "Mozilla/6.0 (Windows NT 6.2; WOW64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1");
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
 
			String inputLine;
 
			//save to this filename
			String fileName = "test.html";
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
 
			System.out.println("Done");
 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//--parse html
		try {
			File input = new File("test.html");
			Document doc;
//			doc = Jsoup.parse(input, "UTF-8");
			doc = Jsoup.parse(input, "BIG5");
			
			// find total num = totalNum
			Elements totalNums = doc.select("div>h1>span");
			Element totalNume = totalNums.get(0);
			Integer totalNum = Integer.parseInt(totalNume.text().trim());
			System.out.println("totalNum="+totalNum);
			
			// total pages = totalPages
			Integer totalPage =  totalNum/ItemPerPage;
			if(totalPage*ItemPerPage < totalNum){	
				totalPage++;
			}
			System.out.println("totalPage="+totalPage);
			
			// get page items			
			Elements items = doc.select("div>table[class^=datagrid]>tbody>tr");
			System.out.println(items.size());

			//	64 (32 pair), 
			//	first = addr of item, 
			//	second = detail of item.
			/*
			--first
			<tr class="addr">
			    <td colspan="3" class="overall"><a href="http://buy.sinyi.com.tw/house/08679T.html" target="_blank">桃園縣桃園市冠倫大國小資美宅(08679T) </a></td>
			    <td colspan="5" class="overall">
			        <div style="float:left;">桃園縣桃園市  大有路</div>
			        <ul class="func" style="float:right;">
			            <li><a href="http://buy.sinyi.com.tw/house/08679T.html#life-info" target="_blank"><img src="http://img.sinyi.com/i/new_item_page/07_live.gif" alt="桃園縣桃園市買屋,冠倫大國小資美宅,08679T" title="冠倫大國小資美宅(08679T)" width="17" height="17" /></a></li>
			            <li><a href="http://buy.sinyi.com.tw/house/08679T.html#basic-data" target="_blank"><img src="http://img.sinyi.com/i/icon06.gif" alt="互動看屋" title="互動看屋" width="17" height="17" /></a></li>
			            <li><a href="http://buy.sinyi.com.tw/buy_to_street.php/08679T" target="_blank"><img src="http://img.sinyi.com/i/icon07.gif" alt="街景" title="街景" width="17" height="17" /></a></li>
			            <li><a href="http://buy.sinyi.com.tw/house/08679T.html?3" target="_blank"><img src="http://img.sinyi.com/i/icon08.gif" alt="地圖" title="地圖" width="17" height="17" /></a></li>
			            <li><a href="http://buy.sinyi.com.tw/house/08679T.html?4" target="_blank"><img src="http://img.sinyi.com/i/icon09.gif" alt="格局圖" title="格局圖" width="17" height="17" /></a></li>
			        </ul>
			    </td>
			</tr>
			
			--second
			<tr>
			    <td class="photo">
			        <p>        <a href="http://buy.sinyi.com.tw/house/08679T.html" target="_blank"><img src="http://img.sinyi.com/h/02/b/A/08679TA.JPG" alt="桃園縣買屋,桃園市買屋,冠倫大國小資美宅,08679T" title="冠倫大國小資美宅(08679T)" width="89" height="67" border="0" /></a></p>
			    </td>
			    <td class="overall">
			        <p>總價：<span>450萬</span></p>
			        <p class="lower-price">最優價<em>398萬</em></p>
			        <a href="http://www.sinyi.com.tw/lightbox/eba_frm.php?u=http%3A%2F%2Fbuy.sinyi.com.tw%2Findex.php%2Fj3g8t4&t=5L%2Bh576p5oi%2F5bGLLeahg%2BWckue4o%2Bahg%2BWckuW4gue4veWDuTB%2BMTAwMOiQrOaIv%2BWxi%2BOAgeahg%2BWckuahg%2BWckuiyt%2BWxi%2Biyt%2BaIv%2BWtkCDigJMg5L%2Bh576p5oi%2F5bGL&n=08679T"  class="fancybox-frame appoint">預約看屋</a><a href="http://buy.sinyi.com.tw/house/08679T.html"  class="detail" target="_blank">詳細資訊</a>
			        <p>
			            <!--單價：-->
			        </p>
			    </td>
			    <td class="overall">建　坪：16.19坪</br>主＋陽：12.02坪</br></td>
			    <td>電梯大樓<br>套房<br>住宅</td>
			    <td>1/1/1/0</td>
			    <td>5/19</td>
			    <td class="agency"><img src="http://img.sinyi.com/i/logo-agent.jpg" alt="信義房屋" /><br />信義房屋<br /><br /></td>
			    <td class="trace" title="08679T"><a href="javascript:void(0);" class="check">勾選</a></td>
			</tr>
						 
			 */
			

			//-- find price of item
			// done
//			Elements item = items.select("td>p>span");
			Elements price = items.select("p>em");
			System.out.println("em="+price.size());
			for(int i=0;i<price.size();i++){
//				System.out.println("price="+price.get(i).text());
			}
			
			
			String text = price.first().text();
			byte[] array = text.getBytes("UTF-8");
			String s = new String(array, Charset.forName("UTF-8"));
//			System.out.println("s="+s);
			
			
			//-- find Ping of item
			// done
			Elements ping = items.select("td[class^=overall]");
			System.out.println("ping="+ping.size());
			for(int i=0;i<ping.size();i++){
				if((i%4)==3){
//					System.out.println(ping.get(i).text());
				}
			}
			
			
			
			//-- find seq of item 
			// done
			Elements seq = items.select("td[class^=trace]");
			System.out.println("seq="+seq.size());
			for(int i=0;i<seq.size();i++){
//				System.out.println(seq.get(i).attr("title"));
			}
//			System.out.println(seq.attr("title"));
			//<td class="trace" title="08679T">
			
			
			//--print all
			for(int i=0;i<price.size();i++){
//			for(int i=0;i<1;i++){
				
				System.out.println("--------------");
				System.out.println("title="+seq.get(i).attr("title"));				
				System.out.println("price="+price.get(i).text());
				System.out.println("ping="+ping.get((i*4)+3).text());
				ConvertLatlng.convert(seq.get(i).attr("title"));
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}

		
		System.out.println("parse end");
	}
	
	
}

