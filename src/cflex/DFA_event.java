package cflex;

import cflex.callback.ICallback;

public class DFA_event {
    private Character c;
    public final DFA_node current_node;
    public final DFA_node next_node;
    public final ICallback callback;
    public DFA_event(DFA_node current_node_, DFA_node next_node_, ICallback callback_){
        current_node = current_node_;
        next_node = next_node_;
        callback = callback_;
    }

    public Character getChar(){
        return c;
    }

    public void setChar(Character c_){
        c = c_;
    }
}
