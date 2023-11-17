package com.paracamplus.ilp3.ilp3tme7.interpreter.primitive;

import com.paracamplus.ilp1.interpreter.interfaces.EvaluationException;
import com.paracamplus.ilp1.interpreter.primitive.UnaryPrimitive;
import com.paracamplus.ilp3.ilp3tme7.interpreter.CoroutineInstance;

public class Resume extends  UnaryPrimitive {
    
    public Resume () {
        super("resume");
    }
    
    @Override
	public Object apply (Object c) throws EvaluationException {
        // call the thread 
        if(!(c instanceof CoroutineInstance))
            throw new EvaluationException("Calling yield on Object :" + c.getClass() + " but expected instance of CoroutineInstance");
        CoroutineInstance coroutine = (CoroutineInstance) c; 
        coroutine.resumeCoroutine();
        return Boolean.TRUE; 
    }
}
