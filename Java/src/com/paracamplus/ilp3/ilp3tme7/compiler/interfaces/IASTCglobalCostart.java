package com.paracamplus.ilp3.ilp3tme7.compiler.interfaces;

import com.paracamplus.ilp1.compiler.interfaces.IASTCglobalVariable;
import com.paracamplus.ilp3.ilp3tme7.interfaces.IASTcostart;

public interface IASTCglobalCostart extends IASTcostart {
    @Override
    IASTCglobalVariable getFunction () ;
}
