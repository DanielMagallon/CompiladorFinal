package Interfaz.TextEditor;

import Abstract.Variables;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


import Interfaz.Compilador;
import Interfaz.MyOptionPane;
import Managers.ColorManager;
import Managers.DragDropListener;
import Managers.DropFiles;
import Managers.FileManager;
import Managers.SugerenceTextField;
import compiler.DataDefinition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Highlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

@SuppressWarnings("serial")
public class TabPanel extends JPanel implements DocumentListener, DragDropListener 
{

	private LineNumberingTextArea lineNumberingTextArea;
	private JTextPane textArea;
	public LabelTitle refTitle;

	public void refresh(){
	    lineNumberingTextArea.updateLineNumbers();
	    lineNumberingTextArea.repaint();
    }
        
        public void insertAfText(int posicion, String v) 
        {
             try {
                
                 textArea.getDocument().insertString(posicion-1, v+" ", Variables.attr);
                 
//                 Analisis.perfomance(new StringBuilder(text()));
                 
            } catch (BadLocationException ex) {
                Logger.getLogger(TabPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void insertBefText(int indOf, String cad,boolean antes)
        {
             try {
                
                 
                 textArea.getDocument().insertString(indOf,antes ? cad :cad+" ", Variables.attr);
                 
//                 Analisis.perfomance(new StringBuilder(text()));
            } catch (BadLocationException ex) {
                Logger.getLogger(TabPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void deleteText(int indOf, int toInd)
        {
            try {
                
                if(indOf!=0 && textArea.getText(indOf-1, 1).isEmpty())
                    indOf--;
                
                textArea.getDocument().remove(indOf, toInd+1);
//                 Analisis.perfomance(new StringBuilder(text()));
            } catch (BadLocationException ex) {
                Logger.getLogger(TabPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void replaceText(int indOf, int toInd,String newString)
        {
            try {
                
                textArea.getDocument().remove(indOf, toInd);
                textArea.getDocument().insertString(indOf,newString,Variables.attr);
                
//                 Analisis.perfomance(new StringBuilder(text()));
            } catch (BadLocationException ex) {
                Logger.getLogger(TabPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        private static SugerenceTextField textSerach;
        private static JPopupMenu menuEmer;
        private static ListaSugerencias listSuge;
        private static  Color entryBg = Color.gray;
        private static   Highlighter hilit;
        private static   Highlighter.HighlightPainter painterErr,painterWar;
        
        static{
            hilit = new DefaultHighlighter();
            painterErr = new DefaultHighlighter.DefaultHighlightPainter(new Color(0xFB542B));
            painterWar = new DefaultHighlighter.DefaultHighlightPainter(new Color(0xFFF632));
            
        } 
        
	public TabPanel(String path) {
		
                
		setName(path);
		setLayout(new BorderLayout());
		
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
                addWordListener();
		
		new DropFiles(textArea, this);
                
                textArea.setHighlighter(hilit);
                textArea.setSelectionColor(Color.YELLOW);
		textArea.getDocument().addDocumentListener(this);
                
		textArea.setCaretColor(Color.orange);
//		textArea.setBackground(ColorManager.ESCALA_AZUL.getColorScaleOf(1));
		textArea.setBackground(ColorManager.ESCALA_AZUL.getColorScaleOf(2));
		textArea.setForeground(Color.white);
		textArea.setFont(new Font("Monospaced", textArea.getFont().getStyle(), 16));
                
		scrollPane.setViewportView(textArea);
		lineNumberingTextArea  = new LineNumberingTextArea(textArea,textArea.getFont(),
				ColorManager.ESCALA_AZUL.getColorScaleOf(5),ColorManager.ESCALA_AZUL.getMaxiumScaleColor());
		
		scrollPane.setRowHeaderView(lineNumberingTextArea);
		
	}
        
        
    public void marcar(int from,int to)
    {
        textArea.select(from, to);
    }


    private int findLastNonWordChar (String text, int index) 
    {
    while (--index >= 0) {
        if (String.valueOf(text.charAt(index)).matches("\\W")) {
            break;
        }
    }
    return index;
    }

    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }
    
        
        
        private void addWordListener()
        {
            final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
                Color.green);
        final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, 
                Color.white);
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                
                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) 
                    {
                        if (text.substring(wordL, wordR).matches(DataDefinition.regex.toString()))
                            setCharacterAttributes(wordL, wordR - wordL, attr, false);
                        else
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);

                if (text.substring(before, after).matches(DataDefinition.regex.toString()))
                {
                    setCharacterAttributes(before, after - before, attr, false);
                } else {
                    setCharacterAttributes(before, after - before, attrBlack, false);
                    }
                }
            };
        textArea = new JTextPane(doc);
        
        menuEmer = new JPopupMenu(); 
        listSuge = new ListaSugerencias(this);
        textSerach = new SugerenceTextField(new KeyAdapter() 
        {   
                @Override
                public void keyPressed(KeyEvent ke) 
                {
                    if(ke.getKeyCode() == KeyEvent.VK_DOWN || 
                       ke.getKeyCode() == KeyEvent.VK_RIGHT)
                        listSuge.requestFocus();
                }

                @Override
                public void keyReleased(KeyEvent ke) 
                {
                    listSuge.filterByBegin(textSerach.getText());
                }
                
                
            
        });
        
        menuEmer.add(textSerach);
        
       menuEmer.add(new JScrollPane(listSuge));
        
        textArea.addKeyListener(new KeyAdapter() 
        {
                @Override
                public void keyPressed(KeyEvent me) 
                {
                    if(me.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        listSuge.resetTabCount();
                    }
                    else if(me.getKeyCode() == KeyEvent.VK_TAB)
                     {
                         listSuge.incrementTab();
                     }
                    /*if(!me.isControlDown())
                    {
                        Compilador.getInstance().undoList.check = true;
                        Compilador.getInstance().undoList.time = System.currentTimeMillis();
                    }*/
                    
//                    Compilador.getInstance().typing=true;
                    else if(me.isControlDown() && me.getKeyCode() == KeyEvent.VK_SPACE)
                    {   
                        
                        getText_Position();
                        
                        menuEmer.show(textArea, textArea.getCaret().getMagicCaretPosition().x,
                                textArea.getCaret().getMagicCaretPosition().y+20);
                        
                        
                        textSerach.requestFocus();
                    }
                 
                    hilit.removeAllHighlights();   
                    textArea.repaint();
                }

              
                
        });
        
    }
        
        private static int caretPosion_text,finalCaretPosText;
        
        private void getText_Position()
        {
            caretPosion_text = textArea.getCaretPosition();
            finalCaretPosText=0;
         
            try {
                while(true)
                {
                
                    if(textArea.getText(caretPosion_text-1,1).trim().equals("")){
                        break;
                    }
                    finalCaretPosText++;
                    caretPosion_text--;
                }
                
            }
            catch (BadLocationException ex) {
                   
               }
           
            finally{
                try {
                    textSerach.setText(textArea.getText(caretPosion_text,finalCaretPosText));
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
            
        }
     
        public void makeAction(String text, int positionCaret,boolean tres)
        {
            try {
                
                
                String newText = tres ? String.format(text, listSuge.tabCount,listSuge.tabCount,listSuge.tabCount)
                : String.format(text, listSuge.tabCount,listSuge.tabCount);
                
                menuEmer.setVisible(false);
                listSuge.defaultFilter();
                
                textArea.getDocument().remove(caretPosion_text, finalCaretPosText);
                
                int caret = textArea.getCaretPosition();
                
                textArea.getDocument().insertString(caretPosion_text, newText, null);
                
                if(positionCaret!=-1)
                {
                    
                    textArea.setCaretPosition(caret+positionCaret);
                }
                
            } catch (Exception ex) {
                
            }
        }
        
        public void markType(long index, int length,int type)
        {
            try {
                hilit.addHighlight((int)index, (int)index+length, type == -1 ? painterErr : painterWar);
                textArea.repaint();
            } catch (BadLocationException ex) {
                Logger.getLogger(TabPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
	public void labelTitulo(LabelTitle label) {
		refTitle = label;
		
	}
	
	public String text() {
		return textArea.getText();
	}
	
	public void text(String text) {
		textArea.setText(text);
	}
	
		@Override
	    public void insertUpdate(DocumentEvent documentEvent)
	    {
	        lineNumberingTextArea.updateLineNumbers();
	        if(refTitle.isSaved()) {
	        	refTitle.unsaveLabel();
	        }
	    }

	    @Override
	    public void removeUpdate(DocumentEvent documentEvent)
	    {
	        lineNumberingTextArea.updateLineNumbers();
	        if(refTitle.isSaved()) {
	        	refTitle.unsaveLabel();
	        }
	    }

	    @Override
	    public void changedUpdate(DocumentEvent documentEvent)
	    {
	        lineNumberingTextArea.updateLineNumbers();
	        if(refTitle.isSaved()) {
	        	refTitle.unsaveLabel();
	        }
	    }

	    private String nombre;
		@Override
		public void onDragEntered(DataFlavor[] data, Transferable tr, DropTargetDragEvent dtde)
				throws UnsupportedFlavorException, IOException {
			// TODO Auto-generated method stub
			if(data[1].isFlavorTextType())
			{
				
				DataFlavor selectedFlavor = DataFlavor.selectBestTextFlavor(data);
				
				if(tr.getTransferData(selectedFlavor) instanceof Reader)
				{				
					BufferedReader bf = new BufferedReader(((Reader) tr.getTransferData(selectedFlavor)));
					int size=0;
					String c;
					
					while((c=bf.readLine())!=null)
					{
						size++;
						
						if(size==2)
						{
							dtde.rejectDrag();
							ruta=null;
							return;
						}
						ruta = c;
					}
					
					ruta = ruta.replace("file://", "").trim();
					if(validExtension(ruta))
					{
						System.out.println(nombre = new File(ruta).getName());
					}
					else {
						denegate(dtde);
					}
				}
				else dtde.rejectDrag();
			}
		}

		@Override
		public void onDragExited() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDrop(DropTargetDropEvent et, Component comp) throws UnsupportedFlavorException, IOException {
			// TODO Auto-generated method stub
			if(ruta!=null)
			{
				Compilador.getInstance().addDocument(nombre, ruta, FileManager.loadFile(ruta));
			}
		}

		@Override
		public boolean validExtension(String ext) {
			// TODO Auto-generated method stub
			return ext.endsWith(".ryj");
		}
		
		String ruta;
		private void denegate(DropTargetDragEvent dtde) {
			dtde.rejectDrag();
			ruta = null;
		}

    
}
