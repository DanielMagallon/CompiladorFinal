package compiler;

import java.util.ArrayList;
import java.util.LinkedList;



public class Analyze
{
    private static String text2Analyze;
    private static ArrayList<Integer> lines = new ArrayList<>();

    public static class Cola extends LinkedList<Object>
    {

        private String[] specials = {"=",">","<"};

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            
            for(Object obj : this)
            {
                if(obj instanceof Integer)
                {
                   
                    sb.append("\nLinea ").append(obj).append(": ");
                }else if (obj instanceof String){
                    sb.append("[").append(obj).append("], ");
                            
                }else{
                    sb.append("Index begin: ").append(obj);
                }
            }
            
            return sb.toString();
        }
        
        public void check()
        {
            if(this.isEmpty()){
                System.out.println("Lista vacia. No hay nada que analizar");
                return;
            }
            if(this.get(this.size()-1) instanceof Integer)
            {
                System.out.println("Se removio objeto entero");
                this.pollLast();
            }
        }
        
        
        @Override
        public boolean add(Object e) 
        {
            
            if(e instanceof String)
            {
                e = e.toString().replaceAll("\n", "");
                
                 if(e.toString().length()==0)
                    return false;
            }
            
            if(this.size()>1 && e instanceof String)
            {
                for(String esp : specials)
                {
                    if(this.get(this.size()-1).equals(esp))
                    {
                        for(String esp2 : specials)
                        {
                            if(e.equals(esp2))
                            {
                                this.set(this.size()-1, esp+e);
                                return false;
                            }
                        }
                    }
                }
            }
            else if(this.size()>0 && e instanceof Integer)
            {
                 if(this.get(this.size()-1) instanceof Integer)
                 {
                    this.set(this.size()-1, e);
                    return false;
                 }  
            }else if(this.size()>0 && e instanceof Long)
            {
                 if(this.get(this.size()-1) instanceof Long)
                 {
                    this.set(this.size()-1, e);
                    return false;
                 }  
            }
            return super.add(e);
        }
        
    }
    
    protected static Cola lexemas = new Cola();

    private static Lexico lexInstance;

    public static void setLexico(Lexico instance)
    {
        lexInstance=instance;
        DataDefinition.load();

    }
    
    public static void perfomance(String text)
    {
        lines.clear();
        text2Analyze = text;
        lexemas.clear();
        splitByWords_Symbol();
        lexInstance.lexAnalyze();
    }
    
    
    private static int i;
    
    private static void splitByWords_Symbol()
    {
       StringBuilder lex = new StringBuilder();
       char c;
       boolean space=false,stringDec=false,charDec=false;
       int count=1;
       
       
       
        for(i=0; i< text2Analyze.length(); i++)
        {
            c= text2Analyze.charAt(i);
            
            if(i==0){
                lexemas.add(count++);
            }
                       
            if(c=='"' && !charDec){
                
                if(!stringDec)
                {
                    
                    lexemas.add(lex.toString());
                    lex.setLength(0);
                    lex.append(c);
                }else if(stringDec && lex.charAt(0)==c){
                    lexemas.add((long)i-lex.length()+1);  
                    lexemas.add(lex.toString()+c);
                    lex.setLength(0);
                }
                
                stringDec=!stringDec;
                
                
               
            }else if(c=='\'' && !stringDec){
            
                 if(!charDec)
                 {
                    lexemas.add(lex.toString());
                    lex.setLength(0);
                    lex.append(c);
                }else if(charDec && lex.charAt(0)==c){
                    lexemas.add((long)i-lex.length()+1);  
                    lexemas.add(lex.toString()+c);
                    lex.setLength(0);
                }
                charDec=!charDec;
                
               
                    
            }else if(stringDec || charDec){

                if(c=='\n')
                {
                    stringDec=charDec=false;
                    lexemas.add((long)i-lex.length());  
                    lexemas.add(lex.toString());
                        lex.setLength(0);
                    lexemas.add(count++);
                }
                else{ 
                    lex.append(c);
                
                    if(i== text2Analyze.length()-1){
                        lexemas.add((long)i-lex.length());  
                        lexemas.add(lex.toString());
                            lex.setLength(0);
                    }
                }
                
                
            }else if(c == '\n'){
                
                lexemas.add((long)i-lex.length());                
               lexemas.add(lex.toString());
               lex.setLength(0);
                    
               lexemas.add(count++);
                 
               space=true;
                 
            }
            else if( !(c ==  ' ' ||  c ==  '\t'))
            {
                space = false;
                
                if(DataDefinition.isSymbol(c+"",false))
                {   
                    lexemas.add((long)i-lex.length());
                    lexemas.add(lex.toString());
                    
                    lex.setLength(0);
                    
                    boolean doble=false;
                    
                    if( i!= text2Analyze.length()-1 && (c=='<' || c=='>' || c=='='))
                        if(text2Analyze.charAt(i+1)=='=')
                            doble=true;
                        
                    lexemas.add((long)i);
                    lexemas.add(doble ? c+"=" : c+"");
                    
                    if(doble)
                        i++;
                }
                else {
                    
                     if(i== text2Analyze.length()-1)
                     {
                        lexemas.add((long)i-lex.length());
                        lexemas.add(lex.toString()+c);
                        lex.setLength(0);
                        
                     }
                     else lex.append(c);
                }
                   
            }
            else{
                 if(!space){
                    lexemas.add((long)i-lex.length());
                    lexemas.add(lex.toString());
                    lex.setLength(0);
                 }
                 
                 space=true;
            }
        }
        
        lexemas.check();
        
        //Lexemas por linea mostrar
        //System.out.println(lexemas);
    }
     
}