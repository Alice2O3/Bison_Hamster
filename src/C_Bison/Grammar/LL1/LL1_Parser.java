package C_Bison.Grammar.LL1;

import C_Bison.Grammar.AST;
import C_Bison.Grammar.AST_Types.*;
import C_Bison.Grammar.Grammar_Types.*;
import C_Bison.Language.Rules.Lexing.Rules_Workflow;
import C_Flex.DFA_Types.*;
import C_Bison.Grammar.LL1.LL1_Types.*;

import java.util.*;

public class LL1_Parser {
    private AST ast;
    private LL1_table LL1_Table;
    private LL1_expr_pair_list expr_pair_index;
    private Map<String, Integer> Terminal_Map;
    private List<String> Symbol_List;
    private Integer start_symbol;
    private String[] Token_Map;
    private final Stack<AST_node_pair> Token_Stack = new Stack<>();

    private Boolean Push_Expr(AST_node_pair S_top, Integer mapped_token){
        AST_node current_node = S_top.first;
        Integer non_terminal_index = S_top.second;
        if(!LL1_Table.val.containsKey(non_terminal_index)){
            //System.out.print("Token not in LL1 table!\n");
            return false;
        }
        LL1_symbol_index symbol_index = LL1_Table.val.get(non_terminal_index);
        if(!symbol_index.val.containsKey(mapped_token)){
            //System.out.print("Token not in LL1 table!\n");
            return false;
        }
        LL1_expr_pair expr_pair = expr_pair_index.val.get(symbol_index.val.get(mapped_token));
        Grammar_expr expr = expr_pair.second;
        //System.out.printf("Replace using rule %s\n", LL1_Render.expr_pair_to_str(expr_pair));
        Token_Stack.pop();
        ListIterator<Grammar_pair> expr_it = expr.val.listIterator(expr.val.size());
        while(expr_it.hasPrevious()){
            Grammar_pair p = expr_it.previous();
            AST_node new_node = AST_node_factory.Factory_Non_Terminal(p.second);
            current_node.addChild(new_node);
            Token_Stack.push(new AST_node_pair(new_node, p.second));
        }
        return true;
    }

    private Parser_Exception Process_Tokens(DFA_lexing_list token_list){
        ast = new AST();
        AST_node root_node = AST_node_factory.Factory_Non_Terminal(start_symbol);
        ast.setRoot(root_node);
        ast.setSymbols(Symbol_List);

        Token_Stack.clear();
        Token_Stack.push(new AST_node_pair(root_node, start_symbol));

        Iterator<DFA_lexing> it = token_list.val.iterator();
        DFA_lexing lexing_top = it.next();
        //Process
        while(true){
            Integer mapped_token = Terminal_Map.get(Token_Map[lexing_top.token_type]);
            AST_node_pair S_top = Token_Stack.peek();
            //System.out.printf("Processing: <%d>\nStack State: %s\nStack Top: %d\n", mapped_token, Token_Stack, S_top.second);
            if(S_top.second.equals(mapped_token)){ //Matched
                //System.out.print("Token Matched\n\n");
                S_top.first.addChild(AST_node_factory.Factory_Terminal(lexing_top));
                Token_Stack.pop();
                if(!it.hasNext()){
                    break;
                }
                lexing_top = it.next();
            } else { //Not matched
                //System.out.print("Token Not Matched\n");
                if(!Push_Expr(S_top, mapped_token)){
                    //System.out.print("\n");
                    return Parser_Exception.PARSE_ERROR;
                }
                //System.out.print("\n");
            }
        }
        //Process String End (#)
        while(!Token_Stack.empty()){
            AST_node_pair S_top = Token_Stack.peek();
            //System.out.printf("Processing: #\nStack State: %s\nStack Top: %d\n", Token_Stack, S_top.second);
            if(!Push_Expr(S_top, LL1_Process.End_Tag)){
                //System.out.print("\n");
                return Parser_Exception.PARSE_ERROR;
            }
            //System.out.print("\n");
        }

        //System.out.print("LL1 parse successful!\n");

        return Parser_Exception.NORMAL;
    }

    public Parser_Exception Parse_Tokens(String rule_table, DFA_lexing_list token_list, String[] token_map){
        if(rule_table == null || token_list == null || token_map == null){
            return Parser_Exception.READ_ERROR;
        }
        Rules_Workflow rules_workflow = new Rules_Workflow();
        Rules_Workflow.Process_Exception e1 = rules_workflow.process_text(rule_table);
        if (e1 != Rules_Workflow.Process_Exception.NORMAL) {
            return Parser_Exception.READ_ERROR;
        }

        Grammar_list V = rules_workflow.getGrammarList();
        LL1_Process ll1 = new LL1_Process();
        if(ll1.pre_process(V) != Pre_Process_Exception.NORMAL){
            return Parser_Exception.INPUT_ERROR;
        }
        Resolve_Exception e2 = ll1.resolve_LL1_table();
        if(e2 == Resolve_Exception.HAS_LEFT_RECURSION){
            return Parser_Exception.HAS_LEFT_RECURSION;
        }
        else if(e2 == Resolve_Exception.NOT_LL1){
            return Parser_Exception.NOT_LL1;
        }

        /*
        System.out.print(rules_workflow.getSymbolInfo());
        System.out.print("\n");
        System.out.print(LL1_Render.LL1_Table_Render(ll1,48));
        System.out.print("\n");
        */

        Terminal_Map = rules_workflow.getTerminalMap();
        Symbol_List = rules_workflow.getSymbolList();
        LL1_Table = ll1.get_LL1_table();
        expr_pair_index = ll1.get_expr_pair_index();
        start_symbol = ll1.get_start_symbol();
        Token_Map = token_map;

        return Process_Tokens(token_list);
    }

    public AST get_AST(){
        return ast;
    }
}
