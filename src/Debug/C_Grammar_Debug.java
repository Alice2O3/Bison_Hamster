package Debug;

import C_Bison.Language.Easy_C.Lexing.Workflow;
import C_Bison.PreProcess.PreProcess_C;
import C_Flex.Types.*;
import Filesystem.FileIO;

import java.util.List;

public class C_Grammar_Debug {
    private final static String grammar_file = "Compile_Test/C_grammar_test/Easy_C.bison";
    private final static String input_file = "Compile_Test/C_grammar_test/Example.c";
    private final static String preprocessed_file = "Compile_Test/C_grammar_test/Example.pp.c";
    private final static String output_file = "Compile_Test/C_grammar_test/token_flow.txt";
    public static void main(String[] args) {
        //Preprocessing code
        String source_code = FileIO.readFile(input_file);
        if(source_code == null){
            System.out.print("File Read Error!\n");
            return;
        }
        PreProcess_C preprocess = new PreProcess_C();
        String formatted_code = preprocess.process(source_code);
        FileIO.writeFile(formatted_code, preprocessed_file);
        //Process Tokens
        String source_code_2 = FileIO.readFile(preprocessed_file);
        if(source_code_2 == null){
            System.out.print("File Read Error!\n");
            return;
        }
        Workflow workflow = new Workflow();
        workflow.process_code(source_code_2);
        List<DFA_lexing> token_info = workflow.getTokenInfo();
        
    }
}
