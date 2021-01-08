/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;


import java.util.regex.Pattern;
import static  compiler.Automata.automata_nums;


/**
 *
 * @author daniel
 */
public class Lexico 
{
    public static final String comillas = "\"",comilla_simple="'",cadena_correcta="OK";

    
    public static String isString(String lexema)
    {
        if(lexema.startsWith(comillas))
        {
            if(!lexema.endsWith(comillas) || lexema.length()==1)
                return "Se esperaba terminador de cadena: `\"`";
            
            return cadena_correcta;
        }
        return null;
        //return Pattern.compile("^\".*\"$").matcher(lexema).find();
    }
     
    public static String isChar(String lexema)
    {
        
        if(lexema.startsWith(comilla_simple))
        {
            if(lexema.endsWith(comilla_simple))
            {
                if(lexema.length()!=3 || lexema.charAt(1)=='\t')
                    return "Tamaño para tipo caracter invalido\nSolo se puede definir un caracter. "
                            + "No se permite tabulaciones";
                
                return cadena_correcta;
                
            }else return "Se esperaba termindor de comilla simple: `'`";
        }
        return null;
    }
    
    public static String isNum(String cad)
    {
        int index=-1;
        String error="";
        HashState est = automata_nums.get(0);
        String c;
        Integer state=-1;
        String result="";
        
        for(int i=0; i<cad.length(); i++)
        {
            c = cad.charAt(i)+"";
            
            state = est.found(c);
            
            if(state==null)
            {
                index = i;
                error=c;
                result = est.getValorNoEncontrado();
                break;
            }
            else
            {
                for(HashState e : automata_nums)
                {
                    if(e.getEstado() == state)
                    {
                        est = e;
                        break;
                    }  
                }
            }
        }
        
        if(result.equalsIgnoreCase("null"))
            return null;
        
        if(state==null)
        {
            return String.format("Error en el caracter: %s Index: %d\n%s", error,index,result);
            
        }else{
            for(String ter : Automata.terminalesNume)
                if(ter.equalsIgnoreCase(est.getValorTerminal()))
                    return cadena_correcta;
            
            index = cad.length()-1;
            
            return String.format("Error en el caracter: %s Index: %d\n%s", error,index,est.getValorTerminal());
        }
    }
  
    public static boolean isIdentifier(String lexema)
    {
        return Pattern.compile("^([a-zA-Z_])(_*[0-9]*[aA-zZ]*)*$").matcher(lexema).find();
    }


    private Runnable previousTask,currentTask,afterTask;

    public Lexico(Runnable previousTask, Runnable currentTask, Runnable afterTask) {
        this.previousTask = previousTask;
        this.currentTask = currentTask;
        this.afterTask = afterTask;
        Automata.load();
    }

    private int lineaAct=-1,currentSize;

    public void setLineaAct(int lineaAct) {
        this.lineaAct = lineaAct;
    }

    public void setIndexOf(long indexOf) {
        this.indexOf = indexOf;
    }

    public int getCurrentLine(){
        return lineaAct;
    }

    private long indexOf=0;
    public long getCurrentIndexChar(){
        return indexOf;
    }

    private String currentToken;
    public String getCurrentToken() {
        return currentToken;
    }

    public boolean followAnalyzis;

    public boolean lexAnalyze()
    {
        previousTask.run();

        if(!Analyze.lexemas.isEmpty())
        {
            for(Object token : Analyze.lexemas)
            {
                if(token instanceof Integer)
                    lineaAct = (Integer) token;

                else if(token instanceof Long)
                    indexOf = (long) token;

                else{
                    currentToken = token.toString();
                    currentSize = token.toString().length();
                    currentTask.run();
                }

                if(!followAnalyzis)
                    break;
            }

            indexOf += currentSize;
            afterTask.run();

            return true;

        }else{
            //No tokens for analyze
            return false;
        }
    }
}
