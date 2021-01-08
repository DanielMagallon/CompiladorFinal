/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz.TextEditor;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author daniel
 */
public class TablaSimbolos extends javax.swing.JDialog {

    /**
     * Creates new form TablaSimbolos
     */
    
    private DefaultTableModel model;
    
    public TablaSimbolos(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        
        
        
        model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        
            
        };
        
        model.addColumn("Linea");
        model.addColumn("Posicion Caracter");
        model.addColumn("Nombre");
        model.addColumn("Identificador/Componente");

        initComponents();
        setSize(700, 400);
    }

    public void removeComp(){
        this.model.setRowCount(0);
    }
    
    public void addIdentificador(int linea, long index,String nombre,String comp)
    {
        model.addRow(new Object[]{linea,index,nombre,comp});
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(model);
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
