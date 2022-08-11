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

package org.apache.skywalking.plugin.test.mockcollector.util;

import org.apache.skywalking.apm.network.common.v3.KeyStringValuePair;
import org.apache.skywalking.apm.network.logging.v3.LogData;
import org.apache.skywalking.apm.network.logging.v3.LogDataBody;
import org.apache.skywalking.apm.network.logging.v3.TraceContext;
import org.apache.skywalking.plugin.test.mockcollector.entity.Log;
import org.apache.skywalking.plugin.test.mockcollector.entity.ValidateData;

import java.util.ArrayList;
import java.util.List;

public class LogDataHandler {

    public static void parseLogData(LogData logData) {
        Log.LogBuilder builder = Log.builder();

        builder.timestamp(logData.getTimestamp());

        builder.endpoint(logData.getEndpoint());

        LogDataBody body = logData.getBody();
        Log.LogDataBody.LogDataBodyBuilder logDataBodyBuilder = Log.LogDataBody.builder();
        logDataBodyBuilder.type(body.getType());
        switch (body.getContentCase()) {
            case TEXT:
                logDataBodyBuilder.content(new Log.TextLog(body.getText().getText()));
                break;
            case JSON:
                logDataBodyBuilder.content(new Log.JsonLog(body.getJson().getJson()));
                break;
            case YAML:
                logDataBodyBuilder.content(new Log.YamlLog(body.getYaml().getYaml()));
                break;
        }
        builder.body(logDataBodyBuilder.build());

        TraceContext traceContext = logData.getTraceContext();
        builder.traceContext(Log.TraceContext.builder()
                .traceId(traceContext.getTraceId())
                .traceSegmentId(traceContext.getTraceSegmentId())
                .spanId(traceContext.getSpanId())
                .build());

        Log.LogTags.LogTagsBuilder logTagsBuilder = Log.LogTags.builder();
        List<Log.KeyValuePair> kvs = new ArrayList<>();
        for (KeyStringValuePair keyStringValuePair : logData.getTags().getDataList()) {
            kvs.add(new Log.KeyValuePair(keyStringValuePair.getKey(), keyStringValuePair.getValue()));
        }
        builder.tags(logTagsBuilder.data(kvs).build());

        builder.layer(logData.getLayer());

        ValidateData.INSTANCE.getLogItems()
                .addLogItem(logData.getService(), builder.build());
    }

}
