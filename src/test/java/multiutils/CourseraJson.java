package multiutils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import pt.ornrocha.jsonutils.MTUJsonIOUtils;
import pt.ornrocha.webutils.connectionutils.WEBConnection;
import pt.ornrocha.webutils.connectionutils.WebConnectionException;
import pt.ornrocha.webutils.connectionutils.downloaders.HTTPFileDownloader;
import pt.ornrocha.webutils.connectionutils.gui.DownloadProgressPanel;

public class CourseraJson {

	public static void main(String[] args) throws IOException, WebConnectionException {
		//String file="/home/orocha/statistical-genomics";
        //MTUJsonIOUtils.ConvertJsonFileToPrettyJson(file, "/home/orocha/stats-genomic");
		String urll="https://d3c33hcgiwev3.cloudfront.net/6UV3LLooEeWROg75ViZp8Q.processed/full/720p/index.mp4?Expires=1463011200&Signature=H6mrs9Z5G5JAoLomaVR-fmYzUDLH9VEP1WAEAO-PClSBUivqXHh6B3jPI0sMWVE71rk9sWoFKH4YaqvpxQMin0QSQP5-kuJjqt4Onqz3XevW2diBXCYH7pRUv34BxKtojI~VMDNqmSx2cBf2AhI95D35rQJGEiHCawQdmc4Ul2w_&Key-Pair-Id=APKAJLTNE6QMUY6HBC5A";
		WEBConnection conn =new WEBConnection(urll);
		FileOutputStream outputStream = new FileOutputStream("/home/orocha/vid.mp4");
		InputStream in=conn.doConnection();
		
		 byte[] buffer = new byte[4096];
         int bytesRead = -1;
         long totalBytesRead = 0;
         int percentCompleted = 0;
         
         boolean keepalive=true;
        

         while ((bytesRead = in.read(buffer)) != -1 && keepalive) {
             outputStream.write(buffer, 0, bytesRead);
             totalBytesRead += bytesRead;

         }

         outputStream.close();
         
	}

}
