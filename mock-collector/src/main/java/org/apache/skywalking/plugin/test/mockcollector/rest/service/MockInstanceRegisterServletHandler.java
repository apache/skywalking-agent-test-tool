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

package org.apache.skywalking.plugin.test.mockcollector.rest.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.apache.skywalking.apm.network.register.v2.ServiceInstance;
import org.apache.skywalking.apm.network.register.v2.ServiceInstances;
import org.apache.skywalking.plugin.test.mockcollector.entity.RegistryItem;
import org.apache.skywalking.plugin.test.mockcollector.entity.ValidateData;
import org.apache.skywalking.plugin.test.mockcollector.service.Sequences;
import org.apache.skywalking.plugin.test.mockcollector.util.ProtoBufJsonUtils;

public class MockInstanceRegisterServletHandler extends JettyJsonHandler {
    private static final String KEY = "key";
    private static final String VALUE = "value";

    public static final String SERVLET_PATH = "/v2/instance/register";

    @Override
    protected JsonElement doGet(final HttpServletRequest req) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected JsonElement doPost(final HttpServletRequest req) throws IOException {
        JsonArray response = new JsonArray();
        ServiceInstances.Builder builder = ServiceInstances.newBuilder();
        ProtoBufJsonUtils.fromJSON(getJsonBody(req), builder);

        for (ServiceInstance serviceInstance : builder.build().getInstancesList()) {
            int instanceId = Sequences.INSTANCE_SEQUENCE.incrementAndGet();
            ValidateData.INSTANCE.getRegistryItem()
                                 .registryInstance(
                                     new RegistryItem.Instance(serviceInstance.getServiceId(), instanceId));

            JsonObject register = new JsonObject();
            register.addProperty(KEY, serviceInstance.getInstanceUUID());
            register.addProperty(VALUE, instanceId);

            response.add(register);
        }
        return response;
    }
}
