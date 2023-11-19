package com.paracamplus.ilp3.ilp3tme7.ast;
import com.paracamplus.ilp1.ast.ASTexpression;
import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp3.ilp3tme7.interfaces.IASTcostart;
import com.paracamplus.ilp3.ilp3tme7.interfaces.IASTvisitor;
public class ASTcostart extends ASTexpression implements IASTcostart{

    public ASTcostart(IASTexpression function, IASTexpression[] arguments) {
        this.function = function ; 
        this.arguments = arguments ; 
    }
	private final IASTexpression function;
    private final IASTexpression[] arguments;
    
    @Override
	public IASTexpression getFunction () {
        return function;
    }
    @Override
	public IASTexpression[] getArguments () {
        return arguments;
    }

    @Override
	public <Result, Data, Anomaly extends Throwable> Result accept(
			com.paracamplus.ilp1.interfaces.IASTvisitor<Result, Data, Anomaly> visitor,
			Data data) throws Anomaly {
		return ((IASTvisitor <Result, Data, Anomaly>) visitor).visit(this, data);
	}
    
}
