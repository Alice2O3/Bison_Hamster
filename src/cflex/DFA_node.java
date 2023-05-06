package cflex;

import cflex.callback.DFA_callback_fallback;
import cflex.callback.*;

import java.util.HashMap;
import java.util.Map;

public class DFA_node {
    private final Map<Character, DFA_event> next_state;
    private int index;

    public DFA_node(){
        next_state = new HashMap<>();
    }

    public DFA_event getNext(DFA dfa, Character c){
        if(next_state.containsKey(c)){
            return next_state.get(c);
        }
        System.out.println("Warning: Unsupported state, using fallback instead.");
        return new DFA_event(this, dfa.fallback_node, new DFA_callback_fallback());
    }

    public void addEvent(Character c, DFA_event e){
        next_state.put(c, e);
    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int index_){
        index = index_;
    }
}
