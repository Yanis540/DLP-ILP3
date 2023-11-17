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
        // ici dans ce code faudra appeler la fonction 
        try{
            resumeSemaphore.acquire();
            func.apply(interpreter, args); //<=== invoquer ça dans la méthode run 
            this.isFinished = true; 
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        yieldSemaphore.release();

    }

    public void yieldCoroutine(){
        try{

            yieldSemaphore.release();
            resumeSemaphore.acquire();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void resumeCoroutine(){
        try{
            resumeSemaphore.release();
            if(!isFinished)
                yieldSemaphore.acquire();

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    




}
