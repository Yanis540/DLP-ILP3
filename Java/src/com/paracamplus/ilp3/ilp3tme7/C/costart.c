#include "ilp.h"
#include "costart.h"





#define COROUTINE_STACK_SIZE 1024 * 32

static ILP_Object current_coroutine = NULL;

// Function to call after the termination of coroutine
void ILP_coroutine_end()
{
    current_coroutine->_content.asCoroutine.finished = 1;
    // Return the coroutine of caller
    if (setcontext(current_coroutine->_content.asCoroutine.caller_ctx) == -1) {
        ILP_error("swapcontext failed");
    }
}

ILP_Object ILP_costart(void (*f)(), int argc, ...)
{
    // Allocation
    va_list args;
    ILP_Object coroutine = ILP_malloc(sizeof(struct ILP_Object), &ILP_object_Coroutine_class);
    ucontext_t* caller_ctx = ILP_MALLOC(sizeof(ucontext_t));
    ucontext_t* coroutine_ctx = ILP_MALLOC(sizeof(ucontext_t));
    ucontext_t* end_ctx = ILP_MALLOC(sizeof(ucontext_t));
    char* coroutine_stk = ILP_MALLOC(COROUTINE_STACK_SIZE);
    char* end_stk = ILP_MALLOC(COROUTINE_STACK_SIZE);
    if ( !caller_ctx || !coroutine_ctx || !end_ctx || !coroutine_stk || !end_stk) {
        return ILP_die("Memory exhaustion");
    }

    // Current Context
    getcontext(caller_ctx);

    // Context to call when the coroutine is terminated
    getcontext(end_ctx);
    end_ctx->uc_stack.ss_sp = end_stk;
    end_ctx->uc_stack.ss_size = COROUTINE_STACK_SIZE;
    end_ctx->uc_link = NULL;
    makecontext(end_ctx, ILP_coroutine_end, 0);

    // context of coroutine
    getcontext(coroutine_ctx);
    coroutine_ctx->uc_stack.ss_sp = coroutine_stk;
    coroutine_ctx->uc_stack.ss_size = COROUTINE_STACK_SIZE;
    coroutine_ctx->uc_link = end_ctx;

    coroutine->_content.asCoroutine.caller_ctx = caller_ctx;
    coroutine->_content.asCoroutine.coroutine_ctx = coroutine_ctx;
    coroutine->_content.asCoroutine.finished = 0;

    // fonction and arguments of coroutine
    va_start(args, argc);
    switch(argc){
        case 0: {
            makecontext(coroutine_ctx, f, 1, NULL);
            break;
        }
        case 1: {
            ILP_Object arg1 = va_arg(args, ILP_Object);  
            makecontext(coroutine_ctx, f, 2, NULL, arg1);
            break;
        }
        case 2: {
            ILP_Object arg1 = va_arg(args, ILP_Object);
            ILP_Object arg2 = va_arg(args, ILP_Object);
            makecontext(coroutine_ctx, f, 3, NULL, arg1, arg2);
            break;
        }
        case 3: {
            ILP_Object arg1 = va_arg(args, ILP_Object);
            ILP_Object arg2 = va_arg(args, ILP_Object);
            ILP_Object arg3 = va_arg(args, ILP_Object);
            makecontext(coroutine_ctx, f, 4, NULL, arg1, arg2, arg3);
            break;
        }
        case 4: {
            ILP_Object arg1 = va_arg(args, ILP_Object);
            ILP_Object arg2 = va_arg(args, ILP_Object);
            ILP_Object arg3 = va_arg(args, ILP_Object);
            ILP_Object arg4 = va_arg(args, ILP_Object);
            makecontext(coroutine_ctx, f, 5, NULL, arg1, arg2, arg3, arg4);
            break;
        }
        default: {
            // FIXME restriction on arity!
        }
    }
    va_end(args);
    return coroutine;
}

ILP_Object ILP_yield()
{
    if (!current_coroutine) {
        return ILP_error("yield called outside a coroutine");
    }
    //change the context from coroutine to caller
    if (swapcontext(current_coroutine->_content.asCoroutine.coroutine_ctx,
            current_coroutine->_content.asCoroutine.caller_ctx) == -1) {
        return ILP_error("swapcontext failed");
    }
     
     return ILP_FALSE;
}

ILP_Object ILP_resume(ILP_Object coroutine)
{
    if(!ILP_isCoroutine(coroutine))
        ILP_domain_error("Calling resume on not coroutine object",coroutine);
    if (!coroutine->_content.asCoroutine.finished) {
        ILP_Object old_coroutine = current_coroutine;
        current_coroutine = coroutine;
        // change the context from caller to coroutine
        if (swapcontext(coroutine->_content.asCoroutine.caller_ctx,
                coroutine->_content.asCoroutine.coroutine_ctx) == -1) {
            return ILP_error("swapcontext failed");
        }
        // in the case of imbricate coroutines
        current_coroutine = old_coroutine;
    }
    
    return ILP_FALSE;
}




struct ILP_Class ILP_object_Coroutine_class = {
    &ILP_object_Class_class,
    { { &ILP_object_Object_class,
        "Coroutine",
        0,
        NULL,
        2,
        { ILP_print,
        ILP_classOf } } }
};