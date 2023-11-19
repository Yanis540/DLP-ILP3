package com.paracamplus.ilp3.ilp3tme7.compiler.normalizer;

import com.paracamplus.ilp1.compiler.interfaces.IASTCglobalVariable;
import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp3.ilp3tme7.compiler.ast.ASTCglobalCostart;
import com.paracamplus.ilp3.ilp3tme7.compiler.interfaces.IASTCglobalCostart;

public class NormalizationFactory extends com.paracamplus.ilp3.compiler.normalizer.NormalizationFactory 
implements INormalizationFactory{

    @Override
    public IASTCglobalCostart newCostart(IASTCglobalVariable function, IASTexpression[] arguments) {
        return new ASTCglobalCostart(function, arguments);
    }
    
}
