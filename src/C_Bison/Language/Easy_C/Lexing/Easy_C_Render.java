package C_Bison.Language.Easy_C.Lexing;

import C_Bison.Language.Easy_C.Easy_C_Tokens;
import C_Flex.DFA_Types.*;

public class Easy_C_Render {
    public static String Convert_Tokens_to_str(DFA_lexing_list tokens_list){
        StringBuilder sb = new StringBuilder();
        int token_index = 0;
        for(DFA_lexing l : tokens_list.val){
            sb.append(String.format("[@%d,%d:%d='%s',<%s>,%d:%d]\n", token_index, l.l_index_global, l.r_index_global, l.token, Easy_C_Tokens.Token_Map[l.token_type], l.line_index, l.row_index));
            token_index++;
        }
        return sb.toString();
    }
}
