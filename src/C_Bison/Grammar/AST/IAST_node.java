package C_Bison.Grammar.AST;

import java.util.List;

public interface IAST_node {
    void addChild(IAST_node e);
    List<IAST_node> getNode_list();
    String getSymbol();
}
