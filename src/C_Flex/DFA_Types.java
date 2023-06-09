package C_Flex;

import C_Flex.callback.ICallback;

import java.util.ArrayList;
import java.util.List;

public class DFA_Types {
    public static class DFA_event {
        private Character c;
        public final DFA_Node current_node;
        public final DFA_Node next_node;
        public final ICallback callback;
        public DFA_event(DFA_Node current_node_, DFA_Node next_node_, ICallback callback_){
            current_node = current_node_;
            next_node = next_node_;
            callback = callback_;
        }

        public Character getChar(){
            return c;
        }

        public void setChar(Character c_){
            c = c_;
        }
    }

    public static class DFA_lexing {
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

    public static class DFA_lexing_list{
        public List<DFA_lexing> val = new ArrayList<>();
    }
}
