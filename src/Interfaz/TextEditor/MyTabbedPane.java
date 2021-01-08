package Interfaz.TextEditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

import Abstract.Variables;
import Interfaz.Compilador;
import Interfaz.MyOptionPane;

@SuppressWarnings("serial")
public class MyTabbedPane extends JTabbedPane implements ChangeListener
{

	public int ind=-1;
	
	public MyTabbedPane() 
	{
		super(TOP);
		
		addChangeListener(this);
		this.setUI(new MetalTabbedPaneUI() {
			   @Override
			   protected int calculateTabWidth(int tabPlacement, int tabIndex,
			       FontMetrics metrics) {
			     int width = super.calculateTabWidth(tabPlacement, tabIndex, metrics);
			     int extra = tabIndex * 10;
			     return width + extra;
			   }
			   @Override
			protected int calculateMaxTabHeight(int arg0) {
				// TODO Auto-generated method stub
				return 30;
			}
			
		});
//		
	}
	
	private String c;
	public MyTabbedPane set(String cad)
	{
		c=cad;
		return this;
	}
	
	@Override
	public void addTab(String title, Component component) 
	{
		ind++;
		TabPanel tb = (TabPanel)component;
		MyPanel panelTit = new MyPanel(ind,title,tb);
		mytb=panelTit;
		
		
		
		tb.labelTitulo(panelTit.lbltitle);
		if(c!=null)
		tb.text(c);
		
		super.addTab(title, component);
		
		setTabComponentAt(ind, panelTit);
		this.setSelectedIndex(ind);
		
		 
	}
	
	
	
	public class MyPanel extends JPanel{
		 
		public int indice;
		private LabelTitle lbltitle;
		public TabPanel tabRef;
		
		public MyPanel(int ind,String title,TabPanel r) 
		{
			tabRef = r;
			add(new JLabel(Variables.fileLinux));
			add(lbltitle = new LabelTitle(title));
			indice=ind;

			JLabel close = new JLabel("X");
			close.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mouseClicked(MouseEvent e) 
				{
					
					close();
				}
				
				@Override
				public void mouseEntered(MouseEvent e) 
				{
					close.setForeground(Color.red);
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					close.setForeground(Color.black);
				}
			});
			
			add(close);
			setOpaque(false);
			
		}
		
		public boolean close()
		{
			if(!lbltitle.isSaved()) {
				int opc = MyOptionPane.showConfirm(this, "Desea guardar el archivo"+lbltitle+"?", 
						"Guardar archivo", JOptionPane.YES_NO_CANCEL_OPTION); 
				
				if(opc==JOptionPane.YES_OPTION)
					Compilador.getInstance().save(tabRef);
				
				else if(opc==JOptionPane.CANCEL_OPTION)
					return false;
			}
			
			MyTabbedPane.this.remove(indice);
			MyTabbedPane.this.ind--;
			
			int var = MyTabbedPane.this.getTabCount()-indice;
			for(int i=1; i<=var; i++)
			{
				--((MyPanel) MyTabbedPane.this.getTabComponentAt(indice)).indice;
//				((MyPanel) MyTabbedPane.this.getTabComponentAt(indice)).save();
				indice++;
			}
			
			if(MyTabbedPane.this.ind==-1) 
			{
				Compilador.getInstance().panelEditor.remove(Compilador.getInstance().tabbedPane);
//					ref.compVisible = ref.welcome;
				Compilador.getInstance().panelEditor.add(Compilador.getInstance().welcome);
					
					/*if(ref.split)
						ref.splitPane.setRightComponent(ref.welcome);
					
					else {
						ref.contentPane.remove(ref.tabbedPane);
						ref.contentPane.add(ref.welcome,"Center");
					}*/
					
				Compilador.getInstance().panelEditor.validate();
				Compilador.getInstance().panelEditor.repaint();
			}
			
			return true;
		}
	}
        
        
        public MyPanel getActual(){
            return mytb;
        }

	private MyPanel mytb;
	
	public void closeTab()
	{
		if(mytb!=null)
			mytb.close();
	}
	
	
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		if(this.getSelectedIndex()!=-1)
		{
			Component c = this.getTabComponentAt(this.getSelectedIndex());
			
			if(c!=null)
				mytb = (MyPanel) c; 
		}else mytb=null;
		
	}
}
