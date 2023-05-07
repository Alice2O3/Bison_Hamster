package Cflex;

import Cflex.callback.ICallback;

public class DFA_edgeInterval {
    public final Integer u;
    public final Integer v;
    public final Character cl;
    public final Character cr;
    public final ICallback func;

    public DFA_edgeInterval(int u_, int v_, char cl_, char cr_, ICallback func_){
        u = u_;
        v = v_;
        cl = cl_;
        cr = cr_;
        func = func_;
    }

    public DFA_edgeInterval(int u_, int v_, int cl_, int cr_, ICallback func_){
        u = u_;
        v = v_;
        cl = (char) cl_;
        cr = (char) cr_;
        func = func_;
    }
}
