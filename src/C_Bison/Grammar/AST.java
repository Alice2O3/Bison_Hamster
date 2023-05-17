package C_Bison.Grammar;

import C_Bison.Grammar.AST_Types.*;

import java.util.List;
import java.util.Stack;

public class AST {
    private AST_node root;
    private List<String> Symbol_List;

    public void Traverse(){
        Stack<AST_node> S = new Stack<>();
        S.push(root);
        while(!S.empty()){
            AST_node S_top = S.peek();
            if(S_top.isTerminal){
                System.out.printf("\"%s\"\n", S_top.lexing.token);
            } else {
                System.out.printf("%s\n", Symbol_List.get(S_top.symbol_index));
            }
            S.pop();
            for(AST_node e : S_top.node_list){
                S.push(e);
            }
        }
    }

    public void setRoot(AST_node e){
        root = e;
    }

    public void setSymbols(List<String> symbol_list) {
        Symbol_List = symbol_list;
    }
}
