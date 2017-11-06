package com.example.devices;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import jdk.dio.DeviceManager;
import jdk.dio.gpio.GPIOPin;

@Component
public class LedController {
    public static final String[] LED_PINS = { "GPIO17", "GPIO27", "GPIO22" };
    private ArrayList<GPIOPin> gpioPins = null;

    public LedController() {
    	gpioPins = new ArrayList<GPIOPin>();
    	
    	for (int i=0; i<LED_PINS.length ; i++) {
    		GPIOPin pin;
			try {
				pin = DeviceManager.open(LED_PINS[i], GPIOPin.class);
	    		gpioPins.add(pin);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

    @PreDestroy
    public void close() {
		try {
	    	for (int i=0; i<gpioPins.size() ; i++) {
					gpioPins.get(i).close();
	    	}
	    	System.out.println("[LOG] Pre-destory method excuted...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }
    
    public void turnOn(int index) {
    	if (index >= 0 && index < gpioPins.size()) {
        	try {
				gpioPins.get(index).setValue(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    public void turnOff(int index) {
    	if (index >= 0 && index < gpioPins.size()) {
        	try {
				gpioPins.get(index).setValue(false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
}
