package Perfomance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class CodeCreator {

    public String fileName;

    class Block {

        public final String blockName;
        public StringBuilder code;
        public ArrayList<Block> codeBlocks;
        public Block parentBlock;
        public boolean hasFinished;
        public final int nroTabulaciones,ID_BLOCK;
        public final String blockIdentifier;
        public StringBuilder tabulaciones;

        protected Block(Block parent,String name, int nro, int id,String blockIdentifier){


            this.blockIdentifier = blockIdentifier;
            this.ID_BLOCK = id;
            this.nroTabulaciones =nro;
            blockName = name;
            parentBlock = parent;
            code = new StringBuilder();
            codeBlocks = new ArrayList<>();
            tabulaciones  = new StringBuilder();
            check();
        }

        private void check(){

            tabulaciones.append("\t".repeat(Math.max(0, nroTabulaciones)));

            if(blockName.equals("main"))
            {
                code.append("int main()\n{\n");
            }else if(blockIdentifier.equals("Do")){
                code.append(blockName).append(": {\n");
            }

        }

        public void addCode(String code)
        {
            Block bl = new Block(this,"normal",this.nroTabulaciones +1,ID_BLOCK,"Normal");
            bl.code.append(bl.tabulaciones).append(code);
            this.codeBlocks.add(bl);
        }

        public void addBlock(Block block){
            codeBlocks.add(block);
        }

        public Block finishBlock()
        {

            if(blockIdentifier.equals("Main"))
            {
                if(!hasFinished){
                    getCodeBlocks();
                    code.append("\nprintf(\"Presione enter para salir: \");").append
                            ("\nscanf(\"%c\",&temp);").append("\nscanf(\"%c\",&temp);").append("\n}");
                    hasFinished=true;
                }
            }
            else{
                getCodeBlocks();

                if (blockIdentifier.equals("Do"))
                        code.append("\n}\n");

                else if (blockIdentifier.equals("Case"))
                    code.append("\ngoto ").append(semRef.switchDataStack.peek().blockEndName).append(";\n").append(blockName).append(": ;");

            }


                return this.parentBlock==null ? this : this.parentBlock;
        }

        private void getCodeBlocks()
        {
            for(Block block : codeBlocks)
            {
                code.append(block.code);
            }
        }
    }

    private Block block,lastBlock;
    private StringBuilder codeLine,codeMacros,codeTempLine;
    private int sizeCreatedConstants;
    private Semantic semRef;

    protected CodeCreator(Semantic ref)
    {
        semRef  = ref;
        codeLine = new StringBuilder();
        codeMacros = new StringBuilder();
        codeTempLine = new StringBuilder();
        block = null;
    }

    public String getCurrentIdBlockName(){
        return block.blockIdentifier;
    }

    public String getCurrentBlockName(){
        return block.blockName;
    }

    public static String getVar4Constants(String type)
    {
        switch (type) {
            case "string":
                return "$sVar";

            case "int":
                return "$iVar";

            case "float":
                return "$fVar";

            case "char":
                return "$cVar";

            default:
                return null;
        }
    }

    public String addConstant(String value)
    {
        sizeCreatedConstants++;
        String var="$var"+sizeCreatedConstants;
        codeMacros.append("#define ").append(var).append(" ").append(value).append("\n");

        return var;
    }

    public String getCode(){

        return "#include <stdio.h>\n"+"#include <stdlib.h>\n"+"#include <string.h>\n\nchar temp;\n"+
                codeMacros + block.code;
    }

    public void finishBlock(){
            lastBlock = block;
            block = block.finishBlock();
    }

    private int currentIdBlock;

    public int getCurrentIdBlock(){
        return block.ID_BLOCK;
    }

    public void addBlock(String blockname,String idName)
    {
        if(block==null)
        {
            block = new Block(null,blockname,0,currentIdBlock++,idName);
            lastBlock = null;
        }
        else{
            Block aux = block;
            block = new Block(aux,blockname,aux.nroTabulaciones +1,currentIdBlock++,idName);
            aux.addBlock(block);
        }
        codeLine.setLength(0);
    }

    public Block getLastBlock(){
        return lastBlock;
    }

    public void addSwitch()
    {
        Block aux = block;
        block = new Block(aux,"switch",aux.nroTabulaciones +1,aux.ID_BLOCK,"Switch");
        aux.addBlock(block);
    }

    public void addCase()
    {
        Block aux = block;
        block = new Block(aux,getNextCase(),aux.nroTabulaciones +1,aux.ID_BLOCK,"Case");
        aux.addBlock(block);
    }

    public void addDefault(){
        Block aux = block;
        block = new Block(aux,"default",aux.nroTabulaciones +1,aux.ID_BLOCK,"Default");
        aux.addBlock(block);
    }

    public static String getEquivalent(String comp)
    {
        switch (comp)
        {
            case "string":
                return "char ";

            case "int":
            case "float":
            case "char":
            case "const":
                return comp.concat(" ");

            case "read":
                return "scanf";

            default:
                return comp;
        }
    }

    private int doCant=-1;
    public String getNextDo(){
        doCant++;
        return doCant==0 ? "Do" : "Do"+doCant;
    }

    private int caseCant=-1;
    private String currentCase,lastCase;
    public String getNextCase(){
        caseCant++;

        if(caseCant==0){
            currentCase = "Case";
            lastCase = null;
        }
        else
        {
            lastCase = currentCase;
            currentCase = "Case"+caseCant;
        }

        return currentCase;
    }

    private int endSwitchCant=-1;
    public String getNextEndSwitch(){
        endSwitchCant++;
        return endSwitchCant==0 ? "endSwitch" :"endSwitch"+endSwitchCant;

    }

    public String getCurrentCase(){
        return currentCase;
    }

    public void addCode()
    {
        block.addCode(codeLine.toString());
        codeLine.setLength(0);
    }

//    public String getCurrentTabulation(){
//        return block.tabulaciones.toString()+"\t";
//    }

    public CodeCreator appendCode(String comp){
//        if(DataDefinition.isSpecialWord(comp))
        String eq = getEquivalent(comp);
        codeLine.append(eq);
        return this;
    }

    public CodeCreator appendTemporalCode(String comp)
    {
        codeTempLine.append(comp);
        return this;
    }

    public void toCString(boolean logicExp)
    {
        if(!logicExp) {
            if (codeTempLine.length() > 0)
                calculateVariables();
        }else{
            checkLogicVariables();
        }
    }

    private static final String[] symbols = {"==","<=",">=","<",">"};

    private void checkLogicVariables(){
        String[] lines = codeTempLine.toString().split("\n");
        String[] tks;
        codeTempLine.setLength(0);
        for(String line : lines)
        {
            tks = line.split(" ");


            //Si el mañao de la linea es 3  o 5, se vreficiara las expresiones
            //para en caso de haber variables string, cambie el codigo
            if(tks.length==5 || tks.length==3) {

                if(tks.length==3){
                    //si es se cumple es porque se estaba haciendo una negeacion: iVar = !iVar
                    if(tks[2].startsWith("!")) {
                        codeTempLine.append(line).append("\n");
                        continue;
                    }

                    String res = convertSingleExpression(tks[2]);
                    codeTempLine.append(tks[0]).append(" = ").append(res).append(";\n");
                }
                else{
                    //tks[0] -> id
                    //tks[1] -> =
//                    tks[2] -> var<var
                    //tks[3] -> && / ||
//                    tks[4] -> var < var
                    String res = convertSingleExpression(tks[2]).replace(";","");
                    String res1 = convertSingleExpression(tks[4]).replace(";","");
                    codeTempLine.append(tks[0]).append(" = ").append(res).append(" ").append(tks[3]).append(" ").append(res1).append(";\n");
                }
            }else{
                codeTempLine.append(line).append("\n");
            }
        }
    }

    //start cmd.exe /C d.out start cmd.exe /C d.out

    private boolean readTemp;
    public String convertToScanf(String id, String type){
        if(type.equals("string"))
        {
            if(readTemp){
                String scanfString = "\" %[^\\n]%*c\"";
                readTemp=false;
                return String.format("%s = (char *) malloc(sizeof(char));\nscanf(%s,%s);",id, scanfString,id);
            }
            String scanfString = "\" %[^\\n]%*c\"";
            return String.format("%s = (char *) malloc(sizeof(char));\nscanf(%s,%s);",id, scanfString,id);
        }
        else{
            readTemp = true;
            if(type.equals("char")) {
                return String.format("scanf(\" %s\",&%s);",getModouleEquiv(type),id);
            }
            return String.format("scanf(\"%s\",&%s);",getModouleEquiv(type),id);
        }
    }

    public String convertToPrintf(String litcad,String[][] args)
    {
        if(args==null || args.length==0){
            return String.format("printf(%s);",litcad);
        }else{
            return "printf(".concat(reduceString(new StringBuilder(litcad),args).toString()).concat(");");
        }
    }

    public StringBuilder reduceString(StringBuilder cad,String[][] argsAndTypes){

        for(String[] typeArg : argsAndTypes)
        {
            String mod = getModouleEquiv(typeArg[1]);

            if (mod==null) throw new IllegalArgumentException("The type is not correct.");

            typeArg[1] = mod;
        }

        String num="";
        int openIndex=-1,closeIndex,i=0;
        StringBuilder  parameters = new StringBuilder();

        while (i != cad.length()) {

            if (cad.charAt(i) == '{') {
                openIndex = i++;
            } else if (cad.charAt(i) == '}') {

                if (openIndex == -1) {
                    i++;
                    continue;
                }

                closeIndex = i;

                if (openIndex + 1 < closeIndex) {
                    AtomicInteger numInt = new AtomicInteger();
                    if (isPositiveNum(num, numInt) && numInt.get() < argsAndTypes.length) {
                        int aux = openIndex;

                        while (aux <= closeIndex) {
                            cad.deleteCharAt(openIndex);
                            aux++;
                            i--;
                        }

                        i += 3;
                        cad.insert(openIndex, argsAndTypes[numInt.get()][1]);
                        parameters.append(argsAndTypes[numInt.get()][0]).append(",");
                    }

                } else i++;
                num = "";
                openIndex = -1;

            } else {
                if (openIndex != -1) {
                    num = num.concat(cad.charAt(i) + "");
                }

                i++;
            }
        }

        if(parameters.length()>0)
            parameters.deleteCharAt(parameters.length()-1); //Borra la ,

        return parameters.length()==0 ? cad :cad.append(", ").append(parameters);
    }


    private boolean isPositiveNum(String cad, AtomicInteger num){

        cad=cad.replace(" ","");
        try{
            num.set(Integer.parseInt(cad));

            return num.get() >= 0;

        }catch(NumberFormatException ex){
            return false;
        }

    }

    private String getModouleEquiv(String type)
    {
        return type.equals("string") ? "%s" : type.equals("int") ? "%d" : type.equals("char") ?
                "%c" : type.equals("float") ? "%f" : null;
    }

    public String convertPrintlnToPf(String cad,String type){

        String typMod = type.equals("string") ? "%s" : type.equals("int") ? "%d" :
                type.equals("char") ? "%c" : type.equals("float") ? "%f" : null;

        if(typMod==null)  throw new IllegalArgumentException("The type given is not correct.");


        return String.format("printf(\"%s\\n\",%s);",typMod,cad);

    }

    private String convertSingleExpression(String exp)
    {
        String firstVar, secondVar,symbol=null;

        for(String sym : symbols)
        {
            if(exp.contains(sym))
            {
                symbol = sym;
                break;
            }
//            throw new IllegalArgumentException("CodeCreator.checkLogicVariables: The expression doesnt contain an logic symbol");
        }

        if(symbol==null){
            //Se puede dar el caso que la expresion sea iVar || x<=10;
            return exp;
//            throw new NullPointerException("CodeCreator.checkLogicVariables: The var symbol is null.");
        }

        String[] subs = exp.split(symbol);

        firstVar = subs[0];
        secondVar = subs[1].replace(";","");


        Semantic.Types type1,type2;

        type1  = semRef.getTypeOf(firstVar);

        type2  = semRef.getTypeOf(secondVar);

        //Se supon que si un tipo string, el otro lo debe ser, en caso de que no, entonces algo esta mal
        //al validar las expresiones logicas anterioremente
        if ((type1== Semantic.Types.STRING && type2!= Semantic.Types.STRING)
            || (type1 != Semantic.Types.STRING && type2== Semantic.Types.STRING)){
            throw new IllegalArgumentException("CodeCreator.checkLogicVariables: The types are different");
        }else{
            if(type1 == Semantic.Types.STRING)
            {
                return getCLogicEquivalent(symbol,firstVar,secondVar);
            }else{
               return exp;
            }
        }
    }

    private String getCLogicEquivalent(String symbol,String var1, String var2){
        return String.format("strcmp(%s,%s) %s 0",var1,var2,symbol);
    }

    private void calculateVariables()
    {
        String [] lines = codeTempLine.toString().split("\n");

        boolean isEqual;
        StringBuilder strlens = new StringBuilder();
        StringBuilder vars = new StringBuilder();

        for(String line : lines)
        {
            isEqual=false;
            for(String tk : line.split(" "))
            {
                if(!isEqual)
                {
                    isEqual = tk.equals("=");
                }else{

                    if(tk.equals("+") || tk.equals(";"))
                        continue;

                    if(tk.equals(semRef.dataTypeAux[1])) {
                        continue;
                    }

                    vars.append(tk).append(" ");
                    if(tk.startsWith("$")){
                        strlens.append("+strlen(").append(tk).append(")");
                    }else{
                        strlens.append("+strlen(").append(tk).append(")");
                    }

                }
            }
        }

        strlens.deleteCharAt(0);
        codeTempLine.setLength(0);
        codeTempLine.append(semRef.dataTypeAux[1]).append(" = (char*) malloc (").append(strlens).append(");\n");
        for(String var : vars.toString().split(" "))
        {
            codeTempLine.append("strcat(").append(semRef.dataTypeAux[1]).append(",").append(var).append(");\n");
        }
    }

    public void addTemporalCode()
    {
        block.addCode(codeTempLine.toString());
        codeTempLine.setLength(0);
    }

    public void reset(){
        codeLine.setLength(0);
        codeMacros.setLength(0);
        codeTempLine.setLength(0);
        sizeCreatedConstants=0;
        currentIdBlock=0;
        endSwitchCant=caseCant=doCant=-1;
        block=null;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
