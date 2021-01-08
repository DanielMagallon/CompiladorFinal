/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz.TextEditor;

import Interfaz.MyOptionPane;
import Managers.SugerenceAbstract;
import Managers.SugerenceManager;
import Managers.SugerenceTextField;
import compiler.DataDefinition;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextPane;

/**
 *
 * @author daniel
 */
public class ListaSugerencias extends JList<SugerenceManager>
{

    private TabPanel r;
    private Dimension dim = new Dimension();
    private DefaultListModel<SugerenceManager> modelo = new DefaultListModel();
    private DefaultListModel<SugerenceManager> modeloPR = new DefaultListModel();
    private static String spaces="    ";
    
    public  StringBuilder tabCount = new StringBuilder();
    
    public  void resetTabCount()
    {
        
        tabCount.setLength(0);
    }
    
    public  void incrementTab()
    {
        
        tabCount.append("\t");
    }
    
    public ListaSugerencias(TabPanel ref) 
    {
        //setPreferredSize(new Dimension(0,100));
        
        r = ref;
        
        setModel(modelo);

        this.addKeyListener(new KeyAdapter() 
        {
            @Override
            public void keyReleased(KeyEvent ke) 
            {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER){
                    action();
                }
            }
               
        });
        this.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent me) 
            {
                if(me.getClickCount()==2)
                    action();
            }
             
        });
        
        
       /* for(String[] objs : DataDefinition)
        {
            SugerenceManager su =new SugerenceManager(objs[0], objs[0]);
            modeloPR.addElement(su);
            modelo.addElement(su);
        }*/

        Object alias[][] =
        {
            {"main","main()\n%s{\n%s}",15,false},
            {"clname","class myclass\n%s{\n%s}",20,false},
            {"dow","do{\n\n} while();",14,false},
            {"swt","switch(val)\n{\n\tcase id\n\t{\n\t}\n}",30,false},
            {"prf","printf(\"{0}\",id);",10,false},
            {"pln","println(\"Type here\\n\");",25,false},
            {"read","read(id);",10,false}

        };
        
        
        for(Object[] aliases : alias)
        {
            SugerenceManager su = new SugerenceManager(aliases[0].toString(), 
                                    aliases[1].toString(),(int)aliases[2],(boolean)aliases[3]);
            modeloPR.addElement(su);
            modelo.addElement(su);
        }
        
        
        
    }

    
    
    
    public void filterByBegin(String start)
    {
        modelo.clear();
        
        
        if(start.trim().isEmpty())
            defaultFilter();

        else
            for(int i=0; i<modeloPR.getSize(); i++)
            {
                if(modeloPR.getElementAt(i).toString().startsWith(start))
                {
                    modelo.addElement(modeloPR.getElementAt(i));
                }
            }
        
        resizeList();
    }
    
    public void defaultFilter(){
        for(int i=0; i<modeloPR.getSize(); i++)
        {
           modelo.addElement(modeloPR.getElementAt(i));
        }
    }
    
    private void action()
    {
        try {
            r.makeAction(ListaSugerencias.this.getSelectedValue().getTextAction(),
                    ListaSugerencias.this.getSelectedValue().getCaret(),
                    this.getSelectedValue().isThree());
            this.defaultFilter();
        }catch (Exception e){}
    }
    
    private int _normal_height=20;
    
    @Override
    public void requestFocus() {
        super.requestFocus(); //To change body of generated methods, choose Tools | Templates.
        resizeList();
    }
    
    private void resizeList(){
        
        this.setSelectedIndex(0);
        
        dim.height = modelo.getSize()*_normal_height;
        
        setPreferredSize(dim);
    }
    
}
