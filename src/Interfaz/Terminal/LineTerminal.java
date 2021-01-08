package Interfaz.Terminal;

import Perfomance.QuickTerminal;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LineTerminal extends JDialog implements Terminal
{
    int y=0;
    JTextArea textArea;
    private int userInputStart = 0;

    public LineTerminal()
    {
        textArea = new JTextArea();
        textArea.setBackground(Color.black);
        textArea.setForeground(Color.white);
        ((AbstractDocument) textArea.getDocument()).setDocumentFilter(new ProtectedDocumentFilter(this));

        InputMap im = textArea.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = textArea.getActionMap();

        Action oldAction = am.get("insert-break");
        am.put("insert-break", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int range = textArea.getCaretPosition() - userInputStart;
                try {
                    String text = textArea.getText(userInputStart, range).trim();
                    System.out.println("[" + text + "]");
                    userInputStart += range;

                } catch (BadLocationException ex) {
                    Logger.getLogger(QuickTerminal.class.getName()).log(Level.SEVERE, null, ex);
                }
                oldAction.actionPerformed(e);
            }
        });

        this.setSize(500,500);
        textArea.setCaretColor(Color.yellow);
        textArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN,17));
        this.getContentPane().add(new JScrollPane(textArea));
    }

    protected void updateUserInputPos() {
        int pos = textArea.getCaretPosition();
        textArea.setCaretPosition(textArea.getText().length());
        userInputStart = pos;

    }

    @Override
    public int getUserInputStart() {
        return userInputStart;
    }

    @Override
    public void appendText(String text) {
        textArea.append(text);
        updateUserInputPos();
    }


    public class ProtectedDocumentFilter extends DocumentFilter {

        private UserInput userInput;

        public ProtectedDocumentFilter(UserInput userInput) {
            this.userInput = userInput;
        }

        public UserInput getUserInput() {
            return userInput;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (offset >= getUserInput().getUserInputStart()) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (offset >= getUserInput().getUserInputStart()) {
                super.remove(fb, offset, length); //To change body of generated methods, choose Tools | Templates.
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (offset >= getUserInput().getUserInputStart()) {
                super.replace(fb, offset, length, text, attrs); //To change body of generated methods, choose Tools | Templates.
            }
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(700,700);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        LineTerminal lt = new LineTerminal();
        JButton as = new JButton("AA");
        as.addActionListener(a->{
            lt.setVisible(true);
        });
        f.getContentPane().add(as);
        f.setVisible(true);
    }
}
