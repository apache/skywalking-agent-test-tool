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

package org.apache.skywalking.plugin.test.agent.tool.validator.assertor;

import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.exception.*;
import org.apache.skywalking.plugin.test.agent.tool.validator.entity.*;
import org.apache.skywalking.plugin.test.agent.tool.validator.exception.AssertFailedException;
import org.apache.skywalking.plugin.test.agent.tool.validator.exception.ExceptedException;

import java.util.*;

public class LogAssert {
    public static void assertEquals(LogItem expected, LogItem actual) {
        if (expected.getLogs() == null) {
            return;
        }

        logsEquals(expected.getLogs(), actual.getLogs());
    }

    private static void logsEquals(List<LogData> excepted, List<LogData> actual) {
        if (excepted == null) {
            return;
        }

        if (actual == null || excepted.size() != actual.size()) {
            throw new LogSizeNotEqualsException(excepted.size(), (actual != null) ? actual.size() : 0);
        }

        for (int index = 0; index < excepted.size(); index++) {
            LogData exceptedLog = excepted.get(index);
            LogData actualLog = actual.get(index);

            try {
                logEquals(exceptedLog, actualLog);
            } catch (AssertFailedException e) {
                throw new LogAssertFailedException(e, exceptedLog, actualLog);
            }
        }
    }

    private static void logEquals(LogData excepted, LogData actual) {
        ExpressParser.parse(excepted.getTimestamp()).assertValue("timestamp", actual.getTimestamp());
        ExpressParser.parse(excepted.getEndpoint()).assertValue("endpoint", actual.getEndpoint());
        logDataBodyEquals(excepted.getBody(), actual.getBody());
        traceContextEquals(excepted.getTraceContext(), actual.getTraceContext());
        tagsEquals(excepted.getTags(), actual.getTags());
        ExpressParser.parse(excepted.getLayer()).assertValue("layer", actual.getLayer());
    }

    private static void logDataBodyEquals(LogDataBody excepted, LogDataBody actual) {
        ExpressParser.parse(excepted.getType()).assertValue("body.type", actual.getType());

        HashMap<String, String> exceptedContent = excepted.getContent();
        if (exceptedContent == null) {
            return;
        }

        HashMap<String, String> actualContent = actual.getContent();
        if (actualContent == null) {
            throw new LogBodyContentNotFoundException(exceptedContent);
        }

        String content;
        if ((content = exceptedContent.get("text")) != null) {
            ExpressParser.parse(content).assertValue("body.content.text", actualContent.get("text"));
        } else if ((content = exceptedContent.get("json")) != null) {
            ExpressParser.parse(content).assertValue("body.content.json", actualContent.get("json"));
        } else if ((content = exceptedContent.get("yaml")) != null) {
            ExpressParser.parse(content).assertValue("body.content.yaml", actualContent.get("yaml"));
        } else {
            throw new ExceptedException("unknown content field");
        }
    }

    private static void traceContextEquals(TraceContext excepted, TraceContext actual) {
        if (excepted == null) {
            return;
        }
        if (actual == null) {
            throw new LogTraceContextNotFoundException(excepted);
        }
        ExpressParser.parse(excepted.getTraceId()).assertValue("traceContext.traceId", actual.getTraceId());
        ExpressParser.parse(excepted.getTraceSegmentId()).assertValue("traceContext.traceSegmentId", actual.getTraceSegmentId());
        ExpressParser.parse(excepted.getSpanId()).assertValue("traceContext.spanId", actual.getSpanId());
    }

    private static void tagsEquals(LogTags excepted, LogTags actual) {
        if (excepted == null) {
            return;
        }
        if (actual == null) {
            throw new LogTagsNotFoundException(excepted);
        }

        List<LogTags.KeyValuePair> exceptedData = excepted.getData();
        List<LogTags.KeyValuePair> actualData = actual.getData();

        if (exceptedData == null) {
            return;
        }
        if (actualData == null) {
            throw new LogTagsDataNotFoundException(exceptedData);
        }

        if (exceptedData.size() != actualData.size()) {
            throw new TagSizeNotEqualsException(exceptedData.size(), actualData.size());
        }
        for (int index = 0; index < exceptedData.size(); index++) {
            tagEquals(exceptedData.get(index), actualData.get(index));
        }
    }

    private static void tagEquals(LogTags.KeyValuePair excepted, LogTags.KeyValuePair actual) {
        try {
            keyValuePairEquals(excepted, actual);
        } catch (KeyValueNotEqualsException e) {
            throw new TagKeyNotEqualsException(excepted.getKey(), actual.getKey());
        } catch (ValueAssertFailedException e) {
            throw new TagValueNotEqualsException(excepted.getKey(), excepted.getValue(), actual.getValue());
        }
    }

    private static void keyValuePairEquals(LogTags.KeyValuePair excepted, LogTags.KeyValuePair actual) {
        if (!excepted.getKey().equals(actual.getKey())) {
            throw new KeyValueNotEqualsException();
        }

        ExpressParser.parse(excepted.getValue()).assertValue("", actual.getValue());
    }

}
