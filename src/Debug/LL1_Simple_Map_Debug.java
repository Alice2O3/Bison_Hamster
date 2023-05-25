package Debug;

import C_Bison.Grammar.Grammar_Types.Grammar_list;
import C_Bison.Grammar.LL1.LL1_Process;
import C_Bison.Grammar.LL1.LL1_Render;
import C_Bison.Grammar.LL1.LL1_Types.Pre_Process_Exception;
import C_Bison.Grammar.LL1.LL1_Types.Resolve_Exception;
import C_Bison.Language.Rules.Simple_Read.Simple_Read_Workflow;
import Util.FileIO;

public class LL1_Simple_Map_Debug {
    private final static String input_file = "Compile_Test/LL1_simple_map_test/Example.txt";
    private final static String output_file = "Compile_Test/LL1_simple_map_test/Output.txt";

    public static void get_LL1_table_test(){
        String code = FileIO.readFile(input_file);
        if(code == null){
            System.out.print("File Read Error!\n");
            return;
        }
        Grammar_list V = Simple_Read_Workflow.str_to_grammar(code);
        if(V == null){
            System.out.print("Input Error!\n");
            return;
        }
        LL1_Process ll1 = new LL1_Process();
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
        sb.append(Simple_Read_Workflow.grammar_to_str(V)).append("\n");
        sb.append(LL1_Render.LL1_Table_Render_Map_Simple(ll1)).append("\n");
        sb.append(LL1_Render.LL1_Table_Render_Simple(ll1, 12));
        String ret = sb.toString();
        System.out.print(ret);
        FileIO.writeFile(ret, output_file);
    }

    public static void main(String[] args){
        get_LL1_table_test();
    }
}
