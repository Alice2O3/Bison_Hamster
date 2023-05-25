package C_Bison.Grammar.AST;

public class AST_Types {
    public static class AST_node_pair{
        public IAST_Node first;
        public Integer second;
        public AST_node_pair(IAST_Node first_, Integer second_){
            first = first_;
            second = second_;
        }
    }

    public static class AST_stack_info{
        public IAST_Node node;
        public Integer layer;
        public Boolean state; //0: out, 1: in
        public Boolean list_end;
        public AST_stack_info(IAST_Node node_, Integer layer_, Boolean state_, Boolean list_end_){
            node = node_;
            layer = layer_;
            state = state_;
            list_end = list_end_;
        }
    }
}
