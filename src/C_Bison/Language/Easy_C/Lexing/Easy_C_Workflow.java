package C_Bison.Language.Easy_C.Lexing;

import C_Bison.Scanner.C_Scanner;
import C_Flex.DFA_Types.*;

public class Easy_C_Workflow {
    private DFA_lexing_list token_list;
    public void process_code(String code){
        token_list = new C_Scanner().scan(code);
    }

    public DFA_lexing_list getTokenList(){
        return token_list;
    }
}
