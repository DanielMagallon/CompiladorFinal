package Perfomance;

import compiler.Lexico;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Semantic
{
    public enum Token{

//        NONE(-1),
        IDENTIFIER(1),
        SYMBOL(2),
        SPECIAL_WORD(3),
        LITCAD(4),
        LITCHAR(5),
        NUM(6);

        private int id;
        Token(int id)
        {
            this.id=id;
        }

        public int getId() {
            return id;
        }
    }

    public enum Types{

        STRING("string",3),
        INT("int",0),
        CHAR("char",1),
        FLOAT("float",2);

        public String type;
        public int position;

        Types(String type,int pos) {
            this.type = type;
            this.position = pos;
        }
    }

    class IdValue{

        public String varName;
        public boolean initilized=false, isConstant;
        public int idBlock;
        public IdValue(String varName, boolean isConsntatn, int idBlock) {
            this.idBlock = idBlock;
            this.varName = varName;
            this.isConstant = isConsntatn;
        }

        @Override
        public String toString() {
            return "IdValue{" +
                    "varName='" + varName + '\'' +
                    ", initilized=" + initilized +
                    ", isConstant=" + isConstant +
                    ", idBlock=" + idBlock +
                    '}';
        }
    }

    class MacroConstant {

        public String varName;
        public String value;
        public Types type;

        public MacroConstant(String varName, String value, Types type) {
            this.varName = varName;
            this.value = value;
            this.type = type;
        }


    }

    public static int getIndexSymbol(String symbol)
    {
        switch(symbol)
        {
            case "+":
                return 0;

            case "-":
            case "*":
            case "/":
            case "%":
                return 1;

            case "<":
            case ">":
            case "<=":
            case ">=":
            case "==":
                return 2;

            default:
                return -1;
        }
    }

    //"dd"+12;
    //"dd"+12.2;
    //"dd"+'c';
    //"dd"+12;
    public static String[][][] compatibility =
            {
                    {
                        { "int","int","int",null},/* int */
                        {"char","char",null,null}, /* char */
                        {"float","float","float",null}, /* float */
                        {null,null,null,"string"}, /* string */
                    }, //+
                    {
                            {"int","int","int",null},/* int */
                            {"char","char",null,null}, /* char */
                            {"float","float","float",null}, /* float */
                            {null,null,null,null}, /* string */
                    }, //any  arithmethic operator
                    {
                            {"int","int","int",null},/* int */
                            {"char","char","char",null}, /* char */
                            {"float","float","float",null}, /* float */
                            {null,null,null,"string"}, /* string */
                    } // any logic operator
            };

    public Token currentToken;
    public boolean isConstant;
    public static String[] datatypes={"int","char","float","string"};

    //Esta lista solo almacena valores declarados en no identificadores
    //es decir, almacena cadenas ("ddd"),numeros (2,2.6), carcatr('a'),
    //son declarados automaticamente como macros con variables creadas autoamticamente
    private final ArrayList<MacroConstant> macroConstants = new ArrayList<>();
//    private final ArrayList<String> componentes;
    private final LinkedHashMap<String, ArrayList<IdValue>> dataIdTypes;
    public StringBuilder semanticMsg;
    private final Runnable onError;
    private Runnable onAccepted;
    public StringBuilder expresion;
    public CodeCreator codeCreator;
    private String currentType;
    private boolean isAsign,isPuntoComa,comparing,isSwitch,isDefault,isCase,isReading,isPrintf,isPrintln;
    public String symbolCompare;

    /*
     * DataMacroConstant: 1er valor = nombreVariable, 2do valor = tipo
     */
    public String [] dataTypeAux;

    public Semantic(Runnable error,Runnable onAccepted)
    {
        this.onError=error;
        this.onAccepted=onAccepted;
        expresion = new StringBuilder();
        semanticMsg = new StringBuilder();
//        componentes = new ArrayList<>();
        dataIdTypes = new LinkedHashMap<>();
        codeCreator = new CodeCreator(this);
    }

    public void reset(){

        switchDataStack.clear();
        codeCreator.reset();
        dataIdTypes.clear();
        semanticMsg.setLength(0);
        expresion.setLength(0);
        isDefault=isCase=isReading=isPrintf=isPrintln=comparing=isPuntoComa=isConstant=isSwitch=isAsign=false;
        dataTypeAux=null;
        currentType=symbolCompare=null;
        macroConstants.clear();
    }



    private String getCompatibility(Types type1, Types type2,int index)
    {
        return compatibility[index][type1.position][type2.position];
    }


    class SwitchData{
        public String varname,blockEndName;
        public Types type;

        public SwitchData(String varname, Types type, String blockEnd) {
            this.blockEndName = blockEnd;
            this.varname = varname;
            this.type = type;
        }

        @Override
        public String toString() {
            return "SwitchData{" +
                    "varname='" + varname + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    protected Stack<SwitchData> switchDataStack = new Stack<>();


    public boolean semanticAnalyze(String comp)
    {
        switch (currentToken)
        {
            case SPECIAL_WORD:
                switch (comp)
                {
                    case "class":
                        currentType="class";
                        break;

                    case "read":
                        isReading = true;
                        break;

                    case "case":
                        isCase = true;
                        codeCreator.addCase();
                        break;

                    case "default":
                        isDefault = true;
                        codeCreator.addDefault();
                        break;

                    case "switch":
                        codeCreator.addSwitch();
                        isSwitch=true;
                        break;

                    case "println":
                        isPrintln=true;
                        break;
                    case "printf":
                        isPrintf=true;
                        break;

                    case "do":
                        currentType=null;
                        codeCreator.addBlock(codeCreator.getNextDo(),"Do");
                        break;
                    case "main":
                        currentType=null;
                        codeCreator.addBlock("main","Main");
                        break;

                    case "or":
                    case "and":
                    case "not":
                        //siempre deberia entrar
                        if(comparing){
//                            String auxExp = expresion.toString().replaceAll(" ","");
//                            expresion.setLength(0);
                            expresion.append(comp).append(" ");
                        }
                        break;

                    case "while":
                        comparing=true;
                        break;

                    case "const":
                        isConstant=true;
                        codeCreator.appendCode(comp);
                        break;

                    default:
                        int indexType = Arrays.asList(datatypes).indexOf(comp);

                        //Es algun tipo de dato
                        if(indexType!=-1)
                        {
                            currentType=comp;
                            if(!dataIdTypes.containsKey(comp))
                                dataIdTypes.put(comp,new ArrayList<>());
                            codeCreator.appendCode(comp);
                        }
                }
            break;

            case LITCAD:

                if(isCase){
                    isCase=false;

                    if(convertToIf(comp,Types.STRING))
                        return true;
                    else
                        setError(String.format("El tipo del valor %s no es compatible con " +
                                        "el tipo de dato de la variable %s usada en el switch\n",
                                comp,switchDataStack.peek().varname));
                    break;
                }

                if(isPrintln || isPrintf){
                    expresion.append(comp).append(" ");
                    break;
                }

                String vname  = getVarNameMacroConstant(comp);

                if(vname==null) {
                    String mCVarName = codeCreator.addConstant(comp);
                    macroConstants.add(new MacroConstant(mCVarName, comp, Types.STRING));
                    expresion.append(mCVarName).append(" ");
                }else{
                    expresion.append(vname).append(" ");
                }

                break;

            case LITCHAR:

                if(isCase){
                    isCase=false;

                    if(convertToIf(comp,Types.CHAR))
                        return true;
                    else
                        setError(String.format("El tipo del valor %s no es compatible con " +
                                        "el tipo de dato de la variable %s usada en el switch\n",
                                comp,switchDataStack.peek().varname));
                    break;
                }

                if(isPrintln || isPrintf){
                    expresion.append(comp).append(" ");
                    break;
                }
                vname  = getVarNameMacroConstant(comp);

                if(vname==null) {
                    String mCVarName = codeCreator.addConstant(comp);
                    macroConstants.add(new MacroConstant(mCVarName, comp, Types.CHAR));
                    expresion.append(mCVarName).append(" ");
                }else{
                    expresion.append(vname).append(" ");
                }
                break;

            case NUM:

                if(comp.startsWith("!"))
                    comp=comp.replace("!","-");

                if(isCase){
                    isCase=false;

                    if(convertToIf(comp,comp.contains(".") ? Types.FLOAT : Types.INT))
                            return true;
                    else
                        setError(String.format("El tipo del valor %s no es compatible con " +
                                        "el tipo de dato de la variable %s usada en el switch\n",
                                comp,switchDataStack.peek().varname));
                    break;
                }

                if(isPrintln || isPrintf){
                    expresion.append(comp).append(" ");
                    break;
                }
                vname  = getVarNameMacroConstant(comp);

                if(vname==null) {
                    if(comp.startsWith("0c"))
                        comp = comp.replace("0c","0");

                    String mCVarName = codeCreator.addConstant(comp);
                    macroConstants.add(new MacroConstant(mCVarName, comp, comp.contains(".") ? Types.FLOAT :
                            Types.INT));
                    expresion.append(mCVarName).append(" ");
                }else{
                    expresion.append(vname).append(" ");
                }
                break;

            case SYMBOL:
                switch (comp)
                {
                    case ";":
                        isPuntoComa=true;


                        if(comparing)
                        {
                            comparing=false;
                            currentType=null;
                            boolean ex =  evalLogicExpression();
                            if(isConstant) isConstant=false;
                            return ex;
                        }
                        if(isAsign)
                        {
                            isAsign=false;
                            initVariable();
                            boolean ex=evalExpression();
                            currentType=null;
                            if(isConstant) isConstant=false;
                            return  ex;
                        }
                        if(isPrintln || isPrintf)
                        {
                            return convertPrint();
                        }
                        if(isReading){
                             convertReader();
                            return true;
                        }
                        else{
                            //Agrega el codigo necesario en caso de que la ultima variable no haya sido asignada

                            codeCreator.appendCode(";\n").addCode();
                            if(dataTypeAux[0].equals("string"))
                            {
                                codeCreator.toCString(false);
                            }
                            codeCreator.addTemporalCode();
                            dataTypeAux=null;
                            isAsign=false;
                            expresion.setLength(0);
                            if(isConstant) isConstant=false;
                            currentType=null;
                            return true;
                        }

                    case ",":
                        isPuntoComa=false;
                        if(isAsign)
                        {
                            isAsign=false;
                            initVariable();
                            return evalExpression();
                        }else if(isPrintf || isPrintln){
                            expresion.append(",");
                            break;
                        }
                        else{
                            codeCreator.appendCode(", ");
                        }
                        break;

                    case "+":
                    case "-":
                    case "*":
                    case "/":
                    case "%":
                        expresion.append(comp).append(" ");
                        break;

                    case "(":
                    case ")":
                        if(isAsign || comparing || isPrintf || isPrintln){

                            expresion.append(comp).append(" ");
                        }
                        break;

                    case "}":

                        if(codeCreator.getCurrentIdBlockName().equals("Default")){
                            codeCreator.finishBlock();
                            isDefault=false;
                        }

                        else if(codeCreator.getCurrentIdBlockName().equals("Switch")){
                            String endS = switchDataStack.pop().blockEndName;
                            codeCreator.finishBlock();
                            codeCreator.appendCode("\n").appendCode(endS).appendCode(": ;\n").addCode();
                        }

                        else if(codeCreator.getCurrentIdBlockName().equals("Do")){
                            codeCreator.finishBlock();
                        }

                        else if(codeCreator.getCurrentIdBlockName().equals("Case")){

                            System.out.println("End case");
                            codeCreator.addCode();
                            codeCreator.finishBlock();
                        }

                        else if(codeCreator.getCurrentBlockName().equals("main"))
                        {
                            codeCreator.finishBlock();
                        }
                        break;

                    case "=":
                        symbolCompare=comp;
                        if(isConstant(dataTypeAux[1],dataTypeAux[0],true))
                        {
                            semanticMsg.append(String.format("semanticAnalyze: La variable %s no puede" +
                                            " ser asignada otra vez debido a que es una constante",
                                    dataTypeAux[1]));
                            onError.run();
                            return false;
                        }
                        else isAsign=true;
                        break;

                    case "<=":
                    case ">=":
                    case "<":
                    case ">":
                    case "==":
                        symbolCompare=comp;
                        expresion.append(comp).append(" ");
                        break;
                }
            break;

            case IDENTIFIER:

                if(isCase){
                    isCase=false;

                    //Comprueba si el case [varname] exista, este incializad y ademas es de un tipo compatible
                    if(verifyExistAndInit(comp,false))
                    {
                        Types t = getVariableType(comp);
                        if(convertToIf(comp,t))
                            return true;
                        else
                              setError(String.format("El tipo de la variable %s no es compatible con " +
                                  "el tipo de dato de la variable %s usada en el switch\n",
                                  comp,switchDataStack.peek().varname));
                    }
                    return false;
                }

                if(isSwitch){
                    isSwitch=false;

                    if(verifyExistAndInit(comp,false)){
                        switchDataStack.add(new SwitchData(comp,getVariableType(comp),
                                codeCreator.getNextEndSwitch()));
                        return true;
                    }

                    return false;
                }

                if(isReading){

                    if(expresion.length()!=0) {
                        System.out.println("The exprs have: "+expresion);
                        throw new IllegalStateException("expresion must be empty when is " +
                                "used the read method. Verify code");
                    }

                    //Solo se debe checar si existe, ya que el read se ebcarga de inicializar
                    dataTypeAux=checkExistingVariable(comp);

                    if(dataTypeAux==null){
                            semanticMsg.append(String.format("semanticAnalyze: No se ha declarado el identifcador %s ",
                                    comp));
                            onError.run();
                            return false;
                    }else if(isConstant(comp,dataTypeAux[0],true)){
                        //Checar si es una constante y esta no esta incializada
                        semanticMsg.append(String.format("semanticAnalyze: La constante %s ya ha sido incializada",
                                comp));
                        onError.run();
                        return false;
                    }

                    return true;
                }

                if(isPrintf || isPrintln){
                    if(currentType!=null) {
                        throw new IllegalStateException("CurrentType must be null when is " +
                                "used printf or println. Verify code");
                    }

                    return verifyExistAndInit(comp,true);

                }

                //Si es un identificador y el tipo actual es nuloy hay asingacion
                // es porque es esta evaluando una expresion en otra linea, y no en una
                //declaracion: int x; x=->x+2*x;
                if (currentType==null)
                {
                    if(isAsign){
                       return verifyExistAndInit(comp,true);
                    }else if(comparing){

                        if(verifyExistAndInit(comp,true)){
                            dataTypeAux = checkExistingVariable(comp);
                            return true;
                        }
                        return false;
                    }
                    else{

                        //va a guadar la variable y su tipo en variable auxiliar
                        dataTypeAux = checkExistingVariable(comp);
                        if(dataTypeAux==null){
                            semanticMsg.append(String.format("semanticAnalyze: No se ha declarado el identifcador %s ",
                                    comp));
                            onError.run();
                            return false;
                        }
                    }
                }
                else if(currentType.equals("class"))
                {
                    codeCreator.setFileName(comp);
                    currentType=null;
                }else{
                    if(isAsign){
                        //Si esto se cumple es porque currentType trae algun tipo de dato
                        //y se esta declrando y asingnando en linea:
                        //int x=->y*z;
                        return verifyExistAndInit(comp,true);
                    }
                    else{
                        //Se esta creadbo,declarando algun tipo de variable
                        //int x;
                        boolean g = existsVariableOnBlock(comp);
                        if(g){

                            //va a guadar la variable y su tipo en variable auxiliar
                            dataTypeAux = checkExistingVariable(comp);

                            if(dataTypeAux==null){
                                semanticMsg.append(String.format("semanticAnalyze: No se ha declarado el identifcador %s ",
                                        comp));
                                onError.run();
                                return false;
                            }
                            if(currentType.equals("string"))
                                codeCreator.appendCode("*").appendCode(comp);
                            else if(isConstant)
                                codeCreator.appendCode(" * ").appendCode(comp);
                            else
                                codeCreator.appendCode(comp);

                        }

                        return g;
                    }

                }
                break;
        }

        return true;
    }

    private boolean convertToIf(String comp, Types type)
    {
        if(getCompatibility(type,switchDataStack.peek().type,getIndexSymbol("+"))!=null) {
            String compString;

            if (type == Types.STRING)
                compString = String.format
                        ("\nif(strcmp(%s,%s)!=0)\n", switchDataStack.peek().varname, comp);
            else
                compString = String.format
                        ("\nif(%s!=%s)\n", switchDataStack.peek().varname, comp);

            codeCreator.appendCode(compString).appendCode
                    ("goto ").appendCode(codeCreator.getCurrentCase()).appendCode
                    (";\n");

            return true;
        }
        return false;
    }

    public Types getTypeMacro(String varname)
    {
        for (MacroConstant macroConstant : macroConstants) {
            if (macroConstant.varName.equals(varname))
            {
                return macroConstant.type;
            }
        }
        return null;
    }

    public String getValueMacroConstant(String varname)
    {
        for (MacroConstant macroConstant : macroConstants) {
            if (macroConstant.varName.equals(varname))
            {
                return macroConstant.value;
            }
        }
        return null;
    }

    public String getVarNameMacroConstant(String value)
    {
        for (MacroConstant macroConstant : macroConstants) {
            if (macroConstant.value.equals(value))
            {
                return macroConstant.varName;
            }
        }
        return null;
    }

//    private void setValue2IdValue(String type, String varName,String value){
//        for (IdValue idValue : dataIdTypes.get(type)) {
//            if(idValue.id.equals(varName)){
//                idValue.value=value;
//                break;
//            }
//        }
//    }

   /* public String getValueOfId(String type, String varName){
        for (IdValue idValue : dataIdTypes.get(type)) {
            if(idValue.id.equals(varName)){

                return idValue.value;
            }
        }
        return null;
    }*/

    private void initVariable()
    {
        int id=codeCreator.getCurrentIdBlock();
        while(id>=0)
        {
            for (IdValue idv : dataIdTypes.get(dataTypeAux[0])) {
                if (idv.varName.equals(dataTypeAux[1]) && idv.idBlock == id) {
                    idv.initilized = true;
                    break;
                }
            }
            id--;
        }

    }

    private boolean evalLogicExpression(){
        String[] expArray = expresion.toString().split(" ");
        String compatibility;


        Types type1,type2;

        expresion.setLength(0);
        boolean swap=false;
        String comp=null;
        for (int i = 0; i < expArray.length; i++)
        {
            comp = expArray[i];

            switch (comp)
            {
                case "(":
                case ")":
                case "not":
                case "and":
                case "or":
                    if (swap){
                        swap=false;
                        expresion.append(" ");
                    }
                    expresion.append(comp).append(" ");
                    break;

                case "<=":
                case "<":
                case ">":
                case ">=":
                case "==":
                    type1 = getTypeOf(expArray[i-1]);
                    type2 = getTypeOf(expArray[i+1]);

                    compatibility = getCompatibility(type1,type2,2);

                    if(compatibility==null){
                        setError(String.format("evalLogicExpression: La comparacion entre %s %s %s no es compatible\n",
                                expArray[i-1].startsWith("$") ? getValueMacroConstant(expArray[i-1]): expArray[i-1],
                                comp,
                                expArray[i+1].startsWith("$") ? getValueMacroConstant(expArray[i+1]): expArray[i+1]));
                        return false;
                    }

                default:
                    swap=true;
                    expresion.append(comp);
            }
        }

        String varC = CodeCreator.getVar4Constants("int");

        assert varC != null;
        boolean exist;

        //verifficar esta mamada
        exist = existVariable("int",varC );



        if (!exist) {
            codeCreator.appendTemporalCode("int").appendTemporalCode
                    (" ").appendTemporalCode(varC).appendTemporalCode(";\n");
            //esto solo es para crear una variable como $iVar en caso de que no existe previamenente
            IdValue idValue = new IdValue(varC, false,codeCreator.getCurrentIdBlock());
            idValue.initilized = true;
            dataIdTypes.get("int").add(idValue);


        }

        Posfix.creatorCode = (var)->{

            if(existVariable("int",var))
                return false;

            codeCreator.appendTemporalCode("int").appendTemporalCode
                    (" ").appendTemporalCode(var).appendTemporalCode(";\n");
            IdValue idValue = new IdValue(var, false,codeCreator.getCurrentIdBlock());
            idValue.initilized = true;
            dataIdTypes.get("int").add(idValue);
            return true;
        };

        Posfix.infixToPostfix(expresion.toString().split(" "), varC,"int",false);

        while(!Posfix.expressionCode.isEmpty())
        {
            comp = Posfix.expressionCode.poll();
            codeCreator.appendTemporalCode(comp).appendTemporalCode(";\n");
        }

        codeCreator.toCString(true);

        if(comp==null) throw new NullPointerException("The [comp] variables is null.\nError in eval logical exp.");

        //Esto solo para cuando el bloque pertenece a Do while:
//        if(codeCreator.getCurrentIdBlockName().equals("Do"))
        if(codeCreator.getLastBlock().blockIdentifier.equals("Do"))
        {

            codeCreator.appendTemporalCode("if (").appendTemporalCode(comp.split(" ")[0]).appendTemporalCode(")\n");
            codeCreator.appendTemporalCode("goto ").appendTemporalCode
                    (codeCreator.getLastBlock().blockName).appendTemporalCode(";\n");
            codeCreator.addTemporalCode();

            //Se termino de usar la expreson, se debe elminar
        }
        expresion.setLength(0);

        return true;
    }

    public Types getTypeOf(String varName){
        Types type1 = getVariableType(varName);

        if(type1==null){
            type1 = getTypeMacro(varName);
        }

        if(type1==null) throw new NullPointerException("The given "+varName+" is null.\nVerify the code.");

        return type1;
    }

    private boolean evalExpression()
    {
        boolean get=true;

        String[] expArray = expresion.toString().split(" ");


        if(expArray.length==1)
        {
            Types type1;
            String vl;
            if(expArray[0].startsWith("$")){
                type1 = getTypeMacro(expArray[0]);
                vl = getValueMacroConstant(expArray[0]);
            }else{
                //Es variable
                vl = expArray[0];
                type1 = getVariableType(vl);
            }

            assert type1 != null;
            vl = getCompatibility(Types.valueOf(dataTypeAux[0].toUpperCase()),type1,getIndexSymbol("+"));

            if(vl!=null && vl.equals(dataTypeAux[0])){
                get=true;

                if(currentType==null)
                    codeCreator.appendCode(dataTypeAux[1]);

                if(isConstant)
                    codeCreator.appendCode(" = (int *) ");
                    else codeCreator.appendCode(" = ");
                codeCreator.appendCode(expArray[0]);
                if(isPuntoComa)
                {
                    codeCreator.appendCode(";\n").addCode();
                }
                else codeCreator.appendCode(", ");
            }else{
                semanticMsg.append(String.format
                        ("evalExpression: El termino %s de tipo de dato %s no coincide con el tipo de dato " +
                                "de la variable %s\n", vl,type1.type,dataTypeAux[1]));
                onError.run();
                get=false;
            }

        }

        else {
            //Va a crear una variable para axuliares en caso de ser constante:
            // const int x=r+r; -> crea int $iVar, asignara: $iVar = r+r; al utlimo si es constante:
            // x=$ivar;
            String varC = null;
            String compatibility;
            boolean firstTime = true;
            boolean isCons;
            if (isCons = isConstant(dataTypeAux[1], dataTypeAux[0], false)) {
                varC = CodeCreator.getVar4Constants(dataTypeAux[0]);
                assert varC != null;
                boolean exist;

                exist = existVariable(dataTypeAux[0],varC );


                if (!exist) {
                    codeCreator.appendTemporalCode(dataTypeAux[0]).appendTemporalCode
                            (" ").appendTemporalCode(varC).appendTemporalCode(";\n");
                    IdValue idValue = new IdValue(varC, false,codeCreator.getCurrentIdBlock());
                    idValue.initilized = true;
                    dataIdTypes.get(dataTypeAux[0]).add(idValue);


                }

                Posfix.creatorCode = (var)->{

                    if(existVariable(dataTypeAux[0],var))
                        return false;

                    codeCreator.appendTemporalCode(dataTypeAux[0]).appendTemporalCode
                            (" ").appendTemporalCode(var).appendTemporalCode(";\n");
                    IdValue idValue2 = new IdValue(var, false,codeCreator.getCurrentIdBlock());
                    idValue2.initilized = true;
                    dataIdTypes.get(dataTypeAux[0]).add(idValue2);
                    return true;
                };
                Posfix.infixToPostfix(expArray, varC,dataTypeAux[0], true);
            } else {

                {
                    Posfix.creatorCode = (var)->{

                        if(existVariable(dataTypeAux[0],var))
                            return false;

                        codeCreator.appendTemporalCode(dataTypeAux[0]).appendTemporalCode
                                (" ").appendTemporalCode(var).appendTemporalCode(";\n");
                        IdValue idValue = new IdValue(var, false,codeCreator.getCurrentIdBlock());
                        idValue.initilized = true;
                        dataIdTypes.get(dataTypeAux[0]).add(idValue);
                        return true;
                    };
                }

                //Aqui no es necesario la varC porque datTypeAux ya existe
                // por lo taanto siempre por primera vez entrara y asignara dicha variable
                Posfix.infixToPostfix(expArray, dataTypeAux[1],dataTypeAux[0],true);
            }

            Types type1 , type2;

            String valueId1, valueId2;
            int indexSymbol;

            while (!Posfix.expressionCode.isEmpty()) {
                String[] expresionSplit = Posfix.expressionCode.poll().split(" ");

                indexSymbol = getIndexSymbol(expresionSplit[3]);

                //Es macro
                if (expresionSplit[2].startsWith("$")) {
                    type1 = getTypeMacro(expresionSplit[2]);
                    if (type1 != null)
                        valueId1 = getValueMacroConstant(expresionSplit[2]);
                    else {
                        type1 = getVariableType(expresionSplit[2]);
                        valueId1 = expresionSplit[2];
                    }
                } else {
                    //Es variable
                    valueId1 = expresionSplit[2];
                    type1 = getVariableType(valueId1);
                }

                //Es macro
                if (expresionSplit[4].startsWith("$")) {
                    type2 = getTypeMacro(expresionSplit[4]);
                    if (type2 != null)
                        valueId2 = getValueMacroConstant(expresionSplit[4]);
                    else {
                        type2 = getVariableType(expresionSplit[4]);
                        valueId2 = getValueMacroConstant(expresionSplit[4]);
                    }

                } else {
                    //Es variable
                    valueId2 = expresionSplit[4];
                    type2 = getVariableType(valueId2);
                }


                if (type1 == null) throw new AssertionError("Type 1 is null");
                if (type2 == null) throw new AssertionError("Type 2 is null");

                compatibility = getCompatibility(Types.valueOf(dataTypeAux[0].toUpperCase()),
                                type1, indexSymbol);

                if (compatibility == null) {
                    //No coinciden los tipos
                    semanticMsg.append(String.format
                            ("evalExpression: Las valores de %s & %s de tipos [%s] & [%s] no son compatibles\n",
                                    valueId1, valueId2, type1.type, type2.type));
                    onError.run();
                    get = false;
                    break;
                } else {

                    compatibility = getCompatibility(Types.valueOf(compatibility.toUpperCase()),
                            type2, indexSymbol);

                    if (compatibility!=null && compatibility.equals(dataTypeAux[0])) {

                        if (isPuntoComa) {
                            if (firstTime) {
                                if(currentType!=null)
                                    codeCreator.appendCode(";\n").addCode();
                                else codeCreator.addCode();
                                firstTime = false;
                            }
//                            codeCreator.appendTemporalCode(codeCreator.getCurrentTabulation());
                            for (String s : expresionSplit) {
                                codeCreator.appendTemporalCode(s).appendTemporalCode(" ");
                            }
                            codeCreator.appendTemporalCode(";\n");
                        } else {
                            if(firstTime)
                            {
                                codeCreator.appendCode(", ");
                                firstTime=false;
                            }

//                            codeCreator.appendTemporalCode(codeCreator.getCurrentTabulation());
                            for (String s : expresionSplit) {
                                codeCreator.appendTemporalCode(s).appendTemporalCode(" ");
                            }
                            codeCreator.appendTemporalCode(";\n");
                        }

                    } else {
                        semanticMsg.append(String.format
                                ("evalExpression: EL termino de [%s %s %s] no coincide con el tipo de dato " +
                                                "de la variable %s\n",
                                        valueId1, expresionSplit[3], valueId2, dataTypeAux[1]));
                        onError.run();
                        get = false;
                        break;
                    }
                }
            }

            //Se debe igualar la constante creada a la varuable creada cost int x; x=$iVar;
            if(isCons){
                codeCreator.appendTemporalCode(dataTypeAux[1]).appendTemporalCode(" = ").appendTemporalCode
                        (varC).appendTemporalCode(";\n");
            }
        }

        if(isPuntoComa && compatibility!=null)
        {
            if(dataTypeAux[0].equals("string") && expArray.length>1)
            {
                codeCreator.toCString(false);
            }

            codeCreator.addTemporalCode();
        }

        dataTypeAux=null;
        expresion.setLength(0);
        return get;
    }

    private boolean isConstant(String var,String type,boolean checkInit)
    {
        int id=codeCreator.getCurrentIdBlock();

        while(id>=0) {
            for (IdValue idValue : dataIdTypes.get(type)) {
                if (idValue.varName.equals(var) && idValue.idBlock==id) {
                    return checkInit ? idValue.isConstant && idValue.initilized : idValue.isConstant;
                }
            }
            id--;
        }
        return false;
    }

    private void convertReader()
    {
        String app = codeCreator.convertToScanf(dataTypeAux[1],dataTypeAux[0]);

        codeCreator.appendCode(app).appendCode("\n").addCode();

        //Inicializa la variale
        initVariable();

        dataTypeAux=null;
        isReading=false;
        expresion.setLength(0);
    }

    private boolean convertPrint()
    {
        //Checar expresion del print  y convertir

        if(isPrintln && isPrintf){
            throw new IllegalStateException("Tha variables isPrintln and isPrintf only can be one in true.");
        }

        boolean accepted=true;

        String c = expresion.toString().trim();
        expresion.setLength(0);
        expresion.append(c);
        expresion.deleteCharAt(0); //elimina siempre el 1er parenteis (
        expresion.deleteCharAt(expresion.length()-1); //elimina siempre el ultimo parenteis )

        if(isPrintf) {
            String expArray[] = expresion.toString().split(",");

            if(expArray.length==1){
                codeCreator.appendCode(codeCreator.convertToPrintf(expArray[0],null)).appendCode("\n").addCode();
            }else{
                String values[][] =  new String[expArray.length-1][2];
                for (int i = 0; i < values.length; i++) {

                    expArray[i+1] = expArray[i+1].trim();

                        values[i][0] = expArray[i+1]; //Guarda la variable/paarametro dado

                        if(Objects.equals(Lexico.isString(expArray[i + 1]), Lexico.cadena_correcta))
                            values[i][1] = "string";
                        else if(Objects.equals(Lexico.isChar(expArray[i + 1]), Lexico.cadena_correcta))
                            values[i][1] = "char";

                        else if(Objects.equals(Lexico.isNum(expArray[i + 1]), Lexico.cadena_correcta))
                            values[i][1] = expArray[i+1].contains(".") ? "float" : "int";

                        else{
                            //Si no es ningun valor como cadena/char/numero entonces puede ser un id
                            if(verifyExistAndInit(expArray[i+1],false))
                            {
                                Types t = getVariableType(expArray[i+1]);
                                if(t==null) throw new NullPointerException
                                        ("convertPrint: The type returned is null. It seems the variable "+expArray[i+1]+" not exist");
                                values[i][1] = t.type;
                            }else {
                                accepted=false;
                                break;
                            }
                        }
                }

                if(accepted)
                codeCreator.appendCode(codeCreator.convertToPrintf(expArray[0],values)).appendCode("\n").addCode();
                //Extraer el tipo de dato de cada argumento/variable
            }
            isPrintf=false;
        }

        else if(isPrintln){

            String var = expresion.toString().trim();

            if(Objects.equals(Lexico.isString(var), Lexico.cadena_correcta))
                var = codeCreator.convertPrintlnToPf(var,"string");

            else if(Objects.equals(Lexico.isChar(var), Lexico.cadena_correcta))
                var = codeCreator.convertPrintlnToPf(var,"char");

            else if(Objects.equals(Lexico.isNum(var), Lexico.cadena_correcta))
                var = var.contains(".") ? codeCreator.convertPrintlnToPf(var,"float") :
                    codeCreator.convertPrintlnToPf(var,"int");

            else if(verifyExistAndInit(var,false))
            {
                Types t = getVariableType(var);
                if(t==null) throw new NullPointerException
                        ("convertPrint: The type returned is null. It seems the variable "+var+" not exist." +
                                "Check the methd verifyExistAndInit is ok");

                var = codeCreator.convertPrintlnToPf(var,t.type);

            }else accepted=false;

            if (accepted)
                codeCreator.appendCode(var).appendCode("\n").addCode();

            isPrintln=false;
        }

        expresion.setLength(0);
        return accepted;
    }

    private boolean verifyExistAndInit(String comp, boolean append)
    {
        String data[] = checkExistingVariable(comp);


        if(data==null){
            semanticMsg.append(String.format("verifyExistAndInit: No se ha declarado el identifcador %s ",
                    comp));
            onError.run();
            return false;
        }
        else{
            if(data[2].equals("false")){
                setError(String.format("verifyExistAndInit: El identificador %s no se ha inicializado\n",comp));
                return false;
            }
            else{
                if(append)
                expresion.append(comp).append(" ");
            }
        }

        return true;
    }

    private void setError(String msg){
        semanticMsg.append(msg);
        onError.run();
    }

    protected Types getVariableType(String varname)
    {
        Set<String> keys = dataIdTypes.keySet();

        int id = codeCreator.getCurrentIdBlock();

        while(id>=0) {
            for (String k : keys) {
                for (IdValue idValue : dataIdTypes.get(k)) {
                    if (idValue.varName.equals(varname) && idValue.idBlock == id)
                    {
                        return Types.valueOf(k.toUpperCase());
                    }
                }
            }
            id--;
        }

        return null;
    }

    //Checa si existe una variable dada, de x tipo dado
    private boolean existVariable(String type,String varName)
    {
        if(dataIdTypes.containsKey(type))
        for (IdValue idValue : dataIdTypes.get(type)) {
            if(idValue.varName.equals(varName) && idValue.idBlock==codeCreator.getCurrentIdBlock())
            {
                return true;
            }
        }else{ dataIdTypes.put(type,new ArrayList<>()); }
        return false;
    }

    //Checa si existe una variable, en caso de, retorna datos como su tipo, nombre, y si esta incizliada
    private String[] checkExistingVariable(String comp){

        Set<String> keys = dataIdTypes.keySet();
        int id=codeCreator.getCurrentIdBlock();

        while(id>=0)
        {
            for (String k : keys) {
                for (IdValue idValue : dataIdTypes.get(k)) {
                    //Checa si el nombre de la variable coincide con la de la lista de variables
                    //en caso de, checa ahora que el bloque de la variable sea igual id
                    if (idValue.varName.equals(comp) && idValue.idBlock == id) {
                        return new String[]{k, comp, String.valueOf(idValue.initilized)};
                    }
                }
            }

            //Si no encuentra ninguna coincidencia, posilbmente la vaible xiste en bloques padres
            id--;
        }


        return null;
    }

    //Checa si ya existe la variable dada, en caso de que no, la crea
    private boolean existsVariableOnBlock(String comp){
        Set<String> keys = dataIdTypes.keySet();
        AtomicBoolean status= new AtomicBoolean(true);

        keys.forEach(k->{

            ArrayList<IdValue> idValue = dataIdTypes.get(k);

            idValue.forEach(idValue1 ->
            {
                //Aqui debe checar solo si la variable existe dentro del bloque de codigo
                //da igual si esta variable existe en otro bloque como un do, o un case
                //si existe manda error
                 if(idValue1.varName.equals(comp) && idValue1.idBlock==codeCreator.getCurrentIdBlock()){
                    semanticMsg.append(String.format("checkDataTypes: Ya existe el identifcador %s declarado con el "+
                            "tipo de dato %s\n",comp,k));
                    onError.run();
                    status.set(false);
                }
            });

        });


        if(status.get()){
            //Si no existe el idemtifcador lo crea, con el tipo que le corsonde
            dataIdTypes.get(currentType).add(new IdValue(comp,isConstant,codeCreator.getCurrentIdBlock()));
        }

        return status.get();
    }
}
