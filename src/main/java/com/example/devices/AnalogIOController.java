package com.example.devices;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.i2c_dev.drivers.PCF8591Device;

@Component
public class AnalogIOController {
	private PCF8591Device pcf8591dev = null;
	
	@Autowired
	public AnalogIOController(PCF8591Device pcf8591dev) {
		this.pcf8591dev = pcf8591dev;
	}
	
	@PreDestroy
	public void close() {
		this.pcf8591dev.close();
	}
	
	public int analogRead(int pin) {
		return this.pcf8591dev.analogRead(pin);
	}
	
	public void analogWrite(int pwm) throws IOException {
		this.pcf8591dev.analogWrite(pwm);
	}
}
