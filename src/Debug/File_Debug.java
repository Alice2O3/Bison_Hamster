package Debug;

import LL1.Simple_Read.FileIO;
import LL1.Types.*;
import java.io.File;

public class File_Debug {
    public static void main(String[] args){
        String input_file = "Compile_Test/LL1_read_test/LL1_read_test.txt";
        String output_file = "Compile_Test/LL1_read_test/output.txt";
        LL1_list V = FileIO.read_from_file(new File(input_file));
        FileIO.write_to_file(new File(output_file), V);
    }
}
