package C_Bison.Language.Easy_C.Lexing;

import C_Bison.Scanner.C_Scanner;
import C_Flex.Types.*;

import java.util.List;

public class Workflow {
    private DFA_lexing_list token_info;
    public void process_code(String code){
        token_info = new C_Scanner().scan(code);
    }

    public DFA_lexing_list getTokenInfo(){
        return token_info;
    }
}
