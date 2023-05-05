package cflex.dfa;

import cflex.dfa.callback.ICallback;

public class DFA_edge {
    public final Integer u;
    public final Integer v;
    public final Character c;
    public final ICallback func;

    public DFA_edge(int u_, int v_, char c_, ICallback func_){
        u = u_;
        v = v_;
        c = c_;
        func = func_;
    }
}
