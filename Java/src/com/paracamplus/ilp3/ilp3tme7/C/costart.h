#ifndef COSTART_H
#define COSTART_H

#include "ilp.h"
#include <stdio.h>

/* Primitive */

extern ILP_Object ILP_yield ();
extern ILP_Object ILP_resume (ILP_Object coroutine);


extern ILP_Object ILP_costart (void (*f)(), int argc, ...);

#define ILP_AllocateCoroutine()\
     ILP_malloc(sizeof(struct ILP_Object) \
          + sizeof(struct asCoroutine) , &ILP_object_Coroutine_class)
          
#define ILP_isCoroutine(o) ((o)->_class == &ILP_object_Coroutine_class)

extern struct ILP_Class ILP_object_Coroutine_class;
#endif