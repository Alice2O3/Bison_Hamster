package Debug;

import Filesystem.FileIO;
import C_Bison.Grammar.LL1.Types.*;
import C_Bison.Grammar.LL1.Process;
import C_Bison.Grammar.LL1.Render;
import C_Bison.Grammar.Types.*;
import C_Bison.Grammar.Simple_Read.Workflow;

public class LL1_Simple_Debug {
    private final static String input_file = "Compile_Test/LL1_simple_test/LL1_simple_test.txt";
    private final static String output_file = "Compile_Test/LL1_simple_test/output.txt";

    public static void get_LL1_table_test(){
        String code = FileIO.readFile(input_file);
        if(code == null){
            System.out.print("File Read Error!\n");
            return;
        }
        Grammar_list V = Workflow.str_to_grammar(code);
        if(V == null){
            System.out.print("Input Error!\n");
            return;
        }
        Process ll1 = new Process();
        if(ll1.pre_process(V) != Pre_Process_Exception.NORMAL){
            System.out.print("Pre Process Error!\n");
            return;
        }
        Resolve_Exception e = ll1.resolve_LL1_table();
        if(e == Resolve_Exception.HAS_LEFT_RECURSION){
            System.out.print("Grammar has left recursion!\n");
            return;
        }
        else if(e == Resolve_Exception.NOT_LL1){
            System.out.print("Grammar is not LL1!\n");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Workflow.grammar_to_str(V)).append("\n");
        sb.append(Render.LL1_Table_Render_Simple(ll1, 16));
        String ret = sb.toString();
        System.out.print(ret);
        FileIO.writeFile(ret, output_file);
    }

    public static void main(String[] args){
        get_LL1_table_test();
    }
}
