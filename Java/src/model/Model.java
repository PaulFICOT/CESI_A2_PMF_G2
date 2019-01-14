package model;
import java.io.BufferedReader;
import java.util.Observable;
import java.util.Observer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import view.View;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Model implements SerialPortEventListener {

	public SerialPort serialPort;

	private int x = 1;

	private static Float tr_mod;

	public static Float getTr_mod() {
		return tr_mod;
	}

	public static void setTr_mod(Float tr_mod) {
		Model.tr_mod = tr_mod;
	}




	private static Float t_mod;
	
	public static Float getT_mod() {
		return t_mod;
	}

	public static void setT_mod(Float t_mod) {
		Model.t_mod = t_mod;
	}
	
	


	public static Float température_fridge;
	public static Float température_ext;
	public static Float Humidité;
	public static Float count;
	public static Float point_rosée_c;
	//public static int compterLine = 1; 
	//public static String SetPoint;
	public float SetPoint;
	
	
	

	private static final String PORT_NAMES[] = {
			"/dev/tty.usbserial-A9007UX1",

			"/dev/ttyUSB0",
			
			"COM11"

	};

	public static BufferedReader input;
	public static OutputStream output;
	public static final int TIME_OUT = 2000;

	/** Default bits per second for COM port. */

	public static final int DATA_RATE = 9600;

	public void initialize() {

		CommPortIdentifier portId = null;

		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {

			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

			for (String portName : PORT_NAMES) {

				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}

		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {

			// open serial port, and use class name for the appName.

			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();
			char ch = 1;
			output.write(ch);

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);

		} catch (Exception e) {

			System.err.println(e.toString());
		}
	}

	public synchronized void close() {

		if (serialPort != null) {

			serialPort.removeEventListener();

			serialPort.close();
		}
	}

	public synchronized void serialEvent(SerialPortEvent oEvent) {

		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

			try {
				stringToInt();  //-------------------------------------------------------------------------------
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

	public synchronized void writeData(String data) {

		System.out.println("Sent: " + data);

		try {
			output.write(data.getBytes());
		} catch (Exception e) {
			System.out.println("could not write to port");
		}

	}

	
	public static Float getTempInt() {
		return température_fridge;
	}


	/*public static String getSetPoint() {
		return SetPoint;
	}

	public static void setSetPoint(String setPoint) {
		SetPoint = setPoint;
	}*/

	public static Float getTempExt() {
		return température_ext;
	}

	public static Float getHumidity() {
		return Humidité;
	}


	public static Float getTemps() {
		return count;
	}


	public static Float getDewPoint() {
		return point_rosée_c;
	}


public void stringToInt() throws NumberFormatException, IOException {
		
		switch(x ) {
		case 1:
			t_mod = Float.parseFloat(input.readLine());
			System.out.println("Tension de la thermistance : " + t_mod + " V");
			x ++;
			break;
		case 2:
			tr_mod = Float.parseFloat(input.readLine());
			System.out.println("Résistance de la thermistance : " + tr_mod + " Ohms");
			x ++;
			break;
		case 3:
			température_ext = Float.parseFloat(input.readLine());
			System.out.println("Température extérieur : " + température_ext + " °C");
			x ++;
			break;
		case 4:
			Humidité = Float.parseFloat(input.readLine());
			System.out.println("Humidité : " + Humidité + " %");
			x ++;
			break;
		case 5:
			température_fridge = Float.parseFloat(input.readLine());
			System.out.println("Température du frigo : " + température_fridge + " °C");
			x ++;
			break;
		case 6:
			point_rosée_c = Float.parseFloat(input.readLine());
			System.out.println("point de rosée en °C : " + point_rosée_c + " °C");
			x++ ;
			break;
		case 7:
			count = Float.parseFloat(input.readLine());
			System.out.println("Temps : " + count );
			x = 1 ;
			break;
		default:
			System.out.println("Maybe a problem ?");
			break;
		}
			
			
		
	}
}