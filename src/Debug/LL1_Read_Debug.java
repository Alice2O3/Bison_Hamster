package Debug;

import Grammar.Types.*;
import Grammar.Simple_Read.Workflow;
import Filesystem.FileIO;

public class LL1_Read_Debug {
    private final static String input_file = "Compile_Test/LL1_read_test/LL1_read_test.txt";
    private final static String output_file = "Compile_Test/LL1_read_test/output.txt";
    public static void main(String[] args){
        String code = FileIO.readFile(input_file);
        if(code == null){
            System.out.print("File Read Error!\n");
            return;
        }
        Grammar_list V = Workflow.str_to_grammar(code);
        if(V == null){
            System.out.print("Input Error!\n");
            return;
        }
        String ret = Workflow.grammar_to_str(V);
        System.out.print(ret);
        FileIO.writeFile(ret, output_file);
    }
}
