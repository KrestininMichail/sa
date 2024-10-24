package tsk.teplocounter.pro;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.*;

public class MainView extends JFrame {
    FirstBitTable dataTable = new FirstBitTable();
    ParametersTable paramTable = new ParametersTable();

    private Buttons b = new Buttons();
    private static Font font = new Font(null, 1, 16);


    private JPanel tabSA = new JPanel();
    private JPanel statusPanel = new JPanel(new BorderLayout());
    private JPanel tabVIST = new JPanel();
    private JPanel view = new JPanel(new FlowLayout());

    private static JPanel view_connectSA = BoxLayoutUtils.createVerticalPanel();
    private JPanel superComboBoxPanel = new JPanel(new GridLayout(1,0,0,5));

    private static JPanel comPortPanel = new JPanel();
    private static JPanel baudRatePanel = new JPanel(new GridLayout(1,0,0,5));
    private static JLabel status = new JLabel("Введите номер теплосчётчика и нажмите кнопку \"Соединиться\"");
    private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    private static SuperComboBox superComboBox = new SuperComboBox(SuperComboBox.getWords());

    //BoxLayout boxLayout = new BoxLayout(tableAndStatus, BoxLayout.Y_AXIS);

    public static JPanel getView_connectSA() {
        return view_connectSA;
    }

    public static JPanel getComPortPanel() {
        return comPortPanel;
    }

    public static JPanel getBaudRatePanel() {
        return baudRatePanel;
    }

    public static Font getMyFont() {
        return font;
    }

    public static JLabel getStatus() {
        return status;
    }

    public MainView(){
        super("Программа для съёма показаний теплосчётчиков");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        keyBinding();


        comPortPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        TitledBorder tl_comPortPanel = new TitledBorder("<html><font size =3>Настройки COM-порта");
        comPortPanel.setBorder(new CompoundBorder(tl_comPortPanel, new EmptyBorder(40,20,10,20)));

        TitledBorder tl_baudRatePanel = new TitledBorder("<html><font size =3>Скорость");
        baudRatePanel.setBorder(new CompoundBorder(tl_baudRatePanel, new EmptyBorder(40,20,20,20)));

        superComboBox.setFont(font);
        TitledBorder tl_numberCount = new TitledBorder("<html><font size =3>Номер теплосчётчика");
        superComboBoxPanel.setBorder(new CompoundBorder(tl_numberCount, new EmptyBorder(40,20,10,20)));

        superComboBoxPanel.add(superComboBox);

        b.comPortButtons();
        b.comPortBaudRate();
        b.connectSA();

        statusPanel.setLayout(new BorderLayout());

        tabbedPane.addTab("<html><font size =4>SA-94", tabSA);
        tabSA.setLayout(new BorderLayout());
        view.add(superComboBoxPanel);
        view.add(comPortPanel);
        view.add(baudRatePanel);

        view_connectSA.add(Box.createHorizontalStrut(180));
        view_connectSA.add(b.getConnectSA());
        view_connectSA.add(Box.createVerticalStrut(10));
        view_connectSA.add(new JScrollPane(dataTable));
        view_connectSA.add(Box.createVerticalStrut(30));
        view_connectSA.add(Box.createHorizontalStrut(100));
        view_connectSA.add(new JScrollPane(paramTable));


        statusPanel.add(status, BorderLayout.SOUTH);

        tabSA.add(view,BorderLayout.NORTH);
        tabSA.add(view_connectSA,BorderLayout.CENTER);
        tabSA.add(statusPanel, BorderLayout.SOUTH);
       // tabbedPane.addTab("ВИС.Т", tabVIST);


        setLayout(new GridLayout());
        add(tabbedPane);
        setSize(600, 600);
        setVisible(true);
    }

     public static int getItemSuperComboBox(){
        return Integer.parseInt(superComboBox.getSelectedItem().toString());
    }

    private  void keyBinding() {
        Action action = new AnAction();
        action.putValue(action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK, true));
        Buttons.getConnectSA().getActionMap().put("action", action);
        Buttons.getConnectSA().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put((KeyStroke)action.getValue(action.ACCELERATOR_KEY), "action");
    }

    class AnAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e){
            new Buttons.ConnectSA().actionPerformed(e);
        }
    }

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable(){
//            public void run(){
//                new MainView();
//            }
//        });

//        try {// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if (info.getName().equals("Nimbus")) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            //e.printStackTrace();
//        }
        SwingUtilities.invokeLater(() -> new MainView());

    }
}
