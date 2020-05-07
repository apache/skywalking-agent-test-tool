/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.plugin.test.mockcollector.mock.rest;

import com.google.common.io.CharStreams;
import com.google.gson.JsonElement;
import org.apache.skywalking.apm.network.language.agent.v3.SegmentObject;
import org.apache.skywalking.plugin.test.mockcollector.util.ProtoBufJsonUtils;
import org.apache.skywalking.plugin.test.mockcollector.util.TraceSegmentHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MockTraceSegmentSingleCollectServletHandler extends JettyJsonHandler {
    public static final String SERVLET_PATH = "/v3/segment";

    @Override
    protected JsonElement doGet(final HttpServletRequest req) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected JsonElement doPost(final HttpServletRequest req) throws IOException {
        String json = CharStreams.toString(req.getReader());

        SegmentObject.Builder segmentBuilder = SegmentObject.newBuilder();
        ProtoBufJsonUtils.fromJSON(json, segmentBuilder);

        TraceSegmentHandler.parseSegment(segmentBuilder.build());
        return null;
    }
}
