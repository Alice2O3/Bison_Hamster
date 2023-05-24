package C_Flex;

import java.util.HashMap;
import java.util.Map;
import C_Flex.DFA_Types.*;

public class DFA_node {
    private final Map<Character, DFA_event> next_state;
    private DFA_event default_event;

    public DFA_node(){
        next_state = new HashMap<>();
    }

    public DFA_event getNext(Character c){
        if(next_state.containsKey(c)){
            return next_state.get(c);
        }
        return default_event;
    }

    public void addEvent(Character c, DFA_event e){
        next_state.put(c, e);
    }

    public void setDefault_Event(DFA_event e){
        default_event = e;
    }
}
