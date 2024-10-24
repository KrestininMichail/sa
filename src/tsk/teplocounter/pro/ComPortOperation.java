package tsk.teplocounter.pro;

import jssc.*;
import java.util.ArrayList;
import java.util.*;

import static java.lang.Integer.toBinaryString;

public class ComPortOperation {

   private static SerialPort serialPort;
   private static int baudRate = SerialPort.BAUDRATE_4800;
   private static String portName;

    private static ArrayList<Integer> codes = new ArrayList<>();
   static int[] dataInt;


   public static void setPortName(String pn){
        portName = pn;
    }

    public static String getPortName(){
       return portName;
    }

    public static void closePort() throws SerialPortException{
       serialPort.closePort();
    }

   public static void setBaudRate(int br){
        baudRate = br;
    }

    public static void changeBaudRate() {
        baudRate = (baudRate == SerialPort.BAUDRATE_2400) ? SerialPort.BAUDRATE_4800 : SerialPort.BAUDRATE_2400;
        System.out.println(baudRate);
    }

    public static int getBautRate(){
       return baudRate;
    }

    public static void connect(int number) throws SerialPortException {
        System.out.println(MainView.getItemSuperComboBox());
        serialPort = new SerialPort(portName);
        serialPort.openPort();
        setConnectParams(number);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run()  {
                try {
                if(MainView.getStatus().getText() == "Соединение...") {
                    serialPort.closePort();
                    System.out.println("порт закрыт");
                    if (baudRate == SerialPort.BAUDRATE_2400) {
                        changeBaudRate();
                        serialPort.openPort();
                        setConnectParams(number);
                        Buttons.getJrb4800().setSelected(true);

                    } else { //baudrate == 4800
                        changeBaudRate();
                        serialPort.openPort();
                        setConnectParams(number);
                        Buttons.getJrb2400().setSelected(true);
                        serialPort.writeIntArray(RequestsHandling.serialNumber(number));
                    }
                }

               }catch (SerialPortException e){
                    e.printStackTrace();
                }
            }
        }, 1500);



        new Timer().schedule(new TimerTask() {
                @Override
                public void run()  {
                    if (MainView.getStatus().getText() == "Соединение...") {
                        MainView.getStatus().setText("Нет ответа прибора");
                        changeBaudRate();
                        try {
                            serialPort.closePort();
                            System.out.println("Порт закрыт");
                            Buttons.getConnectSA().setText("Соединиться");
                        }catch (SerialPortException e){
                            e.printStackTrace();
                        }
                        if (baudRate == SerialPort.BAUDRATE_4800) {
                            Buttons.getJrb4800().setSelected(true);
                        } else {
                            Buttons.getJrb2400().setSelected(true);
                        }

                    }
                }
        }, 3000);

    }

    private static void setConnectParams(int number) throws SerialPortException {
        serialPort.setParams(baudRate,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_EVEN);
        serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
        serialPort.addEventListener(new SerialPortReader(serialPort));
        MainView.getStatus().setText("Соединение...");
        serialPort.writeIntArray(RequestsHandling.serialNumber(number));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run()  {
                new DataHandling().anylizeSA(codes);
                MainView.getStatus().setText("Ожидание подключения...");
            }
        }, 300);
    }

    static class SerialPortReader implements SerialPortEventListener {

       private SerialPort serialPort;

       public SerialPortReader(SerialPort serialPort){
           this.serialPort = serialPort;
       }
        public void serialEvent(SerialPortEvent event) {

           if(event.isRXCHAR()) {
               MainView.getStatus().setText("Происходит обмен данными");

           }
               try {
                  dataInt = serialPort.readIntArray(1, 500);
                  codes.add(dataInt[0]);

               } catch (SerialPortException e) {
                   System.out.println(e);

               } catch (SerialPortTimeoutException e) {
                   e.printStackTrace();
               }

        }
    }

    public static void stopSa(){
        codes.clear();
        if(serialPort.isOpened()){
            try {
                serialPort.writeInt(RequestsHandling.STOPBYTE);
            }catch (SerialPortException ex){
                MainView.getStatus().setText("Невозможно закрыть порт " + ComPortOperation.getPortName());
            }
        }
    }

    static String[] portNames() {
        String[] pn = SerialPortList.getPortNames();
        return pn;
       // return new String[] {"COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8"};

    }
}
