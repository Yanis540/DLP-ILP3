package com.paracamplus.ilp3.ilp3tme7.compiler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Set;

import com.paracamplus.ilp1.compiler.AssignDestination;
import com.paracamplus.ilp1.compiler.CompilationException;
import com.paracamplus.ilp1.compiler.NoDestination;
import com.paracamplus.ilp1.compiler.ReturnDestination;
import com.paracamplus.ilp1.compiler.interfaces.IASTCglobalVariable;
import com.paracamplus.ilp1.compiler.interfaces.IGlobalVariableEnvironment;
import com.paracamplus.ilp1.compiler.interfaces.IOperatorEnvironment;
import com.paracamplus.ilp3.compiler.interfaces.IASTClambda;
import com.paracamplus.ilp3.compiler.interfaces.IASTCprogram;

import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.ilp2.interfaces.IASTfunctionDefinition;
import com.paracamplus.ilp3.ilp3tme7.compiler.interfaces.IASTCglobalCostart;
import com.paracamplus.ilp3.ilp3tme7.compiler.interfaces.IASTCvisitor;

import com.paracamplus.ilp3.ilp3tme7.compiler.normalizer.INormalizationFactory;
import com.paracamplus.ilp3.ilp3tme7.compiler.normalizer.NormalizationFactory;
import com.paracamplus.ilp3.ilp3tme7.compiler.normalizer.Normalizer;
import com.paracamplus.ilp3.ilp3tme7.interfaces.IASTcostart;
import com.paracamplus.ilp3.interfaces.IASTprogram;

public class Compiler extends com.paracamplus.ilp3.compiler.Compiler
implements IASTCvisitor<Void, Compiler.Context, CompilationException>{
    public Compiler(IOperatorEnvironment ioe, IGlobalVariableEnvironment igve) {
        super(ioe, igve);
    }
    @Override
    public String compile(IASTprogram program)
            throws CompilationException {

        IASTCprogram newprogram = normalize(program);
        newprogram = (IASTCprogram) optimizer.transform(newprogram);

        GlobalVariableCollector gvc = new GlobalVariableCollector();
        Set<IASTCglobalVariable> gvs = gvc.analyze(newprogram);
        newprogram.setGlobalVariables(gvs);

        FreeVariableCollector fvc = new FreeVariableCollector(newprogram);
        newprogram = fvc.analyze();

        Context context = new Context(NoDestination.NO_DESTINATION);
        StringWriter sw = new StringWriter();
        try {
            out = new BufferedWriter(sw);
            this.visit(newprogram, context);
            out.flush();
        } catch (IOException exc) {
            throw new CompilationException(exc);
        }
        return sw.toString();
    }
    public IASTCprogram normalize(IASTprogram program) 
        throws CompilationException {
        INormalizationFactory nf = new NormalizationFactory();
        Normalizer normalizer = new Normalizer(nf);
        IASTCprogram newprogram = normalizer.transform(program);
        return newprogram;
    }
	
    public Void visit(IASTCprogram iast, Context context)
            throws CompilationException {
        emit(cProgramPrefix);
        
        emit(cGlobalVariablesPrefix);
        for ( IASTCglobalVariable gv : iast.getGlobalVariables() ) {
            emit("ILP_Object ");
            emit(gv.getMangledName());
            emit(";\n");
        }
        emit(cGlobalVariablesSuffix);
        
        emit(cPrototypesPrefix);
        Context c = context.redirect(NoDestination.NO_DESTINATION);
        for ( IASTfunctionDefinition ifd : iast.getFunctionDefinitions() ) {
            this.emitPrototype(ifd, c);
        }
        for ( IASTClambda closure : iast.getClosureDefinitions() ) {
            this.emitPrototype(closure, c);
        }
        emit(cFunctionsPrefix);
        for ( IASTfunctionDefinition ifd : iast.getFunctionDefinitions() ) {
            this.visit(ifd, c);
            emitClosure(ifd, c);
        }
        for ( IASTClambda closure : iast.getClosureDefinitions() ) {
            this.emitFunction(closure, c);
        }
        emit(cFunctionsSuffix);
        
        emit(cBodyPrefix);
        Context cr = context.redirect(ReturnDestination.RETURN_DESTINATION);
        iast.getBody().accept(this, cr);
        emit(cBodySuffix);
        
        emit(cProgramSuffix);
        return null;
    }
    protected String cProgramPrefix = ""
        + "#include <stdio.h> \n"
        + "#include <stdlib.h> \n"
        + "#include \"ilp.h\"  \n"
        + "#include \"costart.h\" \n\n";

    
    public Void visit(IASTCglobalCostart iast, Context context) throws CompilationException {
        emit("{ \n");
        IASTexpression[] arguments = iast.getArguments();
        IASTvariable[] tmps = new IASTvariable[arguments.length];
        for ( int i=0 ; i<arguments.length ; i++ ) {
            IASTvariable tmp = context.newTemporaryVariable();
            emit("  ILP_Object " + tmp.getMangledName() + "; \n");
            tmps[i] = tmp;
        }
        for ( int i=0 ; i<arguments.length ; i++ ) {
            IASTexpression expression = arguments[i];
            IASTvariable tmp = tmps[i];
            Context c = context.redirect(new AssignDestination(tmp));
            expression.accept(this, c);
        }
        emit(context.destination.compile());
        emit("ILP_costart(");
        emit("(void(*)())ilp__" + ((IASTCglobalVariable)iast.getFunction()).getMangledName());
        emit(", ");
        emit(arguments.length);
        for ( int i=0 ; i<arguments.length ; i++ ) {
            IASTvariable tmp = tmps[i];
            emit(", ");
            emit(tmp.getMangledName());
        }
        emit(");\n}\n");
        return null;
    }
    @Override
    public Void visit(IASTcostart iast, Context context) throws CompilationException {
        return visit((IASTCglobalCostart)iast,context);
    }
}
