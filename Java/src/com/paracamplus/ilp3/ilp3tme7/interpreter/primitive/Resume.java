package com.paracamplus.ilp3.ilp3tme7.interpreter.primitive;

import com.paracamplus.ilp1.interpreter.interfaces.EvaluationException;
import com.paracamplus.ilp1.interpreter.primitive.UnaryPrimitive;

public class Resume extends  UnaryPrimitive {
    
    public Resume () {
        super("resume");
    }
    
    @Override
	public Object apply (Object f) throws EvaluationException {
        // call the thread 
        return f ; 
    }
}
