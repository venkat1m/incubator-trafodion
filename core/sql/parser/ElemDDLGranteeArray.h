#ifndef ELEMDDLGRANTEEARRAY_H
#define ELEMDDLGRANTEEARRAY_H
/* -*-C++-*-
 *****************************************************************************
 *
 * File:         ElemDDLGranteeArray.h
 * Description:  class for an array of pointers pointing to instances of
 *               class ElemDDLGrantee
 *               
 *               
 * Created:      5/26/95
 * Language:     C++
 *
 *
// @@@ START COPYRIGHT @@@
//
// (C) Copyright 1995-2014 Hewlett-Packard Development Company, L.P.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//
// @@@ END COPYRIGHT @@@
 *
 *
 *****************************************************************************
 */


#include "Collections.h"
#include "ElemDDLGrantee.h"
#ifndef   SQLPARSERGLOBALS_CONTEXT_AND_DIAGS
#define   SQLPARSERGLOBALS_CONTEXT_AND_DIAGS
#endif
#include "SqlParserGlobals.h"

// -----------------------------------------------------------------------
// contents of this file
// -----------------------------------------------------------------------
class ElemDDLGranteeArray;

// -----------------------------------------------------------------------
// forward references
// -----------------------------------------------------------------------
// None.

// -----------------------------------------------------------------------
// Definition of class ElemDDLGranteeArray
// -----------------------------------------------------------------------
class ElemDDLGranteeArray : public LIST(ElemDDLGrantee *)
{

public:

  // constructor
  ElemDDLGranteeArray(CollHeap *heap = PARSERHEAP());

  // virtual destructor
  virtual ~ElemDDLGranteeArray();

private:

}; // class ElemDDLGranteeArray

#endif // ELEMDDLGRANTEEARRAY_H
