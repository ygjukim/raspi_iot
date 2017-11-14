package com.example.devices;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.i2c_dev.drivers.TMP102Device;

import jdk.dio.DeviceManager;
import jdk.dio.DeviceNotFoundException;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedDeviceTypeException;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.gpio.PinEvent;
import jdk.dio.gpio.PinListener;

@Component
public class TempAlertController {
	private static final String ALERT_PIN = "GPIO23";
	  
	private GPIOPin alertPin = null;
	private TMP102Device tmp102Dev = null;
	private BlinkLedDevice blinkLed = null;
	
	@Autowired
	public TempAlertController(TMP102Device tmp102Dev, BlinkLedDevice blinkLed) 
			throws UnsupportedDeviceTypeException, DeviceNotFoundException, UnavailableDeviceException, IOException {
		this.tmp102Dev = tmp102Dev;
		this.blinkLed = blinkLed;
		
		alertPin = DeviceManager.open(ALERT_PIN, GPIOPin.class);
		
		alertPin.setInputListener(new PinListener() {
			@Override
			public void valueChanged(PinEvent event) {
				// TODO Auto-generated method stub
				boolean value = event.getValue();
				
				if (value) {	// HIGH
					blinkLed.stopBlink();
					System.out.println("Current temp. goes below low limit temp...");
				}
				else {			// LOW
					blinkLed.startBlink();
					System.out.println("Current temp. goes over high limit temp...");
				}
			}			
		});      
	}
	
	public double getTemperature() {
		return tmp102Dev.readTempC();
	}
	
	public double getLowLimitTemp() {
		return tmp102Dev.readLowLimitTempC();
	}
	
	public double getHighLimitTemp() {
		return tmp102Dev.readHighLimitTempC();
	}
		
	public void setLimitTemperature(double lowLimit, double highLimit) {
		if (highLimit != 0 && lowLimit >= highLimit)
			return;		// no operation
		
		if (lowLimit != 0) {
			tmp102Dev.setLowLimitTempC(lowLimit);
		}
		
		if (highLimit != 0) {
			tmp102Dev.setHighLimitTempC(highLimit);
		}
	}	
}
