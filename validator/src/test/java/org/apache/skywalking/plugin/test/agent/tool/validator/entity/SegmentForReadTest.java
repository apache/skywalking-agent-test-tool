/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.skywalking.plugin.test.agent.tool.validator.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SegmentForReadTest {

    @Test
    public void testLogsSorting() {
        SegmentForRead.SpanForRead span = new SegmentForRead.SpanForRead();

        List<Map<String, List<Map<String, String>>>> logs = new ArrayList<>();

        Map<String, List<Map<String, String>>> log1 = new HashMap<>();
        List<Map<String, String>> events1 = new ArrayList<>();

        Map<String, String> endpointEvent1 = new HashMap<>();
        endpointEvent1.put("key", "endpoint");
        endpointEvent1.put("value", "endpoint3");
        events1.add(endpointEvent1);

        Map<String, String> typeEvent1 = new HashMap<>();
        typeEvent1.put("key", "type");
        typeEvent1.put("value", "type2");
        events1.add(typeEvent1);

        Map<String, String> messageEvent1 = new HashMap<>();
        messageEvent1.put("key", "message");
        messageEvent1.put("value", "message1");
        events1.add(messageEvent1);

        log1.put("logEvent", events1);
        logs.add(log1);

        Map<String, List<Map<String, String>>> log2 = new HashMap<>();
        List<Map<String, String>> events2 = new ArrayList<>();

        Map<String, String> endpointEvent2 = new HashMap<>();
        endpointEvent2.put("key", "endpoint");
        endpointEvent2.put("value", "endpoint1");
        events2.add(endpointEvent2);

        Map<String, String> typeEvent2 = new HashMap<>();
        typeEvent2.put("key", "type");
        typeEvent2.put("value", "type1");
        events2.add(typeEvent2);

        Map<String, String> messageEvent2 = new HashMap<>();
        messageEvent2.put("key", "message");
        messageEvent2.put("value", "message2");
        events2.add(messageEvent2);

        log2.put("logEvent", events2);
        logs.add(log2);

        Map<String, List<Map<String, String>>> log3 = new HashMap<>();
        List<Map<String, String>> events3 = new ArrayList<>();

        Map<String, String> endpointEvent3 = new HashMap<>();
        endpointEvent3.put("key", "endpoint");
        endpointEvent3.put("value", "endpoint1");
        events3.add(endpointEvent3);

        Map<String, String> typeEvent3 = new HashMap<>();
        typeEvent3.put("key", "type");
        typeEvent3.put("value", "type1");
        events3.add(typeEvent3);

        Map<String, String> messageEvent3 = new HashMap<>();
        messageEvent3.put("key", "message");
        messageEvent3.put("value", "message1");
        events3.add(messageEvent3);

        log3.put("logEvent", events3);
        logs.add(log3);

        span.setLogs(logs);

        List<LogEvent> sortedLogs = span.logs();

        Assert.assertEquals(3, sortedLogs.size());

        Assert.assertEquals("endpoint1", sortedLogs.get(0).getEndpoint());
        Assert.assertEquals("type1", sortedLogs.get(0).getType());
        Assert.assertEquals("message1", sortedLogs.get(0).getMessage().trim());

        Assert.assertEquals("endpoint1", sortedLogs.get(1).getEndpoint());
        Assert.assertEquals("type1", sortedLogs.get(1).getType());
        Assert.assertEquals("message2", sortedLogs.get(1).getMessage().trim());

        Assert.assertEquals("endpoint3", sortedLogs.get(2).getEndpoint());
        Assert.assertEquals("type2", sortedLogs.get(2).getType());
        Assert.assertEquals("message1", sortedLogs.get(2).getMessage().trim());
    }
}
