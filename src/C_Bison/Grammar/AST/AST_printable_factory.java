package C_Bison.Grammar.AST;

import C_Flex.DFA_Types.*;

public class AST_printable_factory {
    public static IAST_node Factory_Finished(DFA_lexing lexing){
        return new AST_printable_abstract.AST_node_abstract_finished(lexing);
    }

    public static IAST_node Factory_Non_Finished(String symbol){
        return new AST_printable_abstract.AST_node_abstract_non_finished(symbol);
    }
}
