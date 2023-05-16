package Debug;

import C_Bison.Language.Easy_C.Lexing.Render;
import C_Flex.Types.*;
import Filesystem.FileIO;
import C_Bison.PreProcess.PreProcess_C;
import C_Bison.Language.Easy_C.Lexing.Workflow;

public class C_Token_Debug {
    private final static String input_file = "Compile_Test/C_token_test/Example.c";
    private final static String preprocessed_file = "Compile_Test/C_token_test/Example.pp.c";
    private final static String output_file = "Compile_Test/C_token_test/token_flow.txt";
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
        DFA_lexing_list token_info = workflow.getTokenInfo();
        String tokens_output = Render.Convert_Tokens_to_str(token_info);
        System.out.print(tokens_output);
        FileIO.writeFile(tokens_output, output_file);
    }
}
