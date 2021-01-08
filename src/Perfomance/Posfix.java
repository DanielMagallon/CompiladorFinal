package Perfomance;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class Posfix
{
    private static int precedence(String ch)
    {
        switch (ch)
        {
            case "+":
            case "-":
            case "or":
                return 1;

            case "*":
            case "/":
            case "%":
            case "and":

            case "not":
                return 2;
        }
        return -1;
    }

    static class VarManager {

        public String var;
        public boolean inUse;

        public VarManager(String var,boolean inUse) {
            this.var = var;
            this.inUse = inUse;
        }

        @Override
        public String toString() {
            return "VarManager{" +
                    "var='" + var + '\'' +
                    ", inUse=" + inUse +
                    '}';
        }
    }

    static class VarList extends ArrayList<VarManager>{

        public void setUseValue(String varName,boolean used)
        {
            if(varName==null) throw new NullPointerException("The varName given cannot be null");

            for(VarManager vm : this)
            {
                if(vm.var.equals(varName))
                {
                    vm.inUse = used;
                    break;
                }
            }
        }

        @Override
        public boolean add(VarManager varManager) {
            for(VarManager vm : this)
            {
                if(vm.var.equals(varManager.var))
                    return false;
            }

            return super.add(varManager);
        }

        public int isInUse(String o) {
            for(VarManager vm : this)
            {
                if(vm.var.equals(o))
                    return vm.inUse ? 1 : 0;
            }

            return -1;
        }

        //Si se retorna nulo,es porque todas las variables
        //estan en uso, entonces se procederia a crear una
        public String getVarInNotUse(){
            for(VarManager vm : this)
            {
                if(!vm.inUse)
                {
                    vm.inUse=true;
                    return vm.var;
                }
            }

            return null;
        }
    }

    private static final ArrayList<String> expresionResult = new ArrayList<>();
    public static Queue<String> expressionCode = new LinkedList<>();
    private static String varName,varType;

    public static Predicate<String> creatorCode;


    public static void infixToPostfix(String[] exp, String varName, String varType, boolean call)
    {
        Posfix.varType= varType;
        Posfix.varName=varName;
        expresionResult.clear();
        expressionCode.clear();

        Stack<String> stack = new Stack<>();

        for (String c : exp)
        {
            if (c.equals("("))
                stack.push(c);

            else if (c.equals(")"))
            {
                while (!stack.isEmpty() &&
                        !stack.peek().equals("("))
                    expresionResult.add(stack.pop());

                stack.pop();
            }else if (precedence(c)==-1)
                    expresionResult.add(c);
                else
                {
                    while (!stack.isEmpty() && precedence(c)
                            <= precedence(stack.peek())){

                        expresionResult.add(stack.pop());
                    }
                    stack.push(c);
                }

        }

        while (!stack.isEmpty()){
            if(stack.peek().equals("("))
                return;
            expresionResult.add(stack.pop());
        }

        if(call)
            expressions();
        else logicalExpression();
    }

    private static void deleteRedundantNot(){
        int currentNot=0;
        Stack<String> auxExp = new Stack<>();

        for (String s : expresionResult) {
            auxExp.push(s);

            if (s.equals("not")) {
                currentNot++;

                if (currentNot % 2 == 0)
                {
                    while(auxExp.peek().equals("not")){
                        auxExp.pop();
                    }
                }

            } else currentNot = 0;

        }

        expresionResult.clear();
        expresionResult.addAll(auxExp);
    }

    private static void logicalExpression(){
        String ter,nm,exp;
        expressionCode.clear();

        VarList currentVars = new VarList();
        boolean first=true;
        Stack<String> pilaExpresn = new Stack<>();
        AtomicInteger num = new AtomicInteger(1);

        deleteRedundantNot();

        if(expresionResult.size()==1){
            expressionCode.offer(varName.concat(" = ").concat(expresionResult.remove(0)));
        }
        else while(!expresionResult.isEmpty())
        {
                ter=expresionResult.remove(0);

                switch (ter)
                {

                    case "or":
                        exp = pilaExpresn.pop();
                        ter = pilaExpresn.pop();

                        if(first)
                        {
                            nm=varName;
                        }
                        else nm = checkVariables(ter,exp,currentVars,num);

                        currentVars.add(new VarManager(nm,true));
                        expressionCode.offer(nm.concat(" = ").concat(ter).concat(" || ").concat(exp));
                        pilaExpresn.push(nm);
                        break;

                    case "and":
                        exp = pilaExpresn.pop();
                        ter = pilaExpresn.pop();

                        if(first)
                        {
                            nm=varName;
                        }
                        else nm = checkVariables(ter,exp,currentVars,num);

                        currentVars.add(new VarManager(nm,true));
                        expressionCode.offer(nm.concat(" = ").concat(ter).concat(" && ").concat(exp));
                        pilaExpresn.push(nm);
                        break;
                    case "not":
                        exp = pilaExpresn.pop();

                        if(first)
                        {
                            nm=varName;
                            currentVars.add(new VarManager(nm,true));
                        }else{
                            //Checa lo mismo, pero para una sola variable

                            //Checar si exp es una variable
                            int used = currentVars.isInUse(exp);

                            if(used==-1){
                                //La variable que se negara no existe, por lo que esta
                                //pertenece a una expresion tipo not x<=2
                                //Checamos alguna variable libre, si no la creamos
                                nm = createVariable(currentVars,num);
                            }else{
                                //La variable que se negara existe: -> ivar2 ; Ivar2 = ! ivar2;
                                expressionCode.offer(exp.concat(" = !").concat(exp));
                                pilaExpresn.push(exp);
                                break;
                            }
                        }

                        expressionCode.offer(nm.concat(" = ").concat(exp));
                        expressionCode.offer(nm.concat(" = !").concat(nm));
                        pilaExpresn.push(nm);
                        break;

                    default:
                        pilaExpresn.add(ter);
                }

                if(first) first = false;

            }

    }

    private static String checkVariables(String ter, String exp, VarList currentVars, AtomicInteger num){

        int i1 = currentVars.isInUse(ter);
        int i2 = currentVars.isInUse(exp);

        //Ninguna de las variables existe
        if (i1==-1 && i2 == -1)
        {
            return createVariable(currentVars,num);
        }
        else{
            if(i1==0)
            {
                currentVars.setUseValue(ter,true);
                currentVars.setUseValue(exp,false);
                return ter;
            }
            else if(i2==0)
            {
                currentVars.setUseValue(exp,true);
                currentVars.setUseValue(ter,false);
                return exp;
            }
            else {

                String nm=null;
                if(i1==1 && i2==1){
                    nm = ter.startsWith("$") ? exp : ter;
                }
                else if(i1==1){
                    nm = ter;

                }else if(i2==1){
                    nm = exp;
                }else{
                    System.out.printf("%s and %s are: [%d]/[%d]\n",ter,exp,i1,i2);
                }
                currentVars.setUseValue(nm,true);
                return nm;
            }
        }
    }

//    private static boolean isNum(String num){
//        try{
//             Float.parseFloat(num);
//             return true;
//        }catch(NumberFormatException e){
//            return false;
//        }
//    }


    private static void expressions()
    {
        String ter,id1,id2;
        int ind,i=0;
        VarList currentVars = new VarList();

        expressionCode.clear();
        String nm;
        AtomicInteger num = new AtomicInteger(1);
        boolean first=true;

        while (expresionResult.size()!=1)
        {
            ter = expresionResult.get(i++);

            ind = precedence(ter);

            if(ind!=-1)
            {
                id1 = expresionResult.get(i-2);
                id2 = expresionResult.get(i-3);
                nm = varName;
                if(first){
                    first=false;
                    currentVars.add(new VarManager(nm,true));
                }
                else{
                    {

                        int i1 = currentVars.isInUse(id1);
                        int i2 = currentVars.isInUse(id2);

                        if(i1==-1 && i2==-1)
                        {
                            nm = createVariable(currentVars,num);
                        }else{

                            if(i1==0)
                            {
                                currentVars.setUseValue(id1,true);
                                currentVars.setUseValue(id2,false);
                                nm = id1;
                            }
                            else if(i2==0)
                            {
                                currentVars.setUseValue(id2,true);
                                currentVars.setUseValue(id1,false);
                                nm = id2;
                            }
                            else {

                                if(i1==1 && i2==1){
                                        nm = id1.startsWith("$") ? id2 : id1;
                                }
                                else if(i1==1){
                                    nm = id1;

                                }else if(i2==1){
                                    nm = id2;
                                }else{
                                    System.out.printf("%s and %s are: [%d]/[%d]\n",id1,id2,i1,i2);
                                }
                                currentVars.setUseValue(nm,true);
                            }
                        }


                    }
                }

                expresionResult.remove(i-3);
                expresionResult.remove(i-3);
                expresionResult.remove(i-3);
                expressionCode.offer(nm+" = "+id2+" "+ter+" "+id1);
                expresionResult.add(i-3,nm);
                i-=2;

            }
        }

    }

    private static String createVariable(VarList currentVars, AtomicInteger num)
    {
        String nm;
        //Ambas variables no existen, se procede a ver cual variable que no este en uso
        // o en caso de o haber, creamos una
        String varn = currentVars.getVarInNotUse();

        //Creamos nueva variable si es nullo
        if(varn==null){

            //SI la variable no fue creada antes en el codigo la crea,
            nm = CodeCreator.getVar4Constants(varType);
            varn = nm;
            while(true) {
                //Si returna falso, es porque la variable ya existe
                //por lo que cabe la posibilidad de que esta exista dentro de
                //la lista de variables de VarList
                if (!creatorCode.test(nm)) {
                    if(currentVars.isInUse(nm)==-1){
                        //La variable no existe dentro de currentVars
                        //la añadimos
                        currentVars.add(new VarManager(nm,true));
                        break;
                    }else{
                        //La variable ya existe y se esta asignando,
                        // por lo que no podremos usar esa
                        // para asginar
                        nm=varn+num.getAndIncrement();
                    }
                } else{
                    //La variable no existe, fue creada, por lo que añadimos
                    // a varlist
                    currentVars.add(new VarManager(nm,true));
                    break;
                }
            }
        }else{
            //Obtenemos la variable que no esta en uso para asignar otra vez
            nm = varn;
            currentVars.setUseValue(nm,true);
        }

        return nm;
    }
}
