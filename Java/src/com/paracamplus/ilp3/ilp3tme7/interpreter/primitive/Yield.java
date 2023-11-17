package com.paracamplus.ilp3.ilp3tme7.interpreter.primitive;

import com.paracamplus.ilp1.interpreter.interfaces.EvaluationException;
import com.paracamplus.ilp1.interpreter.primitive.Primitive;
import com.paracamplus.ilp3.ilp3tme7.interpreter.CoroutineInstance;

public class Yield extends  Primitive {
    
    public Yield () {
        super("yield");
    }
    @Override
	public int getArity () {
        return 0;
    }
    
    @Override
	public Object apply () throws EvaluationException {
        // call the thread 
        Thread c = Thread.currentThread();
        if(!(c instanceof CoroutineInstance))
            return Boolean.FALSE;  
        CoroutineInstance coroutine = (CoroutineInstance) c; 
        coroutine.yieldCoroutine();
        // Thread.currentThread();
        return Boolean.TRUE; 
    }
}
