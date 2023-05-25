package Debug;

import Util.FileIO;
import C_Bison.Language.Rules.Lexing.Rules_Render;
import C_Bison.Language.Rules.Lexing.Rules_Workflow;
import C_Bison.Grammar.LL1.LL1_Render;
import C_Bison.Grammar.LL1.LL1_Process;
import C_Bison.Grammar.LL1.LL1_Types.*;
import C_Bison.Grammar.Grammar_Types.*;

public class LL1_Analyze_Debug {
    private static String input_path = "Compile_Test/LL1_table_test/Example.bison";
    private static String output_path = "Compile_Test/LL1_table_test/Output.txt";

    public static void main(String[] args) {
        Rules_Workflow workflow = new Rules_Workflow();
        Rules_Workflow.Process_Exception e1 = workflow.process_text(FileIO.readFile(input_path));
        if(e1 != Rules_Workflow.Process_Exception.NORMAL){
            System.out.print("\nInput Error!\n");
            return;
        }
        Grammar_list V = workflow.getGrammar_list();
        StringBuilder sb = new StringBuilder();
        sb.append(Rules_Render.get_Symbol_Info(workflow));
        sb.append("\nRule_Table:\n");
        sb.append(Rules_Render.Grammar_List_to_str_full(V));
        sb.append("\n");
        LL1_Process ll1 = new LL1_Process();
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
        sb.append(LL1_Render.LL1_Table_Render(ll1, 24));
        String ret = sb.toString();
        System.out.print(ret);
        FileIO.writeFile(ret, output_path);
    }
}
