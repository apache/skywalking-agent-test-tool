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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.apache.skywalking.apm.network.common.v3.Commands;
import org.apache.skywalking.apm.network.management.v3.InstanceProperties;
import org.apache.skywalking.plugin.test.mockcollector.util.ProtoBufJsonUtils;

public class MockManagementServiceKeepAliveHandler extends JettyJsonHandler {
    public static final String SERVLET_PATH = "/v3/management/keepAlive";
    private final Gson gson = new Gson();

    @Override
    protected JsonElement doGet(final HttpServletRequest req) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected JsonElement doPost(final HttpServletRequest req) throws IOException {
        final InstanceProperties.Builder request = InstanceProperties.newBuilder();
        ProtoBufJsonUtils.fromJSON(getJsonBody(req), request);
        request.build();
        return gson.fromJson(ProtoBufJsonUtils.toJSON(Commands.newBuilder().build()), JsonElement.class);
    }
}
