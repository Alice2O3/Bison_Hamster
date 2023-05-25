package C_Bison.Grammar.AST;

import C_Flex.DFA_Types.*;

public class AST_Printable_Factory {
    public static IAST_Node Factory_Finished(DFA_lexing lexing){
        return new AST_Printable_Abstract.AST_Node_Abstract_Finished(lexing);
    }

    public static IAST_Node Factory_Non_Finished(String symbol){
        return new AST_Printable_Abstract.AST_Node_Abstract_Non_Finished(symbol);
    }
}
