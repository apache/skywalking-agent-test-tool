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
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.skywalking.apm.network.register.v2.Service;
import org.apache.skywalking.apm.network.register.v2.Services;
import org.apache.skywalking.plugin.test.mockcollector.entity.RegistryItem;
import org.apache.skywalking.plugin.test.mockcollector.entity.ValidateData;
import org.apache.skywalking.plugin.test.mockcollector.mock.Sequences;
import org.apache.skywalking.plugin.test.mockcollector.util.ProtoBufJsonUtils;

public class MockServiceRegisterServletHandler extends JettyJsonHandler {
    public static final String SERVLET_PATH = "/v2/service/register";
    private static final String KEY = "key";
    private static final String VALUE = "value";

    @Override
    protected JsonElement doGet(final HttpServletRequest req) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected JsonElement doPost(final HttpServletRequest req) throws IOException {
        JsonArray response = new JsonArray();

        Services.Builder servicesBuilder = Services.newBuilder();
        ProtoBufJsonUtils.fromJSON(getJsonBody(req), servicesBuilder);
        List<Service> serviceList = servicesBuilder.build().getServicesList();

        for (Service service : serviceList) {
            String serviceName = service.getServiceName();
            if (serviceName.startsWith("localhost") || serviceName.startsWith("127.0.0.1")
                || serviceName.contains(":") || serviceName.contains("/")) {
                return response;
            }

            Integer serviceId = Sequences.SERVICE_MAPPING.get(serviceName);
            if (serviceId == null) {
                serviceId = Sequences.ENDPOINT_SEQUENCE.incrementAndGet();
                Sequences.SERVICE_MAPPING.put(serviceName, serviceId);
                ValidateData.INSTANCE.getRegistryItem()
                                     .registryService(new RegistryItem.Service(serviceName, serviceId));
            }
            JsonObject mapping = new JsonObject();
            mapping.addProperty(KEY, serviceName);
            mapping.addProperty(VALUE, serviceId);
            response.add(mapping);
        }
        return response;
    }
}
