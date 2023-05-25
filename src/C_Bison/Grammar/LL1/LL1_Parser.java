package C_Bison.Grammar.LL1;

import C_Bison.Grammar.AST.AST_Printable_Factory;
import C_Bison.Grammar.AST.AST_Types.*;
import C_Bison.Grammar.AST.IAST_Node;
import C_Bison.Grammar.Grammar_Types.*;
import C_Bison.Language.Rules.Lexing.Rules_Workflow;
import C_Flex.DFA_Types.*;
import C_Bison.Grammar.LL1.LL1_Types.*;

import java.util.*;

public class LL1_Parser {
    private LL1_table LL1_Table;
    private LL1_expr_pair_list expr_pair_index;
    private Map<String, Integer> Symbol_Map;
    private List<String> Symbol_List;
    private Integer start_symbol;
    private List<String> Token_Map;
    private final Stack<AST_node_pair> Token_Stack = new Stack<>();
    private Parser_Exception parser_exception;

    private Boolean Push_Expr(AST_node_pair S_top, Integer mapped_token){
        IAST_Node current_node = S_top.first;
        Integer non_terminal_index = S_top.second;
        if(!LL1_Table.val.containsKey(non_terminal_index)){
            return false;
        }
        LL1_symbol_index symbol_index = LL1_Table.val.get(non_terminal_index);
        if(!symbol_index.val.containsKey(mapped_token)){
            return false;
        }
        LL1_expr_pair expr_pair = expr_pair_index.val.get(symbol_index.val.get(mapped_token));
        Grammar_expr expr = expr_pair.second;
        Token_Stack.pop();
        ListIterator<Grammar_pair> expr_it = expr.val.listIterator(expr.val.size());
        while(expr_it.hasPrevious()){
            Grammar_pair p = expr_it.previous();
            IAST_Node new_node = AST_Printable_Factory.Factory_Non_Finished(Symbol_List.get(p.second));
            current_node.addChild(new_node);
            Token_Stack.push(new AST_node_pair(new_node, p.second));
        }
        return true;
    }

    public IAST_Node Parse_Tokens(DFA_lexing_list token_list){
        IAST_Node root_node = AST_Printable_Factory.Factory_Non_Finished(Symbol_List.get(start_symbol));
        Token_Stack.clear();
        Token_Stack.push(new AST_node_pair(root_node, start_symbol));

        Iterator<DFA_lexing> it = token_list.val.iterator();
        DFA_lexing lexing_top = it.next();
        //Process
        while(true){
            Integer mapped_token = Symbol_Map.get(Token_Map.get(lexing_top.token_type));
            AST_node_pair S_top = Token_Stack.peek();
            if(S_top.second.equals(mapped_token)){ //Matched
                S_top.first.addChild(AST_Printable_Factory.Factory_Finished(lexing_top));
                Token_Stack.pop();
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
        while(!Token_Stack.empty()){
            AST_node_pair S_top = Token_Stack.peek();
            if(!Push_Expr(S_top, LL1_Process.End_Tag)){
                parser_exception = Parser_Exception.PARSE_ERROR;
                return null;
            }
        }
        parser_exception = Parser_Exception.NORMAL;
        return root_node;
    }

    public void Pre_Process(String rule_table, List<String> token_map){
        if(rule_table == null || token_map == null){
            parser_exception = Parser_Exception.READ_ERROR;
            return;
        }
        Rules_Workflow rules_workflow = new Rules_Workflow();
        Rules_Workflow.Process_Exception e1 = rules_workflow.process_text(rule_table);
        if (e1 != Rules_Workflow.Process_Exception.NORMAL) {
            parser_exception = Parser_Exception.READ_ERROR;
            return;
        }
        Grammar_list V = rules_workflow.getGrammarList();
        LL1_Process ll1 = new LL1_Process();
        if(ll1.pre_process(V) != Pre_Process_Exception.NORMAL){
            parser_exception = Parser_Exception.INPUT_ERROR;
            return;
        }
        Resolve_Exception e2 = ll1.resolve_LL1_table();
        if(e2 == Resolve_Exception.HAS_LEFT_RECURSION){
            parser_exception = Parser_Exception.HAS_LEFT_RECURSION;
            return;
        }
        else if(e2 == Resolve_Exception.NOT_LL1){
            parser_exception = Parser_Exception.NOT_LL1;
            return;
        }
        Symbol_Map = rules_workflow.getSymbolMap();
        Symbol_List = rules_workflow.getSymbolList();
        LL1_Table = ll1.get_LL1_table();
        expr_pair_index = ll1.get_expr_pair_index();
        start_symbol = ll1.get_start_symbol();
        Token_Map = token_map;
        parser_exception = Parser_Exception.NORMAL;
    }

    public Parser_Exception get_Exception(){
        return parser_exception;
    }
}
