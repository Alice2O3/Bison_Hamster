package Debug;

import Util.FileIO;
import C_Bison.Language.Rules.Lexing.Rules_Workflow;
import C_Bison.Language.Rules.Lexing.Rules_Render;

public class LL1_Scanner_Debug {
    static String input_path = "Compile_Test/LL1_scanner_test/Example.bison";
    static String output_path = "Compile_Test/LL1_scanner_test/Scanner.txt";

    public static void main(String[] args) {
        String code = FileIO.readFile(input_path);
        //String code = filesystem.FileIO.readFile(input_path);
        if(code == null){
            System.out.print("File Read Error!\n");
            return;
        }
        Rules_Workflow workflow = new Rules_Workflow();
        Rules_Workflow.Process_Exception e = workflow.process_text(code);
        if(e != Rules_Workflow.Process_Exception.NORMAL){
            System.out.print("Input Error!\n");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(code).append("\n");
        sb.append(Rules_Render.get_Symbol_Info(workflow)).append("\n");
        sb.append(Rules_Render.Grammar_List_to_str(workflow.getGrammar_list()));
        String ret = sb.toString();
        System.out.print(ret);
        FileIO.writeFile(ret, output_path);
    }
}
