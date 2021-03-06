/**
 *(C) Copyright 2013-2015 Hewlett-Packard Development Company, L.P.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Copyright 2007 The Apache Software Foundation
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trafodion.dcs.util;

import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;

import org.trafodion.dcs.Constants;
import org.trafodion.dcs.util.Bytes;
import org.trafodion.dcs.util.DcsConfiguration;
import org.trafodion.dcs.script.ScriptManager;
import org.trafodion.dcs.script.ScriptContext;
import org.trafodion.dcs.server.ServerManager;

public class DcsNetworkConfiguration {

    private static final Log LOG = LogFactory
            .getLog(DcsNetworkConfiguration.class);
    private static Configuration conf;
    private InetAddress ia;
    private String intHostAddress;
    private String extHostAddress;
    private String canonicalHostName;
    private String extInterfaceName;
    private boolean matchedInterface = false;

    public DcsNetworkConfiguration(Configuration conf) throws Exception {

        this.conf = conf;
        ia = InetAddress.getLocalHost();

        String dcsDnsInterface = conf.get(Constants.DCS_DNS_INTERFACE,
                Constants.DEFAULT_DCS_DNS_INTERFACE);
        if (dcsDnsInterface.equalsIgnoreCase("default")) {
            intHostAddress = extHostAddress = ia.getHostAddress();
            canonicalHostName = ia.getCanonicalHostName();
            extInterfaceName = NetworkInterface.getByInetAddress(ia)
                    .getDisplayName();
            LOG.info("Using local host [" + extInterfaceName + ","
                    + canonicalHostName + "," + extHostAddress + "]");
        } else {
            // For all nics get all hostnames and addresses
            // and try to match against dcs.dns.interface property
            Enumeration<NetworkInterface> nics = NetworkInterface
                    .getNetworkInterfaces();
            while (nics.hasMoreElements() && !matchedInterface) {
                InetAddress inet = null;
                NetworkInterface ni = nics.nextElement();
                LOG.info("Found interface [" + ni.getDisplayName() + "]");
                if (dcsDnsInterface.equalsIgnoreCase(ni.getDisplayName())) {
                    LOG.info("Matched specified interface [" + ni.getName()
                            + "]");
                    inet = getInetAddress(ni);
                    getCanonicalHostName(ni, inet);
                    extInterfaceName = ni.getDisplayName();
                } else {
                    Enumeration<NetworkInterface> subIfs = ni
                            .getSubInterfaces();
                    for (NetworkInterface subIf : Collections.list(subIfs)) {
                        if (LOG.isDebugEnabled())
                            LOG.debug("Sub Interface Display name ["
                                    + subIf.getDisplayName() + "]");
                        if (dcsDnsInterface.equalsIgnoreCase(subIf
                                .getDisplayName())) {
                            LOG.info("Matched subIf [" + subIf.getName() + "]");
                            inet = getInetAddress(subIf);
                            getCanonicalHostName(subIf, inet);
                            extInterfaceName = ni.getDisplayName();
                            break;
                        }
                    }
                }
            }
        }

        if (!matchedInterface)
            checkCloud();
    }

    public void getCanonicalHostName(NetworkInterface ni, InetAddress inet)
            throws Exception {
        intHostAddress = extHostAddress = inet.getHostAddress();
        canonicalHostName = inet.getCanonicalHostName();
        LOG.info("Using interface [" + ni.getDisplayName() + ","
                + canonicalHostName + "," + extHostAddress + "]");
        ia = inet;
    }

    public InetAddress getInetAddress(NetworkInterface ni) throws Exception {
        InetAddress inet = null;
        Enumeration<InetAddress> rawAdrs = ni.getInetAddresses();
        while (rawAdrs.hasMoreElements()) {
            inet = rawAdrs.nextElement();
            LOG.info("Match Found interface [" + ni.toString() + ","
                    + ni.getDisplayName() + "," + inet.getCanonicalHostName()
                    + "," + inet.getHostAddress() + "]");
        }
        matchedInterface = true;
        return inet;
    }

    public void checkCloud() {
        // Ideally we want to use http://jclouds.apache.org/ so we can support
        // all cloud providers.
        // For now, use OpenStack Nova to retrieve int/ext network address map.
        LOG.info("Checking Cloud environment");
        String cloudCommand = conf.get(Constants.DCS_CLOUD_COMMAND,
                Constants.DEFAULT_DCS_CLOUD_COMMAND);
        ScriptContext scriptContext = new ScriptContext();
        scriptContext.setScriptName(Constants.SYS_SHELL_SCRIPT_NAME);
        scriptContext.setCommand(cloudCommand);
        LOG.info(scriptContext.getScriptName() + " exec ["
                + scriptContext.getCommand() + "]");
        ScriptManager.getInstance().runScript(scriptContext);// This will block
                                                             // while script is
                                                             // running

        StringBuilder sb = new StringBuilder();
        sb.append(scriptContext.getScriptName() + " exit code ["
                + scriptContext.getExitCode() + "]");
        if (!scriptContext.getStdOut().toString().isEmpty())
            sb.append(", stdout [" + scriptContext.getStdOut().toString() + "]");
        if (!scriptContext.getStdErr().toString().isEmpty())
            sb.append(", stderr [" + scriptContext.getStdErr().toString() + "]");
        LOG.info(sb.toString());

        if (!scriptContext.getStdOut().toString().isEmpty()) {
            Scanner scn = new Scanner(scriptContext.getStdOut().toString());
            scn.useDelimiter(",");
            intHostAddress = scn.next();// internal ip
            extHostAddress = scn.next();// external ip
            scn.close();
            LOG.info("Cloud environment found");
        } else
            LOG.info("Cloud environment not found");
    }

    public String getHostName() {
        return canonicalHostName;
    }

    public String getIntHostAddress() {
        return intHostAddress;
    }

    public String getExtHostAddress() {
        return extHostAddress;
    }

    public String getExtInterfaceName() {
        return extInterfaceName;
    }
}
