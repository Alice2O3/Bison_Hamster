package C_Flex;

public class DFA_lexing {
    public Integer l_index_global;
    public Integer r_index_global;
    public Integer line_index;
    public Integer row_index;
    public String token;
    public Integer token_type;
    public DFA_lexing(int l_index_global_, int r_index_global_, int line_index_, int row_index_, String token_, Integer token_type_){
        l_index_global = l_index_global_;
        r_index_global = r_index_global_;
        line_index = line_index_;
        row_index = row_index_;
        token = token_;
        token_type = token_type_;
    }
}
