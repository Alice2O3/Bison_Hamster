package Debug;

import Grammar.Simple_Read.IO;
import Grammar.Types.*;

public class Rule_Debug {
    public static void main(String[] args){
        String test_rule = "S->ABC|D|E";
        Grammar_rule p = IO.str_to_vec(test_rule);
        System.out.println(IO.vec_to_str(p));
    }
}
