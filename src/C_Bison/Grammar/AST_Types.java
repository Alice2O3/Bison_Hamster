package C_Bison.Grammar;

import C_Flex.DFA_Types;

import java.util.ArrayList;
import java.util.List;

public class AST_Types {
    public static class AST_node{
        public Boolean isTerminal;
        public Integer symbol_index; //Non terminal
        public DFA_Types.DFA_lexing lexing; //Is terminal
        public List<AST_node> node_list = new ArrayList<>();
        public void addChild(AST_node e){
            node_list.add(e);
        }
    }

    public static class AST_node_factory{
        public static AST_node Factory_Terminal(DFA_Types.DFA_lexing lexing){
            AST_node ret = new AST_node();
            ret.isTerminal = true;
            ret.lexing = lexing;
            return ret;
        }

        public static AST_node Factory_Non_Terminal(Integer symbol_index){
            AST_node ret = new AST_node();
            ret.isTerminal = false;
            ret.symbol_index = symbol_index;
            return ret;
        }
    }

    public static class AST_node_pair{
        public AST_node first;
        public Integer second;
        public AST_node_pair(AST_node first_, Integer second_){
            first = first_;
            second = second_;
        }
    }
}
