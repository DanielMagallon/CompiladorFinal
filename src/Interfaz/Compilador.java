package Interfaz;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import Interfaz.ArbolArchivos.TreeFiles;
import Interfaz.HiddenComps.PanelHideComponents;
import Interfaz.TextEditor.MyTabbedPane;
import Interfaz.TextEditor.TabPanel;
import Interfaz.TextEditor.MyTabbedPane.MyPanel;
import Interfaz.TextEditor.TablaSimbolos;
import Managers.ColorManager;
import Managers.FileManager;
import Perfomance.Compiler;
import compiler.Analyze;

import java.io.File;

public final class Compilador extends JFrame {

	private JPanel contentPane;
	public PanelEditor panelEditor;
	public MyTabbedPane tabbedPane;
        //public UndoListener undoList;
	private CreateFile askinfo;
	public static TreeFiles treeFiles;
	public static Notificacion notify;
        public CSource cSource;
	
        //public Timer timer;
        //public boolean typing=false;
        
	public PanelHideComponents panelHides;
	public PanelWelcome welcome;
	public JSplitPane splitPane;
	
	public TablaSimbolos tablaSimbolos;
	public Compiler compiler;

	private static Compilador instancia;
	public static Compilador getInstance() {
		return instancia;
	}
	private boolean showMsg=true;

	public About about;

	public Compilador() 
	{
		instancia = this;
		setTitle("Compile RYJ");

		about = new About();
		notify = new Notificacion();
		tablaSimbolos = new TablaSimbolos(this, true);
		//loaderGram = new LoadGrammar(this, true);

		cSource = new CSource(this,true);
		compiler = new Compiler();

                
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		

		setMinimumSize(new Dimension(500,500));
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(ColorManager.ESCALA_AZUL.getColorScaleOf(0));
		setJMenuBar(menuBar);
		
		JMenu mnArchivo = new JMenu("Archivo");
		mnArchivo.setForeground(Color.WHITE);
		menuBar.add(mnArchivo);
		
		JMenuItem mntmNuevo = new JMenuItem("Nuevo");
		mntmNuevo.setBackground(Color.black);
		mntmNuevo.addActionListener((a)->newTab());
		mntmNuevo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		mnArchivo.add(mntmNuevo);
		
		JMenuItem mntmCerrar = new JMenuItem("Cerrar");
		mntmCerrar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		mntmCerrar.addActionListener((a)->tabbedPane.closeTab());
		
		mntmAbrir = new JMenuItem("Abrir");
		mntmAbrir.addActionListener((a)-> FileManager.openFileChosser( objects->
			this.addDocument(objects[0].toString(), objects[1].toString(), objects[2].toString()) ));
		mntmAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		mnArchivo.add(mntmAbrir);
		mnArchivo.add(mntmCerrar);
		
		mntmGuadar = new JMenuItem("Guadar");
		mntmGuadar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		mntmGuadar.addActionListener((a)->save((TabPanel)tabbedPane.getSelectedComponent()));
		
		mntmCerrarTodo = new JMenuItem("Cerrar todo");
		mntmCerrarTodo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		mntmCerrarTodo.addActionListener((a)->this.closeAll());
		mnArchivo.add(mntmCerrarTodo);
		mnArchivo.add(mntmGuadar);
		
		mntmGuardarTodo = new JMenuItem("Guardar Todo");
		mntmGuardarTodo.addActionListener((a)->saveAll());
		mntmGuardarTodo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		mnArchivo.add(mntmGuardarTodo);

                
                {
                    JMenu menuCodigo = new JMenu("Codigo");
                    
                   /* JMenuItem sr = new JMenuItem("Buscar y reemplazar");
                    menuCodigo.add(sr);
                    sr.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
                    sr.addActionListener(a->{
                        
                        ctrlf_h.showSR( tabbedPane.getActual().tabRef);
                    });*/
                   
                    JMenuItem analisisLexico = new JMenuItem("Analizar");
                    menuCodigo.add(analisisLexico);
                    analisisLexico.addActionListener(a -> 
                    {
                        
                        Analyze.perfomance(tabbedPane.getActual().tabRef.text());
						tabbedPane.getActual().tabRef.refresh();

						try {
							
							if(showMsg){
								notify.notifyMsg("Ctrl+f5->Compila | Shit+f5 ejecuta").run();
								this.requestFocus();
								showMsg=false;
							}
							
							Toolkit toolkit = Toolkit.getDefaultToolkit();
							Clipboard clipboard = toolkit.getSystemClipboard();
							clipboard.setContents(new StringSelection(compiler.semantic.codeCreator.getCode()), null);

						}catch(NullPointerException ex){
							cSource.setOUT("");
						}

						/*
							Aqui compilar el archivo nada mas
						 */
                    });

					JMenuItem compile = new JMenuItem("Compilar");
					menuCodigo.add(compile);
					compile.addActionListener(a ->
					{
						cSource.compileCode(compiler.semantic.codeCreator);
					});

					JMenuItem run = new JMenuItem("Ejecutar");
					menuCodigo.add(run);
					run.addActionListener(a ->
							cSource.execute(compiler.semantic.codeCreator));

					JMenuItem ccode = new JMenuItem("Ver codigo C");
					menuCodigo.add(ccode);
					ccode.addActionListener(a ->
					{
						try {
							cSource.setOUT(compiler.semantic.codeCreator.getCode());
						}catch(NullPointerException ex){
							cSource.setOUT("");
						}
						cSource.setVisible(true);

					});

					run.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,ActionEvent.SHIFT_MASK));
					compile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.CTRL_MASK));
					analisisLexico.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK));
					ccode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));

                    menuBar.add(menuCodigo);
                   
                    
                      for(Component mi : menuCodigo.getMenuComponents())
                          if(mi instanceof JMenuItem)
                            mi.setBackground(Color.black);
                      
                    JMenu menuWindow = new JMenu("Programa");
                    menuBar.add(menuWindow);
                    
                    JMenuItem miRefresh = new JMenuItem("Refrescar");
                    miRefresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
                    miRefresh.addActionListener(a->{
                                panelEditor.componentHidden.vclick();
                                tabbedPane.getActual().validate();
                                tabbedPane.getActual().repaint();
                            });
                    menuWindow.add(miRefresh);
                    
                    menuWindow.addSeparator();
                    JMenuItem mtTableSim = new JMenuItem("Tabla de simbolos");
                    mtTableSim.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
                    mtTableSim.addActionListener(a->tablaSimbolos.setVisible(true));
                    menuWindow.add(mtTableSim);
                    
                    for(Component mi : menuWindow.getMenuComponents())
                          if(mi instanceof JMenuItem)
                            mi.setBackground(Color.black);
                }
                
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		tabbedPane = new MyTabbedPane();
		
		splitPane = new JSplitPane();
		splitPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0.1);
		
		panelHides = new PanelHideComponents();
		treeFiles = new TreeFiles();
		contentPane.add(panelHides, BorderLayout.NORTH);
		splitPane.setLeftComponent(treeFiles);
		welcome = new PanelWelcome();
		panelEditor = new PanelEditor();
		panelEditor.agregaAPanelArchivos(welcome);
		
		addSplitTree();
		
		askinfo = new CreateFile(this,(a)->{
			
			//si ambas no cumplen genera errores a la hora de crear paquetes
			//if(a[1] instanceof Integer && (int)a[1] == 1)
			if(a[1] instanceof Integer)
					if((int)a[1] == 1)
						addDocument(a[0].toString(), a[2].toString(),null);
					else;

			else
				addDocument(a[0].toString(), a[1].toString(),a[2].toString());
		});

		for(Component mi : mnArchivo.getMenuComponents())
		{
			mi.setBackground(Color.black);
		}
                
                for(Component mi : menuBar.getComponents())
                    mi.setForeground(Color.white);
		
              //undoList.go();

		about.setVisible(true);
	}
	
	private JMenuItem mntmGuadar;
	private JMenuItem mntmGuardarTodo;
	private JMenuItem mntmAbrir;
	private JMenuItem mntmCerrarTodo;

	public void loadDocument(File path)
	{
		String text = FileManager.loadFile(path.getPath());
		addDocument(path.getName(),path.getPath(),text);

	}

	public void addDocument(String title,String ruta,String text)
	{
		if(tabbedPane.ind==-1) 
                {
			panelEditor.agregaAPanelArchivos(tabbedPane);
                        //timer.start();
                }

		tabbedPane.set(text).addTab(title, new TabPanel(ruta));
	}
	
	public void closeAll()
	{
		while(true) 
		{
			if(0==tabbedPane.getTabCount())
				break;
			if( !((MyPanel) tabbedPane.getTabComponentAt(0)).close()) 
				break;
			
		}
			
	}
	
	public void save(TabPanel sc)
	{
		if(!sc.refTitle.isSaved())
		{			
			sc.refTitle.saveLabel();
			FileManager.saveFile(sc.getName(), sc.text());
		}
	}
	
	public void saveAll()
	{
		for(Component cs : tabbedPane.getComponents())
		{
			if(cs instanceof TabPanel)
				save((TabPanel) cs);
		}
	}
	
	public void addSplitTree()
	{
		contentPane.remove(panelEditor);
		splitPane.setRightComponent(panelEditor);
		contentPane.add(splitPane, BorderLayout.CENTER);
		this.validate();
		this.repaint();
	}
	
	public void hideTree()
	{
		splitPane.remove(panelEditor);
		contentPane.remove(splitPane);
		contentPane.add(panelEditor,"Center");
		this.validate();
	}
	
	public void newTab()
	{
		askinfo.setVisible(true);
	}
}
