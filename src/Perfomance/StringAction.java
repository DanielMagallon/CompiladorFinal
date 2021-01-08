
package Perfomance;
        
import java.awt.*;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.text.*;

public class StringAction extends JFrame
{
     public StringAction()
     {
          JPanel panel = new JPanel();
          setContentPane( panel );

          JTextPane textPane = new JTextPane();
          textPane.setFont( new Font("monospaced", Font.PLAIN, 12) );
          JScrollPane scrollPane = new JScrollPane( textPane );
          scrollPane.setPreferredSize( new Dimension( 200, 200 ) );
          panel.add( scrollPane );
          setTabs( textPane, 4 );
     }

     public void setTabs( JTextPane textPane, int charactersPerTab)
     {
          FontMetrics fm = textPane.getFontMetrics( textPane.getFont() );
          int charWidth = fm.charWidth( 'w' );
          int tabWidth = charWidth * charactersPerTab;

          TabStop[] tabs = new TabStop[10];

          for (int j = 0; j < tabs.length; j++)
          {
               int tab = j + 1;
               tabs[j] = new TabStop( tab * tabWidth );
          }

          TabSet tabSet = new TabSet(tabs);
          SimpleAttributeSet attributes = new SimpleAttributeSet();
          StyleConstants.setTabSet(attributes, tabSet);
          int length = textPane.getDocument().getLength();
          textPane.getStyledDocument().setParagraphAttributes(0, length, attributes, true);
     }

     public static void main(String[] args)
     {
          StringAction frame = new StringAction();
          frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
          frame.pack();
          frame.setVisible(true);
     }
}