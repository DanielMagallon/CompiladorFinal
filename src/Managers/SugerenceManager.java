/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;



/**
 *
 * @author daniel
 */
public class SugerenceManager
{
    private String text,actionText;

    private int caret=-1;
    
    private boolean tres;
    
    public SugerenceManager(String text,String actionText, int pos,boolean tres)
    {
        this(text, actionText);
        this.caret=pos;
        this.tres=tres;
    }
    
    public SugerenceManager(String text,String actionText)
    {
        this.text = text;
        this.actionText = actionText;
    }
    
    public boolean isThree(){
        return tres;
    }
    
    public int getCaret(){
        return caret;
    }
    
    public String getTextAction(){
        return this.actionText;
    }

    @Override
    public String toString() {
        return text;
    }
    
    
    
}
