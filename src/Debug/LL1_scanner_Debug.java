package Debug;

import Grammar.Process_Lexing.Workflow;
import Grammar.Process_Lexing.Convert;

public class LL1_scanner_Debug {
    static String input_path = "Compile_Test/LL1_scanner_test/Easy_C.txt";
    static String output_path = "Compile_Test/LL1_scanner_test/Easy_C_Scanner.txt";

    public static void main(String[] args) {
        String code = filesystem.FileIO.readFile(input_path);
        Workflow workflow = new Workflow();
        Workflow.Process_Exception e = workflow.process_text(code);
        if(e != Workflow.Process_Exception.NORMAL){
            System.out.print("\nInput Error!\n");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(code);
        sb.append("\n");
        sb.append(workflow.getSymbolInfo());
        sb.append("\n");
        sb.append(Convert.Grammar_List_to_str(workflow.getLL1List()));
        String ret = sb.toString();
        System.out.print(ret);
        filesystem.FileIO.writeFile(ret, output_path);
    }
}
