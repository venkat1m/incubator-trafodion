/**********************************************************************
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
**********************************************************************/
#ifndef ELEMDDLPARAMNAME_H
#define ELEMDDLPARAMNAME_H
/* -*-C++-*-
 *****************************************************************************
 *
 * File:         ElemDDLParamName.h
 * Description:  class for Param Name (parse node) elements in DDL
 *               statements
 *
 *               
 * Created:      10/01/1999
 * Language:     C++
 *
 *
 *
 *
 *****************************************************************************
 */


#include "ElemDDLNode.h"

// -----------------------------------------------------------------------
// contents of this file
// -----------------------------------------------------------------------
class ElemDDLParamName;

// -----------------------------------------------------------------------
// forward references
// -----------------------------------------------------------------------
// None.

// -----------------------------------------------------------------------
// Param Name (parse node) elements in DDL statements
// -----------------------------------------------------------------------
class ElemDDLParamName : public ElemDDLNode
{

public:

  // constructor
  ElemDDLParamName(const NAString &paramName)
  : ElemDDLNode(ELM_PARAM_NAME_ELEM),
  paramName_(paramName, PARSERHEAP())
  { }

  // virtual destructor
  virtual ~ElemDDLParamName();

  // cast
  virtual ElemDDLParamName * castToElemDDLParamName();

  // accessor
  inline const NAString & getParamName() const;

  // member functions for tracing
  virtual const NAString displayLabel1() const;
  virtual const NAString getText() const;


private:

  NAString paramName_;

}; // class ElemDDLParamName

// -----------------------------------------------------------------------
// definitions of inline methods for class ElemDDLParamName
// -----------------------------------------------------------------------


inline const NAString &
ElemDDLParamName::getParamName() const
{
  return paramName_;
}

#endif // ELEMDDLPARAMNAME_H
