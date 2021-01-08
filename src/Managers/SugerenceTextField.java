/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import javax.swing.JTextField;

/**
 *
 * @author daniel
 */
public class SugerenceTextField extends JTextField implements SugerenceAbstract
{
    
    public SugerenceTextField(KeyAdapter key) 
    {
        setPreferredSize(new Dimension(400, 25));
        addKeyListener(key);
    }

    
    
    @Override
    public String toString() {
        return "";
    }

    @Override
    public String getTextAction() {
        return "";
    }
 
    
}
