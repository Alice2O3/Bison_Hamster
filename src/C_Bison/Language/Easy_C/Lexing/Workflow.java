package C_Bison.Language.Easy_C.Lexing;

import C_Bison.Scanner.C_Scanner;
import C_Flex.Types.*;

import java.util.List;

public class Workflow {
    private List<DFA_lexing> token_info;
    public void process_code(String code){
        token_info = new C_Scanner().scan(code);
    }

    public List<DFA_lexing> getTokenInfo(){
        return token_info;
    }
}
