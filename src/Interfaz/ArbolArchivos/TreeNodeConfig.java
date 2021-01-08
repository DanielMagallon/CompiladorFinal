package Interfaz.ArbolArchivos;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeNodeConfig extends DefaultMutableTreeNode 
{

	private static final long serialVersionUID = 2285741910788521222L;
	
	public Icon icon;
	public String title;
	private String separator,mainptah;
	private StringBuilder path;
	public TreeNodeConfig padre;
	
	public TreeNodeConfig(Icon icon,String tit,TreeNodeConfig tnc,String mn) 
	{
		this.icon = icon;
		title = tit;
		padre = tnc;
		mainptah = mn;
		path= new StringBuilder();
		separator="/";
		getPath(this);
	}
	
	public TreeNodeConfig(Icon icon,String tit,TreeNodeConfig parent,boolean file) 
	{
		this.icon = icon;
		title = tit;
		padre = parent;
		path= new StringBuilder();
		
		if(file)
			separator = "";
		
		else separator="/";
		getPath(this);
		
	}

	public boolean isPackage() {
		return separator.equals("/");
	}
	
	private void getPath(TreeNodeConfig parent)
	{
		if(parent.padre!=null)
		{
			getPath(parent.padre);

			path.append(parent.title).append(parent.separator);
		}
		else {
			path.insert(0, parent.mainptah+"/"+parent.title+"/");
		}
	}
	
	public String getNodePath()
	{
		return path.toString();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return path.toString();
	}
}
