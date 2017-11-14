package com.example.raspi_iot;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.devices.AnalogIOController;
import com.example.devices.BlinkLedDevice;
import com.example.devices.LedController;
import com.example.devices.TempAlertController;

@RestController
@EnableAutoConfiguration
public class RaspiIotController {
	private LedController ledController = null;
	private AnalogIOController adcController = null;
	private BlinkLedDevice blinkLed = null;
	private TempAlertController tempController = null;
	
	@Autowired
	public RaspiIotController(LedController ledController, 
			AnalogIOController adcController,
			BlinkLedDevice blinkLed,
			TempAlertController tempCpntroller) {
		this.ledController = ledController;
		this.adcController = adcController;
		this.blinkLed = blinkLed;
		this.tempController = tempCpntroller;
		
		blinkLed.start();
	}

	@RequestMapping("/")
	@ResponseBody
	public String home() {
		return "Welcome to Raspi IoT Service...";
	}
	
	@RequestMapping(value="/ledOn/{ledId}", method=RequestMethod.GET)
	@ResponseBody
	public String ledOn(@PathVariable("ledId") String ledId) {
		if (ledController != null) {
			try {
				int ledNum = Integer.parseInt(ledId);
				ledController.turnOn(ledNum);
			} catch(NumberFormatException e) {
				return "Wrong led number!!!";
			}			
		}
		return "#" + ledId + " Led On...";		
	}
	
	@RequestMapping(value="/ledOff",  method=RequestMethod.GET)
	@ResponseBody
	public String ledOff(@RequestParam("led") String ledId) {
		if (ledController != null) {
			try {
				int ledNum = Integer.parseInt(ledId);
				ledController.turnOff(ledNum);
			} catch(NumberFormatException e) {
				return "Wrong led number!!!";
			}			
		}
		return "#" + ledId + " Led Off...";		
	}
	
	@RequestMapping(value="/dimming",  method=RequestMethod.GET)
	@ResponseBody
	public String dimming(@RequestParam("pwm") String pwm) {
		if (adcController != null) {
			try {
				int pwmValue = Integer.parseInt(pwm);
				if (pwmValue < 0 || pwmValue > 255)
					throw new NumberFormatException();
				adcController.analogWrite(pwmValue);
			} catch(NumberFormatException e) {
				return "Wrong pwm value!!!";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return "Error in dimming...";
			}			
		}
		return "Dimming to " + pwm + " ...";		
	}

	@RequestMapping(value="/getVR",  method=RequestMethod.GET)
	@ResponseBody
	public String getVR() {
		int vrValue = 0;
		if (adcController != null) {
			vrValue = adcController.analogRead(0);
		}
		return "Potentimeter Value = " + vrValue;
	}
	
	@RequestMapping(value="/getPR",  method=RequestMethod.GET)
	@ResponseBody
	public String getPR() {
		int prValue = 0;
		if (adcController != null) {
			prValue = adcController.analogRead(1);
		}
		return "Photoresistor Value = " + prValue;
	}
	
	@RequestMapping(value="/getTM",  method=RequestMethod.GET)
	@ResponseBody
	public String getTM() {
		int tmValue = 0;
		if (adcController != null) {
			tmValue = adcController.analogRead(2);
		}
		return "Themistor Value = " + tmValue;
	}
	
	@RequestMapping(value="/blinkLED/{action}", method=RequestMethod.GET)
	@ResponseBody
	public String blinkLED(@PathVariable("action") String action) {
		String response = null;
		if (action.equals("start")) {
			blinkLed.startBlink();
			response = "LED Blinking start...";
		}
		else if (action.equals("stop")) {
			blinkLed.stopBlink();
			response = "LED Blinking stop...";
		}
		else {
			response = "Wrong action parameter(start or stop)...";
		}
		
		return response;
	}
	
	@RequestMapping(value="/getTemp", method=RequestMethod.GET)
	@ResponseBody
	public String getTemp() {
		return "Current Temperature : " + tempController.getTemperature() + " C";
	}

	@RequestMapping(value="/temp_alert/getLimitTemp", method=RequestMethod.GET)
	@ResponseBody
	public String getLimitTemp() {
		return "Current Low Limit Temperature : " + tempController.getLowLimitTemp() + " C <br>"
				+ "Current High Limit Temperature : " + tempController.getHighLimitTemp() + " C";
	}
	
	@RequestMapping(value="/temp_alert/setLimitTemp", method=RequestMethod.GET)
	@ResponseBody
	public String setLimitTemp(@RequestParam("low") String lowLimit, @RequestParam("high") String highLimit) {
		double lowLimitTemp = (lowLimit != null && lowLimit.length() > 0) ? Double.parseDouble(lowLimit) : 0;
		double highLimitTemp = (highLimit != null && highLimit.length() > 0) ? Double.parseDouble(highLimit) : 0;
		tempController.setLimitTemperature(lowLimitTemp, highLimitTemp);
		
		return "Set to Low Limit Temperature : " + lowLimitTemp + " C <br>"
				+ "Set to High Limit Temperature : " + highLimitTemp + " C";
	}
		
}
