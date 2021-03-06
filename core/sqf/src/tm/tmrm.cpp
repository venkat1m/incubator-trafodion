// @@@ START COPYRIGHT @@@
//
// (C) Copyright 2006-2014 Hewlett-Packard Development Company, L.P.
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

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <sys/types.h>

#include "seabed/ms.h"
#include "seabed/trace.h"
#include "tmlogging.h"

#include "tmtx.h"
#include "tmrm.h"
#include "tminfo.h"


void RM_Info::branch_lock()
{
   TMTrace (4, ("RM_Info::branch_lock(%d)\n", iv_branch_mutex.lock_count())); 
   iv_branch_mutex.lock();
}


void RM_Info::branch_unlock()
{
   TMTrace (4, ("RM_Info::branch_unlock(%d)\n", iv_branch_mutex.lock_count()));
   iv_branch_mutex.unlock(); 
}
