package Debug;

import Grammar.LL1.Process;
import Grammar.Process_Lexing.*;
import Grammar.Types.*;
import Grammar.LL1.Types.*;

public class LL1_analyze_Debug {
    static String input_path = "Compile_Test/LL1_table_test/Easy_C.txt";
    static String output_path = "Compile_Test/LL1_table_test/Easy_C_LL1_Table.txt";

    public static void main(String[] args) {
        Workflow workflow = new Workflow();
        Workflow.Process_Exception e1 = workflow.process_text(filesystem.FileIO.readFile(input_path));
        if(e1 != Workflow.Process_Exception.NORMAL){
            System.out.print("\nInput Error!\n");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(workflow.getSymbolInfo());
        sb.append("\nRule_Table:\n");
        sb.append(Convert.Grammar_List_to_str_full(workflow.getLL1List()));
        sb.append("\n");
        Grammar_list V = workflow.getLL1List();
        Process ll1 = new Process();
        if(ll1.pre_process(V) != Pre_Process_Exception.NORMAL){
            System.out.print("\nInput Error!\n");
            return;
        }
        Resolve_Exception e2 = ll1.resolve_LL1_table();
        if(e2 == Resolve_Exception.HAS_LEFT_RECURSION){
            System.out.print("\nGrammar has left recursion!\n");
            return;
        }
        else if(e2 == Resolve_Exception.NOT_LL1){
            System.out.print("\nGrammar is not LL1!\n");
            return;
        }
        sb.append(Render.LL1_Table_Render(ll1, 48));
        String ret = sb.toString();
        System.out.print(ret);
        filesystem.FileIO.writeFile(ret, output_path);
    }
}