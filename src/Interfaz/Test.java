package Interfaz;

import Interfaz.TextEditor.TablaSimbolos;
import Perfomance.Compiler;
import Perfomance.Posfix;
import compiler.Analyze;
import compiler.Lexico;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.text.*;

public class Test {
    public static Compiler compiler;

    //Cuando es concatenacion de cadenas usar strcat
    //Cuando es comparacion de cadenas es strcomp

    public static void main(String[] args) throws Exception
    {
//        echo dddd | sudo -S touch edgar.txt
        compiler = new Compiler();
        compiler.onComiler=false;

        //Checar lo de los mnmeros negativos
//        Analyze.perfomance("class id { main ( ) { \nint y=((10+10)*2)/((10-2)*(10-2));\n} }");
//        Analyze.perfomance("class id { main ( ) { \nint uno=11,dos=uno,tres=dos; string name=\"Edgar\", name2=name,name3; name3=\"Juan\"+(name+name2)+\" Reyes\"; name=name2; name2=\"hello\"+name;\n} }");
//        Analyze.perfomance("class id { main ( ) { \n  int x=((1+10*(1/(10+(100-20)*4)))+2)+111,z;  \n} }");
//        Analyze.perfomance("class id { main ( ) { \n  int x=(((1+10)*(1/(10-100)))*2)*(111-10),z;  \n} }");
//        Analyze.perfomance("class id { main ( ) { int y2=10*60; int x=1000; \n do{ \nstring y2=\"10\";\n}\nwhile(not x==1 and (x==x or y2>=y2));" +
//                "int b,c; b=1;}}");
//        Analyze.perfomance("class id { main ( ) { \n do{ \nint x=0;  \n}\nwhile( not (x<=10 or x>=10) and not (x==1 and (x==2 or not x==1)) );}}");
//        Analyze.perfomance("class id { main ( ) {  string x=\"10\"; switch(x){ case x{int y=10+20;} case x{int v;} default{read(x);} } } } ");
//        System.out.println("Syntax Error: "+compiler.syntaxError);
//        System.out.println("Semantic Error: "+compiler.semError);

      //  Analyze.perfomance("class id { main ( ) {  string x=\"10\"; switch(x){ case x{int y=10+20;} case x{int v;} default{read(x);} } } } ");
        //System.out.println("Syntax Error: "+compiler.syntaxError);
        //System.out.println("Semantic Error: "+compiler.semError);
//        Analyze.perfomance("class id { main ( ) { \n  int y,x=10+29-11,p=10;  \n} }");
//        Analyze.perfomance("class exa{ main() { int x=10; switch(x) { case x{ int y; } case x{}} int y;} }");
        //Tratar este error: not not x<=10 -> not (not x<=10) : como redundate cuando hay un numero par seguidos de
        //not -> entonces eliminamos todos los not, si hay un numero impar, dejamos un not
        //cambiar posiion exp

    }

}