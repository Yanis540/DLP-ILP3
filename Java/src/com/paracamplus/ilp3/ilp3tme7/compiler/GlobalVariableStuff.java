package com.paracamplus.ilp3.ilp3tme7.compiler;

import com.paracamplus.ilp1.compiler.Primitive;
import com.paracamplus.ilp1.compiler.interfaces.IGlobalVariableEnvironment;

public class GlobalVariableStuff extends com.paracamplus.ilp3.compiler.GlobalVariableStuff{
     public static void fillGlobalVariables (IGlobalVariableEnvironment env) {
        com.paracamplus.ilp3.compiler.GlobalVariableStuff.fillGlobalVariables(env);
        env.addGlobalFunctionValue(
                new Primitive("resume", "ILP_resume", 1));
        env.addGlobalFunctionValue(
                new Primitive("yield", "ILP_yield", 0));
    }
}
