package Debug;

import Filesystem.FileIO;
import C_Bison.Language.Rules.Lexing.Workflow;
import C_Bison.Language.Rules.Lexing.Render;

public class LL1_Scanner_Debug {
    static String input_path = "Compile_Test/LL1_scanner_test/Easy_C.bison";
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
        sb.append(Render.Grammar_List_to_str(workflow.getGrammarList()));
        String ret = sb.toString();
        System.out.print(ret);
        FileIO.writeFile(ret, output_path);
    }
}
