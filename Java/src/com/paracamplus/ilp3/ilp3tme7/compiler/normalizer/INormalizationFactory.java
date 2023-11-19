package com.paracamplus.ilp3.ilp3tme7.compiler.normalizer;

import com.paracamplus.ilp1.compiler.interfaces.IASTCglobalVariable;
import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp3.ilp3tme7.compiler.interfaces.IASTCglobalCostart;

public interface INormalizationFactory
extends com.paracamplus.ilp3.compiler.normalizer.INormalizationFactory  {
    IASTCglobalCostart newCostart(IASTCglobalVariable function,IASTexpression[] arguments);
}
