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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.apache.skywalking.apm.network.register.v2.ServiceInstancePingPkg;
import org.apache.skywalking.plugin.test.mockcollector.entity.RegistryItem;
import org.apache.skywalking.plugin.test.mockcollector.entity.ValidateData;
import org.apache.skywalking.plugin.test.mockcollector.util.ProtoBufJsonUtils;

public class MockServiceInstancePingServletHandler extends JettyJsonHandler {
    public static final String SERVLET_PATH = "/v2/instance/heartbeat";

    @Override
    protected JsonElement doGet(final HttpServletRequest req) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected JsonElement doPost(final HttpServletRequest req) throws IOException {

        ServiceInstancePingPkg.Builder builder = ServiceInstancePingPkg.newBuilder();
        ProtoBufJsonUtils.fromJSON(getJsonBody(req), builder);
        ServiceInstancePingPkg instancePingPkg = builder.build();

        ValidateData.INSTANCE.getRegistryItem()
                             .registryHeartBeat(new RegistryItem.HeartBeat(instancePingPkg.getServiceInstanceId()));
        return new JsonArray();
    }
}
