/************************************************************************** 
 * Orlando Rocha (ornrocha@gmail.com)
 *
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 *  
 */
package pt.ornrocha.webutils.connectionutils;

import java.net.HttpURLConnection;

public class InterruptConnectionThread implements Runnable {

    HttpURLConnection con;
    long timeout=60000;
    boolean canceldisconnect=false;
    
    public InterruptConnectionThread(HttpURLConnection con) {
        this.con = con;
    }
    
    public InterruptConnectionThread(HttpURLConnection con, long timeout) {
        this.timeout = timeout;
    }
    
    public void cancelDisconnect(boolean bol){
    	this.canceldisconnect=bol;
    }
    
    public boolean getcanceldisconnectstate(){
    	return this.canceldisconnect;
    }

    public void run() {
        try {
            Thread.sleep(timeout); 
        } catch (InterruptedException e) {
               
        }
        if(!canceldisconnect){
            con.disconnect();
           System.out.println("Timer thread forcing to quit connection");
        }
    }
}
