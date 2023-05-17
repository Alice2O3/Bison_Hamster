package C_Bison.Grammar;

import C_Bison.Grammar.AST_Types.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class AST {
    private static String tab_space = "\t";
    private AST_node root;
    private List<String> Symbol_List;
    private Map<String, Integer> Terminal_Map;

    private String tab_n(Integer n){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<n;i++){
            sb.append(tab_space);
        }
        return sb.toString();
    }

    public String convert_to_json(){
        StringBuilder sb = new StringBuilder();
        Stack<AST_stack_info> S = new Stack<>();
        S.push(new AST_stack_info(root, 1, false, true));
        S.push(new AST_stack_info(root, 1, true, true));
        sb.append("{\n");
        while(!S.empty()){
            AST_stack_info S_top = S.peek();
            AST_node ast_node = S_top.node;
            Integer layer = S_top.layer;
            S.pop();
            if(S_top.state){
                sb.append(tab_n(layer));
                if(ast_node.isFinished){
                    sb.append("\"value\": \"")
                            .append(ast_node.lexing.token)
                            .append("\"\n");
                } else {
                    sb.append("\"type\": \"")
                            .append(Symbol_List.get(ast_node.symbol_index))
                            .append("\",\n")
                            .append(tab_n(layer))
                            .append("\"children\":\n")
                            .append(tab_n(layer))
                            .append("[\n")
                            .append(tab_n(layer + 1))
                            .append("{\n");
                }
                Iterator<AST_node> it = ast_node.node_list.iterator();
                if(it.hasNext()){
                    AST_node node = it.next();
                    S.push(new AST_stack_info(node, layer + 2, false, true));
                    S.push(new AST_stack_info(node, layer + 2, true, true));
                }
                while(it.hasNext()){
                    AST_node node = it.next();
                    S.push(new AST_stack_info(node, layer + 2, false, false));
                    S.push(new AST_stack_info(node, layer + 2, true, false));
                }
            }
            else {
                if(!ast_node.isFinished){
                    sb.append(tab_n(layer + 1))
                            .append("}\n")
                            .append(tab_n(layer))
                            .append("]");
                    if(!S_top.list_end){
                        sb.append(",");
                    }
                    sb.append("\n");
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    public void setRoot(AST_node e){
        root = e;
    }

    public void setSymbols(Map<String, Integer> terminal_map, List<String> symbol_list) {
        Terminal_Map = terminal_map;
        Symbol_List = symbol_list;
    }
}
