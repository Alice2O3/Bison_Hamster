package Debug;

import LL1.Process;
import LL1.Process_Lexing.*;
import LL1.Types.*;
import LL1.Process.Types.*;

public class LL1_analyze_Debug {
    static String input_path = "Compile_Test/LL1_table_test/Example.txt";
    static String output_path = "Compile_Test/LL1_table_test/Example_LL1_Table.txt";

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
        sb.append(Convert.LL1_List_to_str_full(workflow.getLL1List()));
        sb.append("\n");
        LL1_list V = workflow.getLL1List();
        Process ll1 = new Process();
        if(ll1.pre_process(V) != Pre_Process_Exception.NORMAL){
            System.out.print("\nInput Error!\n");
            return;
        }
        Resolve_Exception e2 = ll1.resolve_LL1_table();
        if(e2 == Process.Types.Resolve_Exception.HAS_LEFT_RECURSION){
            System.out.print("\nGrammar has left recursion!\n");
            return;
        }
        else if(e2 == Resolve_Exception.NOT_LL1){
            System.out.print("\nGrammar is not LL1!\n");
            return;
        }
        sb.append(Render.LL1_Table_Render(ll1, 24));
        String ret = sb.toString();
        System.out.print(ret);
        filesystem.FileIO.writeFile(ret, output_path);
    }
}
