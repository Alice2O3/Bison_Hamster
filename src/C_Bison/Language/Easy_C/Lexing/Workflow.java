package C_Bison.Language.Easy_C.Lexing;

import C_Bison.Scanner.C_Scanner;
import C_Flex.Types.*;

public class Workflow {
    private DFA_lexing_list token_list;
    public void process_code(String code){
        token_list = new C_Scanner().scan(code);
    }

    public DFA_lexing_list getTokenList(){
        return token_list;
    }
}
