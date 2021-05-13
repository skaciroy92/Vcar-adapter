package com.inw.adapter;

import java.net.Socket;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingTimer {
	
	protected static final Logger logger = LoggerFactory.getLogger(PingTimer.class);

	 protected static PingTimer obj; 
	    public static void pingDevice(Socket s) throws InterruptedException 
	    { 
	        obj = new PingTimer(); 
	          
	        //creating a new instance of timer class 
	        Timer timer = new Timer(); 
	        TimerTask task = new Helper(); 
	  
	        //instance of date object for fixed-rate execution 
	        Date date = new Date(); 
	  
	        timer.scheduleAtFixedRate(task, date, 5000); 
	          
	        logger.info("Ping Timer running"); 
	        synchronized(obj) 
	        { 
	            //make the main thread wait 
	            obj.wait(); 
	              
	            //once timer has scheduled the task 4 times,  
	            //main thread resumes 
	            //and terminates the timer 
	            timer.cancel(); 
	              
	            //purge is used to remove all cancelled  
	            //tasks from the timer'stak queue 
	            logger.info(" timer.purge() : " + timer.purge()); 
	        } 
	    }
	
}
