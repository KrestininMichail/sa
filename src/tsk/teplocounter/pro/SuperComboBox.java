package tsk.teplocounter.pro;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class SuperComboBox extends JComboBox {

    private static Vector<String> words = new Vector<>();

    public static Vector<String> getWords() {
        words.add("012456");
        words.add("222222");
        words.add("345543");
        words.add("345771");
        return words;
    }
    public SuperComboBox(){}
    public SuperComboBox(Vector<String> words){
        super(words);
        setEditable(true);
        setPreferredSize(new Dimension(80, 30));

        ComboBoxEditor editor = getEditor();
        JTextField textField = (JTextField) editor.getEditorComponent();

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if(!(Character.isDigit(c)
                        || (c == KeyEvent.VK_BACK_SPACE)
                        || (c == KeyEvent.VK_DELETE)))
                {
                    e.consume();
                }
            }
        });
        AutoCompleteTextDocument doc = new AutoCompleteTextDocument(textField);
    }

    private class AutoCompleteTextDocument extends PlainDocument{
        //text component in which work document
        private JTextComponent comp;
        private int beforeCompletion = 3;
        public AutoCompleteTextDocument (JTextComponent comp){
            this.comp = comp;
            comp.setDocument(this);
        }



        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            //current position in text
            int end = offs + str.length();
            //define a current word position
            final int wordStart = Utilities.getWordStart(comp, offs);
            //length of current word
            int wordLength = end - wordStart;

            if(str == null) return;

            //check is it possible to end the word
            if(wordLength >= beforeCompletion){
                //receive a current word
                String word = getText(wordStart, offs - wordStart) + str;
                //attempting to find full option in list
                String wholeWord = "";
                for(String next : words){
                    if(next.startsWith(word)){
                        //word is found
                        wholeWord = next;
                        break;
                    }
                }
                //if word is found
                if(wholeWord.length() > 0){
                    //cut the part for autfilling
                    final String toComplete = wholeWord.substring(wordLength);
                    //positon for selection this part
                    final int startPos = offs + str.length();
                    final int endPos = end + toComplete.length();
                    //add a chunk to the text
                    str = str + toComplete;
                    //delayed task for selecting chunk
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                //add selected chunk
                                comp.setSelectionStart(startPos);
                                comp.setSelectionEnd(endPos);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }

                    });
                }
            }
            //a parent method to add a text
            if((getLength() + str.length()) <= 6)
                super.insertString(offs, str, a);
        }

    }

}
