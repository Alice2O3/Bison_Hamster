package Debug;

import Filesystem.FileIO;
import C_Bison.PreProcess.PreProcess_C;

public class C_Token_Debug {
    private final static String input_file = "Compile_Test/C_token_test/Example.c";
    private final static String preprocessed_file = "Compile_Test/C_token_test/Example.pp.c";
    private final static String output_file = "Compile_Test/C_token_test/tokens.txt";
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

    }
}
