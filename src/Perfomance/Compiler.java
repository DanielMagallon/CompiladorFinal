package Perfomance;

import Interfaz.Compilador;
import compiler.Analyze;
import compiler.DataDefinition;
import compiler.LR_Syntax;
import compiler.Lexico;

public class Compiler
{
    public Lexico lexico;
    public LR_Syntax sintax_lr;
    private static final String cadena_correcta = "OK";
    private String value;
    private boolean followSyntax;
    public StringBuilder lexError,syntaxError;
    public boolean onComiler=true;
    public Semantic semantic;
    public String semError;


    public Compiler()
    {
        semantic = new Semantic(()->
        {
            semError=String.format("Error semantico: %s (linea: %d | index: %d)",
                    semantic.semanticMsg,lexico.getCurrentLine(),lexico.getCurrentIndexChar());
        },()->
        {
            semError = semantic.semanticMsg.toString();

        }

        );
        lexError = new StringBuilder();
        syntaxError = new StringBuilder();
        lexico = new Lexico(this::previousTask,this::currentTask,this::endTask);
        sintax_lr = new LR_Syntax(this::onSyntaxProduction,this::onSyntaxDisplace,this::onSyntaxAccepted);
//        sintax_lr.tableLRGrammar.show();
        Analyze.setLexico(lexico);
    }

    private void onSyntaxProduction(){

//        System.out.println(sintax_lr.pilaSintactico);

    }

    private void onSyntaxDisplace(){

//        System.out.println(sintax_lr.pilaSintactico);
    }

    private void onSyntaxAccepted(){
        if(onComiler)
        Compilador.getInstance().panelEditor.textAreaSintactico.setText
                ("Analisis sintactico finalizado correctamente\n\n"+semError);
    }



    private void previousTask()
    {
        if(onComiler){
            Compilador.getInstance().tablaSimbolos.removeComp();
            Compilador.getInstance().panelEditor.textAreaSintactico.setText("");
            Compilador.getInstance().panelEditor.textAreaLexico.setText("");
        }

        semError="";
        sintax_lr.reset();
        semantic.reset();
        syntaxError.setLength(0);
        lexError.setLength(0);
        lexico.setIndexOf(-1);
        followSyntax=false;
        lexico.followAnalyzis=true;
        lexico.setLineaAct(0);


    }


    private void currentTask(){
        if(DataDefinition.isSpecialWord(lexico.getCurrentToken()))
        {
            if(onComiler)
            Compilador.getInstance().tablaSimbolos.addIdentificador(lexico.getCurrentLine(),
            lexico.getCurrentIndexChar(), lexico.getCurrentToken(),lexico.getCurrentToken());

            semantic.currentToken = Semantic.Token.SPECIAL_WORD;

            if(!(lexico.followAnalyzis = sintax_lr.analyze(lexico.getCurrentToken())))
            {
                syntaxError.append(String.format("Error sintatico en linea: %d en el token: `%s`\n\n",
                        lexico.getCurrentLine(),lexico.getCurrentToken()));
            }else{
              lexico.followAnalyzis = semantic.semanticAnalyze(lexico.getCurrentToken());
            }

        }
        else if(DataDefinition.isSymbol(lexico.getCurrentToken(), true))
        {
            if(onComiler)
            Compilador.getInstance().tablaSimbolos.addIdentificador(lexico.getCurrentLine(),
                    lexico.getCurrentIndexChar(),lexico.getCurrentToken(), lexico.getCurrentToken());

            semantic.currentToken = Semantic.Token.SYMBOL;
            if(!(lexico.followAnalyzis = sintax_lr.analyze(lexico.getCurrentToken())))
            {
                syntaxError.append(String.format("Error sintatico en linea: %d en el token: `%s`\n\n",
                        lexico.getCurrentLine(),lexico.getCurrentToken()));

            }else{
                lexico.followAnalyzis= semantic.semanticAnalyze(lexico.getCurrentToken());
            }
        }
        else if((value=Lexico.isString(lexico.getCurrentToken()))!=null)
        {
            if(value.equalsIgnoreCase(cadena_correcta))
            {
                if(onComiler)
                Compilador.getInstance().tablaSimbolos.addIdentificador(lexico.getCurrentLine(),
                        lexico.getCurrentIndexChar(), lexico.getCurrentToken(), "litcad");

//                dataTable.addComponent("litcad");
            }else{
                lexError.append(String.format("Error en linea: %d en el token: `%s` => %s\n\n",
                        lexico.getCurrentLine(),lexico.getCurrentToken(),value));

                if(onComiler)
                Compilador.getInstance().tabbedPane.getActual().tabRef.markType
                        (lexico.getCurrentIndexChar(), lexico.getCurrentToken().length(),-1);
            }

            semantic.currentToken = Semantic.Token.LITCAD;
            if(!(lexico.followAnalyzis = sintax_lr.analyze("litcad")))
            {
                syntaxError.append(String.format("Error sintatico en linea: %d en el token: `%s`\n\n",
                        lexico.getCurrentLine(),lexico.getCurrentToken()));
            }else{
                lexico.followAnalyzis= semantic.semanticAnalyze(lexico.getCurrentToken());
            }

        }
        else if((value = Lexico.isChar(lexico.getCurrentToken()))!=null)
        {
            if(value.equalsIgnoreCase(cadena_correcta))
            {
                if(onComiler)
                Compilador.getInstance().tablaSimbolos.addIdentificador(lexico.getCurrentLine(),
                        lexico.getCurrentIndexChar(), lexico.getCurrentToken(), "char");
            }else{

                lexError.append(String.format("Error en linea: %d en el token: `%s` => %s\n\n",
                        lexico.getCurrentLine(),lexico.getCurrentToken(),value));

                if(onComiler)
                Compilador.getInstance().tabbedPane.getActual().tabRef.markType
                        (lexico.getCurrentIndexChar(),lexico.getCurrentToken().length(),-1);
            }

            semantic.currentToken = Semantic.Token.LITCHAR;
            if(!(lexico.followAnalyzis = sintax_lr.analyze("litchar")))
            {
                syntaxError.append(String.format("Error sintatico en linea: %d en el token: `%s`\n\n",
                        lexico.getCurrentLine(),lexico.getCurrentToken()));
            }else{
                lexico.followAnalyzis= semantic.semanticAnalyze(lexico.getCurrentToken());
            }
        }
        else if((value=Lexico.isNum(lexico.getCurrentToken()))!=null)
        {
            if(value.equalsIgnoreCase(cadena_correcta))
            {
                if(onComiler)
                Compilador.getInstance().tablaSimbolos.addIdentificador(lexico.getCurrentLine(),
                        lexico.getCurrentIndexChar(), lexico.getCurrentToken(), "num");
//                dataTable.addComponent("num");
            }else{
                lexError.append(String.format("Error en linea: %d en el token: `%s` => %s\n\n",
                        lexico.getCurrentLine(),lexico.getCurrentToken(),value));

                if(onComiler)
                Compilador.getInstance().tabbedPane.getActual().tabRef.markType
                        (lexico.getCurrentIndexChar(), lexico.getCurrentToken().length(),-1);
            }

            semantic.currentToken = Semantic.Token.NUM;
            if(!(lexico.followAnalyzis = sintax_lr.analyze("num")))
            {
                syntaxError.append(String.format("Error sintatico en linea: %d en el token: `%s`\n\n",
                        lexico.getCurrentLine(),lexico.getCurrentToken()));
            }else{
                lexico.followAnalyzis= semantic.semanticAnalyze(lexico.getCurrentToken());
            }
        }
        else if(Lexico.isIdentifier(lexico.getCurrentToken()))
        {
            if(onComiler)
            Compilador.getInstance().tablaSimbolos.addIdentificador(lexico.getCurrentLine(),
                    lexico.getCurrentIndexChar(), lexico.getCurrentToken(), "id");

            semantic.currentToken = Semantic.Token.IDENTIFIER;
            if(!(lexico.followAnalyzis = sintax_lr.analyze("id")))
            {
                syntaxError.append(String.format("Error sintatico en linea: %d en el token: `%s`\n\n",
                        lexico.getCurrentLine(),lexico.getCurrentToken()));
            }else{
                lexico.followAnalyzis= semantic.semanticAnalyze(lexico.getCurrentToken());
            }

        }
        else{
            lexError.append(String.format("Error en linea: %d en el token: `%s`\n\n",
                    lexico.getCurrentLine(),lexico.getCurrentToken()));

            if(onComiler)
            Compilador.getInstance().tabbedPane.getActual().tabRef.markType
                    (lexico.getCurrentIndexChar(), lexico.getCurrentToken().length(),-1);
            //break;
        }
    }


    private void endTask(){

        if(onComiler)
        Compilador.getInstance().panelEditor.textAreaLexico.setText((lexError.length()!=0) ? lexError.toString() :
                "Analisis lexico finalizado correctamente");

        if(lexico.followAnalyzis)
        {
            sintax_lr.analyze("$");

            if(!sintax_lr.acepeted) {
                if (onComiler)
                    Compilador.getInstance().panelEditor.textAreaSintactico.setText(String.format
                            ("Cadena no aceptada (%s-->linea:%d)", lexico.getCurrentToken(), lexico.getCurrentLine()));
            }else{
//                System.out.println(semantic.codeCreator.getCode());
            }

        }else{
            if(!sintax_lr.acepeted) {
                if (onComiler)
                    Compilador.getInstance().panelEditor.textAreaSintactico.setText(syntaxError.toString()+"\n\n"+semError);
            }else {
                if(onComiler)
                    Compilador.getInstance().panelEditor.textAreaSintactico.setText(semError);
            }
        }
    }
}
