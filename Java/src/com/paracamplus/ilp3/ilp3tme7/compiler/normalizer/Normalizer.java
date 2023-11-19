package com.paracamplus.ilp3.ilp3tme7.compiler.normalizer;

import com.paracamplus.ilp1.compiler.CompilationException;
import com.paracamplus.ilp1.compiler.interfaces.IASTCglobalVariable;
import com.paracamplus.ilp1.compiler.normalizer.INormalizationEnvironment;
import com.paracamplus.ilp1.compiler.normalizer.NormalizationEnvironment;
import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp2.compiler.interfaces.IASTCfunctionDefinition;
import com.paracamplus.ilp2.compiler.interfaces.IASTCglobalFunctionVariable;
import com.paracamplus.ilp2.interfaces.IASTfunctionDefinition;
import com.paracamplus.ilp3.compiler.interfaces.IASTCprogram;
import com.paracamplus.ilp3.ilp3tme7.compiler.interfaces.IASTCglobalCostart;
import com.paracamplus.ilp3.ilp3tme7.interfaces.IASTcostart;
import com.paracamplus.ilp3.ilp3tme7.interfaces.IASTvisitor;
import com.paracamplus.ilp3.interfaces.IASTprogram;

public class Normalizer 
extends com.paracamplus.ilp3.compiler.normalizer.Normalizer
implements IASTvisitor<IASTexpression, INormalizationEnvironment, CompilationException>{

    public Normalizer(INormalizationFactory factory) {
        super(factory);
        System.out.println();
    }
    public IASTCprogram transform(IASTprogram program) 
            throws CompilationException {
        INormalizationEnvironment env = NormalizationEnvironment.EMPTY;
    
        IASTfunctionDefinition[] functions = program.getFunctionDefinitions();
        IASTCfunctionDefinition[] funs = 
                new IASTCfunctionDefinition[functions.length];
        for ( IASTfunctionDefinition function : functions ) {
            IASTCglobalFunctionVariable gfv =
                ((INormalizationFactory)factory).newGlobalFunctionVariable(function.getName())
            ;
            env = env.extend(gfv, gfv);
        }
        for ( int i=0 ; i<functions.length ; i++ ) {
            IASTfunctionDefinition function = functions[i];
            IASTCfunctionDefinition newfunction = visit(function, env);
            funs[i] = newfunction;
        }
        
        IASTexpression body = program.getBody();
        IASTexpression newbody = body.accept(this, env);
        return ((INormalizationFactory)factory).newProgram(funs, newbody);
    }

    @Override
    public IASTCglobalCostart visit(IASTcostart iast, INormalizationEnvironment env) throws CompilationException {
        // System.out.println("//>>>>>>>>>>>>>>>>>>>Created a new IASTCglobal costart ");

        IASTexpression funexpr = iast.getFunction().accept(this, env);
        if(!(funexpr instanceof IASTCglobalVariable))
            throw new CompilationException("Coroutine can only have global function");
        IASTCglobalVariable f = (IASTCglobalVariable) funexpr;
        
    	IASTexpression[] arguments = iast.getArguments();
    	IASTexpression[] args = new IASTexpression[arguments.length];
    	for ( int i=0 ; i<arguments.length ; i++ ) {
    		IASTexpression argument = arguments[i];
    		IASTexpression arg = argument.accept(this, env);
    		args[i] = arg;
    	}
        return ((INormalizationFactory)factory).newCostart(f,args);
    }
    
}
