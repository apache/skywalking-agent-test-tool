/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.skywalking.plugin.test.mockcollector.entity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class RegistryItemSerializer implements JsonSerializer<RegistryItem> {
    @Override
    public JsonElement serialize(RegistryItem src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonArray serviceArrays = new JsonArray();
        src.getServices().forEach((serviceName, serviceId) -> {
            JsonObject serviceJson = new JsonObject();
            serviceJson.addProperty(serviceName, serviceId);
            serviceArrays.add(serviceJson);
        });
        jsonObject.add("services", serviceArrays);

        JsonArray instanceArrays = new JsonArray();
        src.getInstanceMapping().forEach((serviceName, instanceIds) -> {
            JsonObject instanceJson = new JsonObject();
            instanceJson.addProperty(serviceName, instanceIds.size());
            instanceArrays.add(instanceJson);
        });
        jsonObject.add("instances", instanceArrays);

        JsonArray operationNameArrays = new JsonArray();
        src.getOperationNames().forEach((serviceName, operationNames) -> {
            JsonObject instanceJson = new JsonObject();
            instanceJson.add(serviceName, new Gson().toJsonTree(operationNames));
            operationNameArrays.add(instanceJson);
        });
        jsonObject.add("operationNames", operationNameArrays);

        JsonArray heartBeatArrays = new JsonArray();
        src.getHeartBeats().forEach((serviceName, count) -> {
            JsonObject instanceJson = new JsonObject();
            instanceJson.addProperty(serviceName, count);
            heartBeatArrays.add(instanceJson);
        });
        jsonObject.add("heartbeat", heartBeatArrays);
        return jsonObject;
    }
}
