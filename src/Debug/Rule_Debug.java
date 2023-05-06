package Debug;

import LL1.Simple_Read.IO;
import LL1.Types.*;

public class Rule_Debug {
    public static void main(String[] args){
        String test_rule = "S->ABC|D|E";
        LL1_rule p = IO.str_to_vec(test_rule);
        System.out.println(IO.vec_to_str(p));
    }
}
