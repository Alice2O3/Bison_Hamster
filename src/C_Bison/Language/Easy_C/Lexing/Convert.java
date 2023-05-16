package C_Bison.Language.Easy_C.Lexing;

import C_Bison.Language.Easy_C.Tokens;
import C_Flex.Types.*;

import java.util.List;

public class Convert {
    public static String Convert_Tokens_to_str(List<DFA_lexing> Token_pair_list){
        StringBuilder sb = new StringBuilder();
        /*
            ["int", Int]
            ["main", Identifier]
            ["(", Left_small_bracket]
            [")", Right_small_bracket]
            ["{", Left_big_bracket]
            ["int", Int]
            ["a", Identifier]
            [",", Comma]
            ["b", Identifier]
            [",", Comma]
            ["c", Identifier]
            [";", Statement_end]
            ["a", Identifier]
            ["=", Assign]
            ["123", Integer]
            [";", Statement_end]
            ["b", Identifier]
            ["=", Assign]
            ["456", Integer]
            [";", Statement_end]
            ["c", Identifier]
            ["=", Assign]
            ["a", Identifier]
            ["+", Plus]
            ["b", Identifier]
            [";", Statement_end]
            ["return", Return]
            ["0", Integer]
            [";", Statement_end]
            ["}", Right_big_bracket]
            ["EOF", EOF]
        */
        for(DFA_lexing lexing : Token_pair_list) {
            sb.append("[").append("\"").append(lexing.token).append("\"").append(", ").append(Tokens.Map[lexing.token_type]).append("]\n");
        }
        return sb.toString();
    }
}
