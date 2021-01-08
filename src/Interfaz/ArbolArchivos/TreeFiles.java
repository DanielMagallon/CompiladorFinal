package Interfaz.ArbolArchivos;

import static Exe.Snippet.propierties;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import Abstract.ActionEnterEvent;
import Exe.Snippet;
import Interfaz.Compilador;
import Interfaz.MyOptionPane;
import Interfaz.HiddenComps.ComponentHidden;
import Managers.ColorManager;
import Managers.FileManager;
import Managers.IconManager;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class TreeFiles extends JPanel implements TreeSelectionListener, ActionListener
{

	public DefaultTreeModel modelo;
	private JTree tree;
	private TreeNodeConfig raiz; 
	public TreeNodeConfig nodeSelected;
	private ComponentHidden lblMinimzeTree;
	private File workspace; 
	
	public TreeFiles() 
	{
		workspace = new File(propierties.rootActual);
                
                if(!workspace.exists())
                    workspace.mkdir();
                
		raiz = new TreeNodeConfig(IconManager.directoryBlue, 
				workspace.getName(),null, propierties.pathFolder);
		nodeSelected = raiz;
		modelo = new DefaultTreeModel(raiz);
		

		FileManager.displayFiles_Directories(raiz, workspace);
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
//		panel.setBackground(ColorManager.ESCALA_AZUL.getColorScaleOf(2));
		panel.setBackground(Color.black);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		tree = new JTree(modelo);
		tree.addKeyListener(new ActionEnterEvent( a->loadFile() ));
		tree.addTreeSelectionListener(this);
//		tree.setBackground(ColorManager.ESCALA_AZUL.getColorScaleOf(1));
		tree.setBackground(Color.black);
		JScrollPane scrollPane = new JScrollPane(tree);
		panel.add(scrollPane, BorderLayout.CENTER);
		
				tree.setCellRenderer(new MiRender());
				
				JPopupMenu popupMenu = new JPopupMenu();
				addPopup(tree, popupMenu);
				
				JMenuItem mntmNuevoArchivo = new JMenuItem("Nuevo archivo");
				mntmNuevoArchivo.addActionListener((a)->Compilador.getInstance().newTab());
				
				popupMenu.add(mntmNuevoArchivo);
				popupMenu.setBackground(ColorManager.ESCALA_AZUL.getMediumScaleColor());
				
				JMenuItem mntmEliminarArchivo = new JMenuItem("Eliminar archivo");
				mntmEliminarArchivo.setActionCommand("del");
				mntmEliminarArchivo.addActionListener(this);
				popupMenu.add(mntmEliminarArchivo);
				
				JMenu mnNewMenu = new JMenu("Abrir con");
				popupMenu.add(mnNewMenu);
				
				JMenuItem mntmAnalizador = new JMenuItem("Analizador");
				mntmAnalizador.setActionCommand("compile");
				mntmAnalizador.addActionListener(this);
				mnNewMenu.add(mntmAnalizador);
				
				JMenuItem mntmExplorador = new JMenuItem("Explorador");
				mntmExplorador.setActionCommand("exp");
				mntmExplorador.addActionListener(this);
				mnNewMenu.add(mntmExplorador);
				
				JSeparator separator = new JSeparator();
				popupMenu.add(separator);
				
				JMenuItem mntmRefrescar = new JMenuItem("Refrescar");
				mntmRefrescar.addActionListener((a)->refresh());
				popupMenu.add(mntmRefrescar);
				
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new LineBorder(Color.WHITE, 1, true));
				FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
				flowLayout.setAlignment(FlowLayout.RIGHT);
				panel_1.setBackground(ColorManager.ESCALA_AZUL.getColorScaleOf(0));
				panel.add(panel_1, BorderLayout.NORTH);
				
				lblMinimzeTree = new ComponentHidden("Arbol de archivos",Compilador.getInstance().panelHides,
						(a)->{
							Compilador.getInstance().hideTree();
							},(a)->{
								Compilador.getInstance().addSplitTree();
								panel_1.add(lblMinimzeTree);
								this.validate();
							});				
				
				panel_1.add(lblMinimzeTree);
				
				paintMenus(popupMenu.getComponents());
				
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) 
			{
				if(e.getClickCount()==2)
					loadFile();
			}
		});
		
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
	}
	
	public void refresh()
	{
		raiz.removeAllChildren();
		FileManager.displayFiles_Directories(raiz, workspace);
		updateNode(raiz);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		switch(arg0.getActionCommand())
		{
		
			case "compile":
				loadFile();
				break;
				
			case "exp":
				FileManager.openExplorer(nodeSelected.getNodePath());
				break;
				
			case "del":
				if(nodeSelected==raiz) {
					MyOptionPane.showMessage(null, "No puede eliminar la carpeta raiz", 
							"Permiso denegado", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(nodes.length>=2) {
					for(TreePath tp : nodes)
					{
						TreeNodeConfig tnc = (TreeNodeConfig)tp.getLastPathComponent();
						
						if(FileManager.delete(tnc.getNodePath()))
							removeNode(tnc);
					}
				}
				else if(FileManager.delete(nodeSelected.getNodePath()))
						removeNode(nodeSelected);
				break;
		}
	}
	
	private void loadFile()
	{
		if(!nodeSelected.isPackage())
			Compilador.getInstance().addDocument(nodeSelected.title, nodeSelected.getNodePath(),
					FileManager.loadFile(nodeSelected.toString()));
	}
	
	private void paintMenus(Component[] cs)
	{
		for(Component menu : cs)
		{
			menu.setBackground(ColorManager.ESCALA_AZUL.getMediumScaleColor());
			
			if(menu instanceof JMenu)
			{
				menu.setForeground(Color.white);
				paintMenus( ((JMenu)menu).getMenuComponents());
			}
		}
	}
	
	public void addNode(boolean isDirectory,String name)
	{
		TreeNodeConfig node = nodeSelected.isPackage() ?
				nodeSelected : nodeSelected.padre;
		
		node.add(new TreeNodeConfig
				(isDirectory ? IconManager.packBlue : IconManager.fileScript, 
						name, node,!isDirectory));
		
		updateNode(node); 
	}
	
	public void removeNode(TreeNodeConfig node)
	{
		/*if(nodes.length>=2) {
			for(TreePath tp : nodes)
			{
				TreeNodeConfig tnc = (TreeNodeConfig)tp.getLastPathComponent();
				
				tnc.removeFromParent();
				updateNode(tnc);
			}
		}
		else {*/

		node.removeFromParent();
		updateNode(node);
	}
	
	public void updateNode(TreeNodeConfig parent) {
		modelo.reload(parent);
	}
	
	class MiRender extends DefaultTreeCellRenderer
	{
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) 
		{
			TreeNodeConfig node =  (TreeNodeConfig) value;
			
			setIcon(node.icon);
			setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
			setForeground(Color.white);
			setText(node.title);
			setOpaque(true);
			
			if(selected)
				setBackground(Color.LIGHT_GRAY);
			else 
				setBackground(null);
			
			return this;
		}
	}
	
	
	private TreePath[] nodes;
	

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		//nodes = (TreePath) tree.getSelectionPaths();
		nodes = tree.getSelectionPaths();
		
		nodeSelected = (TreeNodeConfig)
                tree.getLastSelectedPathComponent();
				
		
		if(nodeSelected==null)
			nodeSelected = raiz;
	
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
