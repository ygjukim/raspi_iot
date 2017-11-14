package com.example.devices;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import jdk.dio.DeviceManager;
import jdk.dio.gpio.GPIOPin;

@Component
public class BlinkLedDevice extends Thread{
    public static final String LED_PIN = "GPIO18";
    public static final int interval = 500;
    
    private GPIOPin ledPin = null;
    private Semaphore blinkSem = null;
    private boolean bRun = true;
    private boolean bBlink = false;
    
    public BlinkLedDevice() throws IOException, InterruptedException {
        ledPin = DeviceManager.open(LED_PIN, GPIOPin.class);
        ledPin.setValue(false);

        bRun = true;
        bBlink = false;
        blinkSem = new Semaphore(1);
        blinkSem.acquire();
    }

    @PreDestroy
    public void close() throws IOException, InterruptedException {
    	bRun = false;
    	Thread.sleep(interval);
    	if (ledPin != null) {
    		ledPin.close();
    	}
    }

    public void startBlink(){
    	 bBlink = true;
    	 blinkSem.release();
    }
    
    public void stopBlink() {
		 bBlink = false;
    }

    public void end() {
        this.bRun = false;
    }
    
    public void run() {
        while (bRun) {
            try {
				  blinkSem.acquire();
			  } catch (InterruptedException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  }
        		
            while (bBlink) {
            	  try {
//    				  System.out.println("Led on...");
					  ledPin.setValue(true);
	                Thread.sleep(interval);

	                if (!bRun || !bBlink) {
	                	  ledPin.setValue(false);
	                	  break;
	                  }
	                  
//	                System.out.println("Led off...");
	                ledPin.setValue(false);
	                Thread.sleep(interval);
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             }	// while
        }		// while
    }
    
}