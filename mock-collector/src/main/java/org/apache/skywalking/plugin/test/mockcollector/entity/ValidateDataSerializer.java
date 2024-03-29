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

import com.google.gson.*;

import java.lang.reflect.Type;

public class ValidateDataSerializer implements JsonSerializer<ValidateData> {
    @Override
    public JsonElement serialize(ValidateData src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new GsonBuilder().registerTypeAdapter(SegmentItems.class, new SegmentItemsSerializer())
                                     .registerTypeAdapter(MeterItems.class, new MeterItemsSerializer())
                                     .registerTypeAdapter(LogItems.class, new LogItemsSerializer())
                                     .create();

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("segmentItems", gson.toJsonTree(src.getSegmentItem()));
        jsonObject.add("meterItems", gson.toJsonTree(src.getMeterItems()));
        jsonObject.add("logItems", gson.toJsonTree(src.getLogItems()));
        return jsonObject;
    }
}
