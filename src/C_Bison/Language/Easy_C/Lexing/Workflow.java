package C_Bison.Language.Easy_C.Lexing;

import C_Bison.Language.Easy_C.Tokens;
import C_Bison.Scanner.C_Scanner;
import C_Flex.DFA_lexing;

import java.util.List;

public class Workflow {
    private String token_info = "";
    private void process_tokens(List<DFA_lexing> lexing_list){
        StringBuilder sb = new StringBuilder();
        /*
            ["0", Integer]
            ["int", Int]
        */
        for(DFA_lexing lexing : lexing_list) {
            sb.append("[").append("\"").append(lexing.token).append("\"").append(", ").append(Tokens.Map[lexing.token_type]).append("]\n");
        }
        token_info = sb.toString();
    }

    public void process_code(String code){
        process_tokens(new C_Scanner().scan(code));
    }

    public String getTokenInfo(){
        return token_info;
    }
}
