package Debug;

import C_Bison.Grammar.AST;
import C_Bison.Parser.C_Parser;
import C_Bison.PreProcess.PreProcess_C;
import Filesystem.FileIO;

public class C_Parser_Debug {
    private final static String input_file = "Compile_Test/C_grammar_test/Example.c";
    private final static String preprocessed_file = "Compile_Test/C_grammar_test/Example.pp.c";
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

        C_Parser parser = new C_Parser();
        AST ast = parser.Parse_Tokens(source_code_2);
        if(ast == null){
            System.out.print("AST Parsing Error!\n");
            return;
        }
    }
}
