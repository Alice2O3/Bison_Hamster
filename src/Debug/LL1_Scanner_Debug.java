package Debug;

import Filesystem.FileIO;
import C_Bison.Language.Rules.Process_Lexing.Workflow;
import C_Bison.Language.Rules.Process_Lexing.Convert;

public class LL1_Scanner_Debug {
    static String input_path = "Compile_Test/LL1_scanner_test/Easy_C.txt";
    static String output_path = "Compile_Test/LL1_scanner_test/Easy_C_Scanner.txt";

    public static void main(String[] args) {
        String code = FileIO.readFile(input_path);
        //String code = filesystem.FileIO.readFile(input_path);
        if(code == null){
            System.out.print("File Read Error!\n");
            return;
        }
        Workflow workflow = new Workflow();
        Workflow.Process_Exception e = workflow.process_text(code);
        if(e != Workflow.Process_Exception.NORMAL){
            System.out.print("Input Error!\n");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(code).append("\n");
        sb.append(workflow.getSymbolInfo()).append("\n");
        sb.append(Convert.Grammar_List_to_str(workflow.getLL1List()));
        String ret = sb.toString();
        System.out.print(ret);
        FileIO.writeFile(ret, output_path);
    }
}
