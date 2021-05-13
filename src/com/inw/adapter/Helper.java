package com.inw.adapter;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Helper extends TimerTask 
{ 
    public static int i = 0; 
	protected static final Logger logger = LoggerFactory.getLogger(Helper.class);

    public void run() 
    { 
        logger.info("Timer ran " + ++i); 
        if(i == 4) 
        { 
            synchronized(PingTimer.obj) 
            { 
            	PingTimer.obj.notify(); 
            } 
        } 
    } 
      
} 