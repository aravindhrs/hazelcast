/*
 * Copyright (c) 2008-2010, Hazel Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hazelcast.impl;

import com.hazelcast.aws.impl.AWSClient;
import com.hazelcast.config.AwsConfig;
import com.hazelcast.config.Config;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class TcpIpJoinerOverAWS extends TcpIpJoiner {

    final AWSClient aws;
    final ILogger logger = Logger.getLogger(this.getClass().getName());

    public TcpIpJoinerOverAWS(Node node) {
        super(node);
        AwsConfig awsConfig = node.getConfig().getNetworkConfig().getJoin().getAwsConfig();
        aws = new AWSClient(awsConfig.getAccessKey(), awsConfig.getSecretKey());
        if (awsConfig.getRegion() != null && awsConfig.getRegion().length() > 0) {
            aws.setEndpoint("ec2." + awsConfig.getRegion() + ".amazonaws.com");
        }
    }

    @Override
    protected List<String> getMembers(Config config) {
        try {
            List<String> list = aws.getPrivateDnsNames();
            logger.log(Level.FINEST, "The list of possible members are: " + list);
            System.out.println("BDUADADADASKDNASDKASDNASKDNAS " + list);
            return list;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return Collections.EMPTY_LIST;
        }
    }
}