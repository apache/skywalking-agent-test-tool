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

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.skywalking.apm.network.common.v3.KeyStringValuePair;
import org.apache.skywalking.apm.network.language.agent.v3.Log;
import org.apache.skywalking.apm.network.language.agent.v3.SegmentObject;
import org.apache.skywalking.apm.network.language.agent.v3.SegmentReference;
import org.apache.skywalking.apm.network.language.agent.v3.SpanObject;
import org.apache.skywalking.plugin.test.mockcollector.entity.Segment;
import org.apache.skywalking.plugin.test.mockcollector.entity.Span;
import org.apache.skywalking.plugin.test.mockcollector.entity.ValidateData;

public class TraceSegmentHandler {

    public static void parseSegment(SegmentObject segmentObject) {
        Segment.SegmentBuilder builder = Segment.builder();

        List<Span> spans = Lists.newArrayList();
        for (SpanObject spanObject : segmentObject.getSpansList()) {
            Span.SpanBuilder spanBuilder = Span.builder();
            spanBuilder.operationName(spanObject.getOperationName())
                       .parentSpanId(spanObject.getParentSpanId())
                       .spanId(spanObject.getSpanId())
                       .spanLayer(spanObject.getSpanLayer().name())
                       .startTime(spanObject.getStartTime())
                       .endTime(spanObject.getEndTime())
                       .componentId(spanObject.getComponentId())
                       .isError(spanObject.getIsError())
                       .spanType(spanObject.getSpanType().name())
                       .peer(spanObject.getPeer())
                       .skipAnalysis(spanObject.getSkipAnalysis());

            for (Log log : spanObject.getLogsList()) {
                spanBuilder.logEvent(log.getDataList());
            }
            for (KeyStringValuePair tags : spanObject.getTagsList()) {
                spanBuilder.tags(tags.getKey(), tags.getValue());
            }
            for (SegmentReference ref : spanObject.getRefsList()) {
                spanBuilder.ref(new Span.SegmentRef(ref));
            }
            spans.add(spanBuilder.build());
        }
        builder.segmentId(segmentObject.getTraceSegmentId()).spans(spans);

        ValidateData.INSTANCE.getSegmentItem()
                             .addSegmentItem(segmentObject.getService(), builder.build());
    }

}
