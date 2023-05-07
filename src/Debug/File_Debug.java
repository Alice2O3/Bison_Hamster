package Debug;

import Grammar.Simple_Read.FileIO;
import Grammar.Types.*;
import java.io.File;

public class File_Debug {
    public static void main(String[] args){
        String input_file = "Compile_Test/LL1_read_test/LL1_read_test.txt";
        String output_file = "Compile_Test/LL1_read_test/output.txt";
        Grammar_list V = FileIO.read_from_file(new File(input_file));
        FileIO.write_to_file(new File(output_file), V);
    }
}
