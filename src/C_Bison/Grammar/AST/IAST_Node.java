package C_Bison.Grammar.AST;

import java.util.List;

public interface IAST_Node {
    void addChild(IAST_Node e);
    List<IAST_Node> getNode_list();
    String getSymbol();
}
