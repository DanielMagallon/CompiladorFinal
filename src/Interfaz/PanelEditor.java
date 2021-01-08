package Interfaz;

import Abstract.Variables;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import Interfaz.HiddenComps.ComponentHidden;
import Interfaz.TextEditor.LineNumberingTextArea;
import Managers.ColorManager;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

public class PanelEditor extends JPanel 
{

	private JPanel panelArchivos,panelTextA,panelAnalizadores,panelHideAll;
	private LineNumberingTextArea linesLexico,linesSinta;
	public JTextPane textAreaLexico,textAreaSintactico;
	private JPanel panelS;
	private JPanel panelL;
//	private JPanel panelLexOpc;
	private JSplitPane splitPane = new JSplitPane();
	
	private GridLayout grid;
	public ComponentHidden componentHidden;
        
	public PanelEditor() {
		setLayout(new BorderLayout(0, 0));
		
		
		splitPane.setResizeWeight(0.6);
		splitPane.setOneTouchExpandable(true);
		add(splitPane, BorderLayout.CENTER);
		 BasicSplitPaneDivider divider = ( (BasicSplitPaneUI) splitPane.getUI()).getDivider();
		  divider.setDividerSize(15);
		    divider.setBorder(BorderFactory.createTitledBorder
		    (divider.getBorder(), "Custom border title -- gets rid of the one-touch arrows!"));
		
		panelTextA = new JPanel();
		panelTextA.setBorder(new EmptyBorder(3, 3, 3, 3));
		splitPane.setRightComponent(panelTextA);
		panelTextA.setLayout(new BorderLayout(0, 0));
		
		
		panelAnalizadores = new JPanel();
		panelTextA.add(panelAnalizadores, BorderLayout.CENTER);
		grid = new GridLayout(2, 1, 0, 5);
		panelAnalizadores.setLayout(grid);
		
		panelL = new JPanel();
		panelL.setBorder(new TitledBorder(new LineBorder(new Color(255, 255, 255)), "Analizador Lexico", TitledBorder.LEFT, TitledBorder.TOP, null, Color.WHITE));
		panelAnalizadores.add(panelL);
		panelL.setLayout(new BorderLayout(0, 0));
		
		panelS = new JPanel();
		panelS.setBorder(new TitledBorder(new LineBorder(new Color(255, 255, 255)), "Analizador Sintactico", TitledBorder.LEADING, TitledBorder.TOP, null, Color.WHITE));
		panelAnalizadores.add(panelS);
		panelS.setLayout(new BorderLayout(0, 0));
		
		panelArchivos = new JPanel();
		splitPane.setLeftComponent(panelArchivos);
		panelArchivos.setLayout(new BorderLayout(0, 0));
		
		JScrollPane srollLex = new JScrollPane();
		textAreaLexico = new JTextPane();
                textAreaLexico.setCaretColor(Color.yellow);
		textAreaLexico.setBackground(ColorManager.ESCALA_AZUL.getMaxiumScaleColor());
		textAreaLexico.setForeground(Color.white);
		textAreaLexico.setFont(new Font("Monospaced", textAreaLexico.getFont().getStyle(), 16));
		linesLexico = new LineNumberingTextArea(textAreaLexico, textAreaLexico.getFont(), 
				ColorManager.ESCALA_AZUL.getColorScaleOf(3), Color.black);
		srollLex.setViewportView(textAreaLexico);
		srollLex.setRowHeaderView(linesLexico);
		panelL.add(srollLex);
		
		textAreaSintactico = new JTextPane();
                textAreaSintactico.setCaretColor(Color.yellow);
		textAreaSintactico.setBackground(ColorManager.ESCALA_AZUL.getMaxiumScaleColor());
		textAreaSintactico.setForeground(Color.white);
		JScrollPane srollSint = new JScrollPane();
		textAreaSintactico.setFont(new Font("Monospaced", textAreaSintactico.getFont().getStyle(), 16));
		linesSinta = new LineNumberingTextArea(textAreaSintactico, textAreaSintactico.getFont(), 
				ColorManager.ESCALA_AZUL.getColorScaleOf(3), Color.black);
		srollSint.setViewportView(textAreaSintactico);
		srollSint.setRowHeaderView(linesSinta);
		panelS.add(srollSint);
		
		textAreaLexico.getDocument().addDocumentListener(new DocListener(linesLexico));
		
//		panelLexOpc = new JPanel();
//		panelL.add(panelLexOpc, BorderLayout.NORTH);
		textAreaSintactico.getDocument().addDocumentListener(new DocListener(linesSinta));
		
		panelHideAll = new JPanel();
                componentHidden = new 
                    ComponentHidden(ALL_COMPONENTS, Compilador.getInstance().panelHides, this::hide, this::show);
		panelHideAll.add(componentHidden);
		panelHideAll.add(new ComponentHidden(ANALIZADOR_LEX, Compilador.getInstance().panelHides, this::hide, this::show));
		panelHideAll.add(new ComponentHidden(ANALIZADOR_sin, Compilador.getInstance().panelHides, this::hide, this::show));
		panelTextA.add(panelHideAll, BorderLayout.NORTH);
                componentHidden.vclick();
                
	}
	
	private final String ALL_COMPONENTS = "Todo";
	private final String ANALIZADOR_LEX = "AL";
	private final String ANALIZADOR_sin = "AS";
	
//        private ArrayList<JButton> botonesRec = new ArrayList<>();
//        
//        public void addButttons()
//        {
//            botonesRec.forEach(btn -> textAreaSintactico.insertComponent(btn));
//        }
//        
//        public void removeAllbtns()
//        {
//            botonesRec.clear();
//        }
        
        public void addError(String text)
        {
            
            try {
                textAreaSintactico.getDocument().insertString(textAreaSintactico.getDocument().getLength(), text,
                        Variables.attr);
            } catch (BadLocationException ex) {
                Logger.getLogger(PanelEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void addRecSintactico(String text,int posicion, int compSize)
        {
            
            JButton rec=null;
            
           if(text.equalsIgnoreCase("delete"))
           {
               rec = new JButton("Eliminar");
               
               rec.addActionListener(a->
                    {
                        
                        Compilador.getInstance().tabbedPane.getActual().tabRef.deleteText(posicion, compSize);

                    }
               );
               
           }
           else if(text.startsWith("addAf="))
           {
               String v = text.split("=")[1];
                rec = new JButton("(AF) Agregar token : "+v);   
                
                rec.addActionListener(a->
                    {
                        
                        Compilador.getInstance().tabbedPane.getActual().tabRef.insertAfText(posicion, v);

                    }
               );
                
           }
           else if(text.startsWith("add="))
           {
               String v = text.split("=")[1];
                rec = new JButton("(AB) Agregar token: "+v);   
                
                rec.addActionListener(a->
                    {
                        
                        Compilador.getInstance().tabbedPane.getActual().tabRef.insertBefText(posicion, v,false);

                    }
               );
                
           }  
           else if(text.startsWith("addFirst="))
           {
               String v = text.split("=")[1];
                rec = new JButton("(firsy) Agregar token: "+v);   
                
                rec.addActionListener(a->
                    {
                        
                        Compilador.getInstance().tabbedPane.getActual().tabRef.insertBefText(posicion, v,true);

                    }
               );
                
           }
           else if(text.startsWith("mod="))
           {
               String v = text.split("=")[1];
               rec = new JButton("Cambiar token por: "+v);
               
               rec.addActionListener(a->
               {
                   Compilador.getInstance().tabbedPane.getActual().tabRef.replaceText(posicion, compSize,v);
               });
               
           }else{
               JOptionPane.showMessageDialog(null, "Hay error en recomendaciones "+text);
           }
           
           textAreaSintactico.insertComponent(rec);
        }
        
	public void hide(Object arg)
	{
		ComponentHidden comp = (ComponentHidden) arg;
		switch(comp.getText())
		{
			case  ALL_COMPONENTS:
				displayOnlyArch();
				break;
				
			case ANALIZADOR_LEX:
				removeFromAnalizadores(panelL);
				break;
				
			case ANALIZADOR_sin:
				removeFromAnalizadores(panelS);
				break;
		}
		panelHideAll.remove(comp);
		panelHideAll.repaint();
		panelHideAll.validate();
		
		
	}
	
	public void removeFromAnalizadores(JPanel panel)
	{
		panelAnalizadores.remove(panel);
		grid.setRows(panelAnalizadores.getComponentCount());
		panelAnalizadores.repaint();
		panelAnalizadores.validate();
	}
	
	public void addInAnalizadores(JPanel panel) {
		panelAnalizadores.add(panel);
		grid.setRows(panelAnalizadores.getComponentCount());
		panelAnalizadores.repaint();
		panelAnalizadores.validate();
	}
	
	public void show(Object arg)
	{
		ComponentHidden comp = (ComponentHidden) arg;
		switch(comp.getText())
		{
			case  ALL_COMPONENTS:
				displayAnalizadores();
				break;
				
			case ANALIZADOR_LEX:
				addInAnalizadores(panelL);
				break;
				
			case ANALIZADOR_sin:
				addInAnalizadores(panelS);
				break;
		}
		
		panelHideAll.add(comp);
		panelHideAll.repaint();
		panelHideAll.validate();
	}
	
	
	public void displayOnlyArch() {
		this.splitPane.remove(panelArchivos);
		this.remove(splitPane);
		this.add(panelArchivos);
		this.validate();
	}
	
	public void displayOnlyAnalizadores() {
		this.remove(splitPane);
		this.add(panelTextA);
		this.validate();
	}
	
	
	public void displayAnalizadores() {
		this.add(splitPane);
		this.splitPane.setLeftComponent(panelArchivos);
		this.validate();
	}
	
	public void agregaAPanelArchivos(Component c) 
	{
		panelArchivos.removeAll();
		panelArchivos.add(c);
		panelArchivos.validate();
	}
	
	class DocListener implements DocumentListener
	{
		private LineNumberingTextArea lines;
		
		public DocListener(LineNumberingTextArea l) 
		{
			lines = l;
		}
		
		@Override
		public void changedUpdate(DocumentEvent arg0) 
		{
			lines.updateLineNumbers();
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) 
		{
			lines.updateLineNumbers();
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) 
		{
			lines.updateLineNumbers();
		}
	}

	

}
