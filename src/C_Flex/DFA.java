package C_Flex;

import java.util.ArrayList;
import java.util.List;
import C_Flex.DFA_Types.*;
import C_Flex.callback.ICallback;

public class DFA {
    private final List<DFA_node> node_list;
    private DFA_node current_state; //The default state is 0
    private DFA_node initial_state;
    private StringBuilder lexeme;
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
            node_list.add(node);
        }
        initial_state = node_list.get(0);
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

    public void gotoInitialState(){
        current_state = initial_state;
    }

    public void processString(String input_str){
        if(initial_state == null){
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

    public void addEdge(Integer u_, Integer v_, ICallback callback){
        DFA_node u = node_list.get(u_);
        DFA_node v = node_list.get(v_);
        u.setDefault_Event(new DFA_event(u, v, callback));
    }

    public void addEdge(Integer u_, Integer v_, Character c, ICallback callback){
        DFA_node u = node_list.get(u_);
        DFA_node v = node_list.get(v_);
        u.addEvent(c, new DFA_event(u, v, callback));
    }

    public void addEdge(Integer u_, Integer v_, Character cl, Character cr, ICallback callback){
        DFA_node u = node_list.get(u_);
        DFA_node v = node_list.get(v_);
        DFA_event event = new DFA_event(u, v, callback);
        for(Character c = cl; c <= cr; c++){
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
        DFA_event event = current_state.getNext(c);
        event.setChar(c);
        current_state = event.next_node;
        event.callback.execute(this, event);
    }
}
