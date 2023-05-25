package C_Bison.Grammar.LL1;

import C_Bison.Grammar.AST.AST_Abstract_Factory;
import C_Bison.Grammar.AST.AST_Types.*;
import C_Bison.Grammar.AST.IAST_Node;
import C_Bison.Grammar.Grammar_Types.*;
import C_Bison.Language.Rules.Lexing.Rules_Workflow;
import C_Flex.DFA_Types.*;
import C_Bison.Grammar.LL1.LL1_Types.*;

import java.util.*;

public class LL1_Parser {
    private List<String> token_map;
    private final Stack<AST_node_pair> token_stack = new Stack<>();
    private final Rules_Workflow rules_workflow = new Rules_Workflow();
    private final LL1_Process ll1_process = new LL1_Process();
    private Parser_Exception parser_exception;

    private Boolean Push_Expr(AST_node_pair S_top, Integer mapped_token){
        LL1_Table LL1_table = ll1_process.get_LL1_table();
        LL1_Expr_Pair_List expr_pair_index = ll1_process.get_expr_pair_index();

        IAST_Node current_node = S_top.first;
        Integer non_terminal_index = S_top.second;
        if(!LL1_table.val.containsKey(non_terminal_index)){
            return false;
        }
        LL1_Symbol_Index symbol_index = LL1_table.val.get(non_terminal_index);
        if(!symbol_index.val.containsKey(mapped_token)){
            return false;
        }
        LL1_Expr_Pair expr_pair = expr_pair_index.val.get(symbol_index.val.get(mapped_token));
        Grammar_expr expr = expr_pair.second;
        token_stack.pop();
        ListIterator<Grammar_pair> expr_it = expr.val.listIterator(expr.val.size());
        while(expr_it.hasPrevious()){
            Grammar_pair p = expr_it.previous();
            IAST_Node new_node = AST_Abstract_Factory.Factory_Non_Finished(rules_workflow.getSymbol_list().get(p.second));
            current_node.addChild(new_node);
            token_stack.push(new AST_node_pair(new_node, p.second));
        }
        return true;
    }

    public IAST_Node Parse_Tokens(DFA_lexing_list token_list){
        Integer start_symbol = ll1_process.get_start_symbol();

        IAST_Node root_node = AST_Abstract_Factory.Factory_Non_Finished(rules_workflow.getSymbol_list().get(start_symbol));
        token_stack.clear();
        token_stack.push(new AST_node_pair(root_node, start_symbol));
        Iterator<DFA_lexing> it = token_list.val.iterator();
        DFA_lexing lexing_top = it.next();
        //Process
        while(true){
            Integer mapped_token = rules_workflow.getSymbol_map().get(token_map.get(lexing_top.token_type));
            AST_node_pair S_top = token_stack.peek();
            if(S_top.second.equals(mapped_token)){ //Matched
                S_top.first.addChild(AST_Abstract_Factory.Factory_Finished(lexing_top));
                token_stack.pop();
                if(!it.hasNext()){
                    break;
                }
                lexing_top = it.next();
            } else { //Not matched
                if(!Push_Expr(S_top, mapped_token)){
                    parser_exception = Parser_Exception.PARSE_ERROR;
                    return null;
                }
            }
        }
        //Process String End (#)
        while(!token_stack.empty()){
            AST_node_pair S_top = token_stack.peek();
            if(!Push_Expr(S_top, LL1_Process.getEnd_Tag())){
                parser_exception = Parser_Exception.PARSE_ERROR;
                return null;
            }
        }
        parser_exception = Parser_Exception.NORMAL;
        return root_node;
    }

    public void Pre_Process(String rule_table, List<String> token_map_){
        if(rule_table == null || token_map_ == null){
            parser_exception = Parser_Exception.READ_ERROR;
            return;
        }
        Rules_Workflow.Process_Exception e1 = rules_workflow.process_text(rule_table);
        if (e1 != Rules_Workflow.Process_Exception.NORMAL) {
            parser_exception = Parser_Exception.READ_ERROR;
            return;
        }
        Grammar_list V = rules_workflow.getGrammar_list();
        if(ll1_process.pre_process(V) != Pre_Process_Exception.NORMAL){
            parser_exception = Parser_Exception.INPUT_ERROR;
            return;
        }
        Resolve_Exception e2 = ll1_process.resolve_LL1_table();
        if(e2 == Resolve_Exception.HAS_LEFT_RECURSION){
            parser_exception = Parser_Exception.HAS_LEFT_RECURSION;
            return;
        }
        else if(e2 == Resolve_Exception.NOT_LL1){
            parser_exception = Parser_Exception.NOT_LL1;
            return;
        }
        token_map = token_map_;
        parser_exception = Parser_Exception.NORMAL;
    }

    public Parser_Exception get_Exception(){
        return parser_exception;
    }

    public Rules_Workflow getRules_workflow(){
        return rules_workflow;
    }

    public LL1_Process getLl1_process(){
        return ll1_process;
    }
}
