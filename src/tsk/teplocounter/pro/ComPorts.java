package tsk.teplocounter.pro;

import jssc.SerialPortList;
import jssc.SerialPort;
import jssc.SerialPortException;

public class ComPorts {
    private static SerialPort serialPort;
    private static int baudRate;
    private static String portName;


   public static void setPortName(String pn){
        portName = pn;
    }

    public static String getPortName(){
       return portName;
    }


   public static void setBaudRate(int br){
        baudRate = (br == 2400) ? 2400 : SerialPort.BAUDRATE_4800;

    }

    public static int getBautRate(){
       return baudRate;
    }

    public static void connect(String port, int baud) throws SerialPortException{
        serialPort = new SerialPort(port);
        serialPort.openPort();
        serialPort.setParams(baudRate,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_EVEN);


        //serialPort.closePort();

    }


    static String[] portNames() {
        String[] pn = SerialPortList.getPortNames();
        return pn;
       // return new String[] {"COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8"};

    }
}
