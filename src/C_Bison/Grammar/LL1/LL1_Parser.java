package C_Bison.Grammar.LL1;

import C_Bison.Grammar.AST;
import C_Bison.Grammar.Grammar_Types.*;
import C_Bison.Language.Rules.Lexing.Rules_Workflow;
import C_Flex.DFA_Types.*;
import C_Bison.Grammar.LL1.LL1_Types.*;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

public class LL1_Parser {
    private AST ast = new AST();
    private LL1_table LL1_Table;
    private LL1_expr_pair_list expr_pair_index;
    private Map<String, Integer> Terminal_Map;
    private Integer start_symbol;
    private String[] Token_Map;
    private Stack<Integer> Token_Stack = new Stack<>();

    private void Push_Expr(LL1_symbol_index symbol_index, Integer mapped_token){
        LL1_expr_pair expr_pair = expr_pair_index.val.get(symbol_index.val.get(mapped_token));
        Grammar_expr expr = expr_pair.second;
        System.out.printf("Replace using rule %s\n", LL1_Render.expr_pair_to_str(expr_pair));
        Token_Stack.pop();
        ListIterator<Grammar_pair> expr_it = expr.val.listIterator(expr.val.size());
        while(expr_it.hasPrevious()){
            Grammar_pair p = expr_it.previous();
            Token_Stack.push(p.second);
        }
    }

    private Parser_Exception Process_Tokens(DFA_lexing_list token_list){
        Token_Stack.clear();
        Token_Stack.push(start_symbol);

        Iterator<DFA_lexing> it = token_list.val.iterator();
        DFA_lexing lexing_top = it.next();
        //Process
        while(true){
            Integer mapped_token = Terminal_Map.get(Token_Map[lexing_top.token_type]);
            System.out.printf("Processing: <%d>\n", mapped_token);
            System.out.printf("Stack State: %s\n", Token_Stack);

            Integer S_top = Token_Stack.peek();
            System.out.printf("Stack Top: %d\n", S_top);

            if(S_top.equals(mapped_token)){ //Matched
                System.out.print("Token Matched\n");
                Token_Stack.pop();
                if(!it.hasNext()){
                    break;
                }
                lexing_top = it.next();
            } else { //Not matched
                if(!LL1_Table.val.containsKey(S_top)){
                    System.out.print("Token not in LL1 table!\n");
                    return Parser_Exception.PARSE_ERROR;
                }
                LL1_symbol_index symbol_index = LL1_Table.val.get(S_top);
                if(!symbol_index.val.containsKey(mapped_token)){
                    System.out.print("Token not in LL1 table!\n");
                    return Parser_Exception.PARSE_ERROR;
                }
                Push_Expr(symbol_index, mapped_token);
            }
            System.out.print("\n");
        }

        if(Token_Stack.empty()){
            System.out.print("\nLL1 parse successful!\n");
            return Parser_Exception.NORMAL;
        } else {
            System.out.print("\nStack is not empty!\n");
            return Parser_Exception.PARSE_ERROR;
        }
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

        Terminal_Map = rules_workflow.getTerminal_Map();
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
