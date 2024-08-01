package tsk.teplocounter.pro;

import jssc.SerialPortException;


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

    ComPorts cp = new ComPorts();
    private Requests requests = new Requests();
    private Font font = new Font(null, 1, 16);
    private JPanel tabSA = new JPanel();
    private JPanel tabVIST = new JPanel();
    private JPanel comPortPanel = new JPanel();

    private JPanel superComboBoxPanel = new JPanel(new GridLayout(1,0,0,5));
    private JPanel baudRatePanel = new JPanel(new GridLayout(1,0,0,5));
    private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    private JButton connectSA = new JButton("Соединиться");
    private JLabel status = new JLabel("Введите номер теплосчётчика и нажмите кнопку \"Соединиться\"");
    private JPanel view = new JPanel(new FlowLayout());
    private JPanel view_connectSA = new JPanel(new FlowLayout());
    private static SuperComboBox superComboBox = new SuperComboBox(SuperComboBox.getWords());




    public MainView(){
        super("Программа для съёма показаний теплосчётчиков");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        comPortPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        TitledBorder tl_comPortPanel = new TitledBorder("<html><font size =3>Настройки COM-порта");
        comPortPanel.setBorder(new CompoundBorder(tl_comPortPanel, new EmptyBorder(40,20,10,20)));

        TitledBorder tl_baudRatePanel = new TitledBorder("<html><font size =3>Скорость");
        baudRatePanel.setBorder(new CompoundBorder(tl_baudRatePanel, new EmptyBorder(40,20,20,20)));

        superComboBox.setFont(font);
        TitledBorder tl_numberCount = new TitledBorder("<html><font size =3>Номер теплосчётчика");
        superComboBoxPanel.setBorder(new CompoundBorder(tl_numberCount, new EmptyBorder(40,20,10,20)));

        superComboBoxPanel.add(superComboBox);


        comPortButtons();
        comPortBaudRate();

        connectSA.setFont(font);
        connectSA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("Соединение...");
                requests.connect();
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
        tabSA.setLayout(new BorderLayout());
        view.add(superComboBoxPanel);
        view.add(comPortPanel);
        view.add(baudRatePanel);
        view_connectSA.add(connectSA);
        tabSA.add(view,BorderLayout.NORTH);
        tabSA.add(view_connectSA,BorderLayout.CENTER);
        tabSA.add(status, BorderLayout.SOUTH);


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

        ButtonGroup bg = new ButtonGroup();
        JRadioButton jrb2400 = new JRadioButton("<html><font size =4>2400", false);
        JRadioButton jrb4800 = new JRadioButton("<html><font size =4>4800", true);
        ComPorts.setBaudRate(4800);
        bg.add(jrb2400);
        bg.add(jrb4800);
        baudRatePanel.add(jrb2400);
        baudRatePanel.add(jrb4800);

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
    public static String getTextSuperComboBox(){
        return superComboBox.getSelectedItem().toString();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new MainView();
            }
        });

    }
}
