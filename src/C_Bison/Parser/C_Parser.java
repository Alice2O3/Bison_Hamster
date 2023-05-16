package C_Bison.Parser;

import C_Bison.Grammar.LL1.Process;
import C_Bison.Grammar.LL1.Types.*;
import C_Bison.Grammar.Types.*;
import C_Bison.Language.Easy_C.Tokens;
import C_Bison.Language.Rules.Lexing.Workflow;
import C_Flex.Types.*;
import Filesystem.FileIO;
import C_Bison.Grammar.LL1.Render;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

public class C_Parser implements IParser {
    private final static String grammar_file = "Grammars/Easy_C.bison";
    private final static String grammar_output = "Grammars/Easy_C_LL1_Table.txt";
    public void Parse_Tokens(DFA_lexing_list token_list) {
        Workflow rule_lexing_workflow = new Workflow();
        Workflow.Process_Exception e1 = rule_lexing_workflow.process_text(FileIO.readFile(grammar_file));
        if (e1 != Workflow.Process_Exception.NORMAL) {
            System.out.print("\nInput Error!\n");
            return;
        }
        Grammar_list V = rule_lexing_workflow.getGrammarList();
        Process ll1 = new Process();
        if(ll1.pre_process(V) != Pre_Process_Exception.NORMAL){
            System.out.print("\nInput Error!\n");
            return;
        }
        Resolve_Exception e2 = ll1.resolve_LL1_table();
        if(e2 == Resolve_Exception.HAS_LEFT_RECURSION){
            System.out.print("\nGrammar has left recursion!\n");
            return;
        }
        else if(e2 == Resolve_Exception.NOT_LL1){
            System.out.print("\nGrammar is not LL1!\n");
            return;
        }
        String LL1_output = Render.LL1_Table_Render(ll1, 48);
        FileIO.writeFile(LL1_output, grammar_output);

        Map<String, Integer> Terminal_Map = rule_lexing_workflow.getTerminal_Map();
        LL1_table LL1_Table = ll1.get_LL1_table();
        LL1_expr_pair_list expr_pair_index = ll1.get_expr_pair_index();

        System.out.print(rule_lexing_workflow.getSymbolInfo());

        Integer start_symbol = ll1.get_start_symbol();
        System.out.printf("Start Symbol: %d\n", start_symbol);

        System.out.print("\nToken Flow:\n");
        for(DFA_lexing lexing : token_list.val){
            System.out.printf("<%d>", Terminal_Map.get(Tokens.Map[lexing.token_type]));
        }
        System.out.print("\n");
        System.out.print("\nParsing:\n");

        Stack<Integer> S = new Stack<>();
        S.push(start_symbol);

        Iterator<DFA_lexing> it = token_list.val.iterator();
        DFA_lexing lexing_top = it.next();

        Boolean success_flag = false;
        //Process
        while(true){
            Integer mapped_token = Terminal_Map.get(Tokens.Map[lexing_top.token_type]);
            System.out.printf("Processing: <%d>\n", mapped_token);
            System.out.printf("Stack State: %s\n", S);

            Integer S_top = S.peek();
            System.out.printf("Stack Top: %d\n", S_top);

            if(S_top.equals(mapped_token)){ //Matched
                System.out.print("Token Matched\n");
                S.pop();
                if(!it.hasNext()){
                    break;
                }
                lexing_top = it.next();
            } else { //Not matched
                if(!LL1_Table.val.containsKey(S_top)){
                    System.out.print("Token not in LL1 table!\n");
                    break;
                }
                LL1_symbol_index symbol_index = LL1_Table.val.get(S_top);
                if(symbol_index.val.containsKey(mapped_token)){
                    LL1_expr_pair expr_pair = expr_pair_index.val.get(symbol_index.val.get(mapped_token));
                    Grammar_expr expr = expr_pair.second;
                    System.out.printf("Replace using rule %s\n", Render.expr_pair_to_str(expr_pair));
                    S.pop();
                    ListIterator<Grammar_pair> expr_it = expr.val.listIterator(expr.val.size());
                    while(expr_it.hasPrevious()){
                        Grammar_pair p = expr_it.previous();
                        S.push(p.second);
                    }
                }
                else{
                    System.out.print("Token not in LL1 table!\n");
                    break;
                }
            }

            System.out.print("\n");
        }
        if(S.empty()){
            System.out.print("\nLL1 parse successful!\n");
        }
    }
}
