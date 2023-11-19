package com.paracamplus.ilp3.ilp3tme7.compiler;

import java.util.Set;

import com.paracamplus.ilp1.compiler.CompilationException;
import com.paracamplus.ilp1.compiler.interfaces.IASTClocalVariable;
import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp3.compiler.interfaces.IASTCprogram;
import com.paracamplus.ilp3.ilp3tme7.compiler.interfaces.IASTCglobalCostart;
import com.paracamplus.ilp3.ilp3tme7.compiler.interfaces.IASTCvisitor;
import com.paracamplus.ilp3.ilp3tme7.interfaces.IASTcostart;

public class FreeVariableCollector 
extends com.paracamplus.ilp3.compiler.FreeVariableCollector
implements IASTCvisitor<Void, Set<IASTClocalVariable>, CompilationException>{

    public FreeVariableCollector(IASTCprogram program) {
        super(program);
    }
    
    public Void visit(IASTCglobalCostart iast, Set<IASTClocalVariable> variables) throws CompilationException {
        iast.getFunction().accept(this, variables);
        for ( IASTexpression expression : iast.getArguments() ) {
            expression.accept(this, variables);
        }
        return null;
    }

	@Override
	public Void visit(IASTcostart iast, Set<IASTClocalVariable> variables) throws CompilationException {
        return visit((IASTCglobalCostart) iast, variables);
	}

    
    
}
