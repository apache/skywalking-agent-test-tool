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
/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package org.apache.skywalking.plugin.test.mockcollector.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.skywalking.apm.network.common.v3.KeyStringValuePair;
import org.apache.skywalking.apm.network.language.agent.v3.SegmentReference;

@Builder
@ToString
@AllArgsConstructor
public class Span {
    private String operationName;
    private int operationId;
    private int parentSpanId;
    private int spanId;
    private String spanLayer;
    private long startTime;
    private long endTime;
    private int componentId;
    private boolean isError;
    private String spanType;
    private String peer;
    private boolean skipAnalysis;
    private List<KeyValuePair> tags = new ArrayList<>();
    private List<LogEvent> logs = new ArrayList<>();
    private List<SegmentRef> refs = new ArrayList<>();

    public static class LogEvent {
        private List<KeyValuePair> logEvent;

        public LogEvent() {
            this.logEvent = new ArrayList<>();
        }
    }

    public static class SpanBuilder {
        public SpanBuilder logEvent(List<KeyStringValuePair> eventMessage) {
            if (logs == null) {
                logs = new ArrayList<>();
            }

            LogEvent event = new LogEvent();
            for (KeyStringValuePair value : eventMessage) {
                event.logEvent.add(new KeyValuePair(value.getKey(), value.getValue()));
            }
            logs.add(event);
            return this;
        }

        public SpanBuilder tags(String key, String value) {
            if (tags == null) {
                tags = new ArrayList<>();
            }

            tags.add(new KeyValuePair(key, value));
            return this;
        }

        public SpanBuilder ref(SegmentRef segmentRefBuilder) {
            if (refs == null) {
                refs = new ArrayList<>();
            }

            refs.add(segmentRefBuilder);
            return this;
        }

    }

    @Getter
    @AllArgsConstructor
    public static class KeyValuePair {
        private String key;
        private String value;
    }

    @ToString
    @Getter
    public static class SegmentRef {
        private String parentEndpoint;
        private String networkAddress;
        private String refType;
        private int parentSpanId;
        private String parentTraceSegmentId;
        private String parentServiceInstance;
        private String parentService;
        private String traceId;

        public SegmentRef(SegmentReference ref) {
            this.parentTraceSegmentId = ref.getParentTraceSegmentId();
            this.refType = ref.getRefType().toString();
            this.parentSpanId = ref.getParentSpanId();
            this.parentEndpoint = ref.getParentEndpoint();
            this.parentService = ref.getParentService();
            this.parentServiceInstance = ref.getParentServiceInstance();
            this.parentTraceSegmentId = ref.getParentTraceSegmentId();
            this.networkAddress = ref.getNetworkAddressUsedAtPeer();
            this.parentSpanId = ref.getParentSpanId();
            this.traceId = ref.getTraceId();
        }

    }
}
