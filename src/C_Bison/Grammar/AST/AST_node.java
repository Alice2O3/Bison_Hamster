package C_Bison.Grammar.AST;

import java.util.ArrayList;
import java.util.List;

public class AST_node implements IAST_node{
    private final List<IAST_node> node_list = new ArrayList<>();

    @Override
    public void addChild(IAST_node e) {
        node_list.add(e);
    }

    @Override
    public List<IAST_node> getNode_list() {
        return node_list;
    }

    @Override
    public String getSymbol(){
        return null;
    }
}
