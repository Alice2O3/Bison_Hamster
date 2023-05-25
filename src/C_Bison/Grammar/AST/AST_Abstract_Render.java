package C_Bison.Grammar.AST;

import C_Bison.Grammar.AST.AST_Types.*;

import java.util.Iterator;
import java.util.Stack;

public class AST_Abstract_Render {
    public static String convert_to_json(IAST_Node root){
        StringBuilder sb = new StringBuilder();
        Stack<AST_stack_info> S = new Stack<>();
        S.push(new AST_stack_info(root, 1, false, true));
        S.push(new AST_stack_info(root, 1, true, true));
        sb.append("{\n");
        while(!S.empty()){
            AST_stack_info S_top = S.peek();
            IAST_Node ast_node = S_top.node;
            IAST_Printable ast_node_printable = (IAST_Printable) ast_node;
            Integer layer = S_top.layer;
            S.pop();
            if(S_top.state){
                sb.append(ast_node_printable.printInfoStart(layer));
                Iterator<IAST_Node> it = ast_node.getNode_list().iterator();
                if(it.hasNext()){
                    IAST_Node node = it.next();
                    S.push(new AST_stack_info(node, layer + 2, false, true));
                    S.push(new AST_stack_info(node, layer + 2, true, true));
                }
                while(it.hasNext()){
                    IAST_Node node = it.next();
                    S.push(new AST_stack_info(node, layer + 2, false, false));
                    S.push(new AST_stack_info(node, layer + 2, true, false));
                }
            }
            else {
                sb.append(ast_node_printable.printInfoEnd(layer, S_top.list_end));
            }
        }
        sb.append("}\n");
        return sb.toString();
    }
}
