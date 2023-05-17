package Debug;

import C_Bison.Language.Easy_C.Lexing.Easy_C_Render;
import C_Flex.DFA_Types.*;
import Filesystem.FileIO;
import C_Bison.PreProcess.PreProcess_C;
import C_Bison.Language.Easy_C.Lexing.Easy_C_Workflow;

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
        Easy_C_Workflow workflow = new Easy_C_Workflow();
        workflow.process_code(source_code_2);
        DFA_lexing_list token_list = workflow.getTokenList();
        String token_output = Easy_C_Render.Convert_Tokens_to_str(token_list);
        System.out.print(token_output);
        FileIO.writeFile(token_output, output_file);
    }
}
