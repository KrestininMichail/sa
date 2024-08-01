package tsk.teplocounter.pro;

import jssc.SerialPortException;
import tsk.teplocounter.pro.tsk.teplocounter.pro.SuperComboBox;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainView extends JFrame {
    @Override
    public Font getFont() {
        return font;
    }

    ComPorts cp = new ComPorts();

    private Font font = new Font(null, 1, 16);
    private JPanel tabSA = new JPanel();
    private JPanel tabVIST = new JPanel();
    private JPanel comPortPanel = new JPanel();
    private JPanel baudRaute = new JPanel(new GridLayout(1,0,0,5));
    private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    private JButton connectSA = new JButton("Соединиться");
    private SuperComboBox superComboBox = new SuperComboBox(SuperComboBox.getWords());




    public MainView(){
        super("Программа для съёма показаний теплосчётчиков");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        comPortPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        TitledBorder tl = new TitledBorder("Настройки COM-порта");
        tl.setTitleFont(font);
        comPortPanel.setBorder(new CompoundBorder(tl, new EmptyBorder(30,30,30,30)));

        comPortButtons();
        comPortBaudRate();

        connectSA.setFont(font);
        connectSA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(ComPorts.getBautRate());
                System.out.println(ComPorts.getPortName());


                try {
                    ComPorts.connect(ComPorts.getPortName(), ComPorts.getBautRate());
                } catch (SerialPortException spe) {
                    spe.printStackTrace();
                }
            }
        });

        tabbedPane.addTab("<html><font size =4>SA-94", tabSA);
        tabSA.setLayout(new FlowLayout());
        tabSA.add(superComboBox);
        tabSA.add(comPortPanel);
        tabSA.add(baudRaute);
        tabSA.add(connectSA);


        tabbedPane.addTab("ВИС.Т", tabVIST);

        setLayout(new GridLayout());
        add(tabbedPane);
        setSize(800, 600);
       // pack();
        setVisible(true);

    }
    String[] comports = cp.portNames();
    JToggleButton [] tbs = new JToggleButton[comports.length];
    public void comPortButtons() {
        ButtonGroup bg = new ButtonGroup();


        for (int i = 0; i < comports.length; i++) {
            if(i == 0){
                ToggleCOM tc = new ToggleCOM();
                tbs[i] = new JToggleButton(comports[i], true);
                tbs[i].setFont(font);
                tbs[i].addActionListener(tc);
                bg.add(tbs[i]);
                comPortPanel.add(tbs[i]);
                ComPorts.setPortName(comports[i]);
                continue;
            }

            if(i > 3)
                break;

            ToggleCOM tc = new ToggleCOM();
            tbs[i] = new JToggleButton(comports[i], false);
            tbs[i].addActionListener(tc);
            tbs[i].setFont(font);
            bg.add(tbs[i]);
            comPortPanel.add(tbs[i]);

        }
    }

    class ToggleCOM implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ComPorts.setPortName(e.getActionCommand());
        }
    }

    private void comPortBaudRate() {
        baudRaute.setBorder(BorderFactory.createTitledBorder("<html><font size =3>Скорость, бод"));
        ButtonGroup bg = new ButtonGroup();
        JRadioButton jrb2400 = new JRadioButton("<html><font size =4>2400", false);
        JRadioButton jrb4800 = new JRadioButton("<html><font size =4>4800", true);
        ComPorts.setBaudRate(4800);
        bg.add(jrb2400);
        bg.add(jrb4800);
        baudRaute.add(jrb2400);
        baudRaute.add(jrb4800);
        jrb2400.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int sel = e.getStateChange();
                if (sel == ItemEvent.SELECTED)

                ComPorts.setBaudRate(2400);

            }
        });
        jrb4800.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int sel = e.getStateChange();
                if (sel == ItemEvent.SELECTED)
                ComPorts.setBaudRate(4800);

            }
        });

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new MainView();
            }
        });

    }
}
