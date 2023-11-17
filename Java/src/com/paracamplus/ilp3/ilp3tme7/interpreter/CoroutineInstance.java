package com.paracamplus.ilp3.ilp3tme7.interpreter;
import java.lang.Thread;
import java.util.concurrent.Semaphore;

import com.paracamplus.ilp1.interpreter.interfaces.Invocable;



public class CoroutineInstance extends Thread {
    private Interpreter interpreter;
    private Invocable func;
    private Object[] args;
    private Semaphore resumeSemaphore; 
    private Semaphore yieldSemaphore; 
    private boolean isFinished; 
    public CoroutineInstance (Interpreter interpreter, Invocable func, Object[] args){
        this.interpreter = interpreter; 
        this.func = func; 
        this.args = args;
        this.resumeSemaphore = new Semaphore(0); 
        this.yieldSemaphore = new Semaphore(0); 
        start();
    }
    
    public void run() {
        try{
            // bloquer le thread courant jusqu'à un appel spécifique de resume()
            resumeSemaphore.acquire();
            func.apply(interpreter, args); //<=== invoquer ça dans la méthode run 
            // quand on a fini la fonction faudra rester sans le processus 
            this.isFinished = true; 
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        //
        yieldSemaphore.release();

    }

    public void yieldCoroutine(){
        try{
            // release le yield, et puis bloquer le thread jusqu'à nouvel appelle de resume 
            yieldSemaphore.release();
            resumeSemaphore.acquire();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void resumeCoroutine(){
        try{
            // release le thread pour lancer la fonction 
            resumeSemaphore.release();
            // si il n'est pas fini alors bloquer le thread yield 
            if(!isFinished)
                yieldSemaphore.acquire();

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    




}
