package com.paracamplus.ilp3.ilp3tme7.interfaces;

import com.paracamplus.ilp1.interfaces.IASTexpression;

public interface IASTcostart extends IASTexpression {
	IASTexpression getFunction();
	IASTexpression[] getArguments();
}

