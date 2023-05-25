package C_Bison.Grammar.AST;

import java.util.ArrayList;
import java.util.List;

public class AST_Node implements IAST_Node {
    private final List<IAST_Node> node_list = new ArrayList<>();

    @Override
    public void addChild(IAST_Node e) {
        node_list.add(e);
    }

    @Override
    public List<IAST_Node> getNode_list() {
        return node_list;
    }

    @Override
    public String getSymbol(){
        return null;
    }
}
