package C_Flex;

import java.util.ArrayList;
import java.util.List;
import C_Flex.DFA_Types.*;

public class DFA {
    private final List<DFA_node> node_list;
    private DFA_node current_state; //The default state is 0
    private DFA_node initial_state;
    private StringBuilder lexeme;

    public DFA_node fallback_node;
    public Integer l_index;
    public Integer r_index;
    public Integer l_index_global;
    public Integer r_index_global;
    public Integer line_index;
    public boolean keep;
    public DFA_lexing_list tokens_list;

    public DFA(){
        node_list = new ArrayList<>();
        tokens_list = new DFA_lexing_list();
        lexeme = new StringBuilder();
        resetState();
    }

    public void initSize(int n){ //Should be called first
        node_list.clear();
        for(int i = 0; i < n; i++){
            DFA_node node = new DFA_node();
            node.setIndex(i);
            node_list.add(node);
        }
        fallback_node = new DFA_node();
        fallback_node.setIndex(-1);
        setInitialState(0);
    }

    public void resetState(){
        clearLexeme();
        keep = false;
        l_index = 0;
        r_index = 0;
        l_index_global = 0;
        r_index_global = 0;
        line_index = 0;
        tokens_list.val.clear();
        gotoInitialState();
    }

    public void setInitialState(int index){
        initial_state = node_list.get(index);
    }

    public void gotoInitialState(){
        current_state = initial_state;
    }

    public void processString(String input_str){
        if(initial_state == null){
            System.err.println("Error: DFA not initialized!");
            return;
        }

        resetState();

        int index = 0;
        while(index < input_str.length()){
            Character c = input_str.charAt(index);
            processChar(c);
            if(!keep){
                index++;
            }
        }
    }

    public void addEdge(DFA_edge e){
        DFA_node u = node_list.get(e.u);
        DFA_node v = node_list.get(e.v);
        u.addEvent(e.c, new DFA_event(u, v, e.func));
    }

    public void addEdge(DFA_edgeInterval e){
        DFA_node u = node_list.get(e.u);
        DFA_node v = node_list.get(e.v);
        DFA_event event = new DFA_event(u, v, e.func);
        for(Character c = e.cl; c <= e.cr; c++){
            u.addEvent(c, event);
        }
    }

    public String getLexeme(){
        return lexeme.toString();
    }

    public void setLexeme(String str){
        clearLexeme();
        lexeme.append(str);
    }

    public void pushChar(Character c){
        lexeme.append(c);
    }

    public void clearLexeme(){
        lexeme.setLength(0);
    }

    private void processChar(Character c){
        DFA_event event = current_state.getNext(this, c);
        event.setChar(c);
        current_state = event.next_node;
        event.callback.execute(this, event);
    }
}
