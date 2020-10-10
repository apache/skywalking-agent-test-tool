/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.skywalking.plugin.test.mockcollector.util;

import org.apache.skywalking.apm.network.language.agent.v3.Label;
import org.apache.skywalking.apm.network.language.agent.v3.MeterData;
import org.apache.skywalking.apm.network.language.agent.v3.MeterHistogram;
import org.apache.skywalking.apm.network.language.agent.v3.MeterSingleValue;
import org.apache.skywalking.plugin.test.mockcollector.entity.Meter;
import org.apache.skywalking.plugin.test.mockcollector.entity.MeterId;
import org.apache.skywalking.plugin.test.mockcollector.entity.ValidateData;

import java.util.List;
import java.util.stream.Collectors;

public class MeterHandler {

    public static Parser createParser() {
        return new Parser();
    }

    public static class Parser {
        private String serviceName;

        public void parse(MeterData meterData) {
            final Meter.MeterBuilder builder = Meter.builder();

            switch (meterData.getMetricCase()) {
                case SINGLEVALUE:
                    final MeterSingleValue singleValue = meterData.getSingleValue();
                    builder.meterId(MeterId.builder()
                        .name(singleValue.getName())
                        .tags(parseTags(singleValue.getLabelsList()))
                        .build());
                    builder.singleValue(singleValue.getValue());
                    break;
                case HISTOGRAM:
                    final MeterHistogram histogram = meterData.getHistogram();
                    builder.meterId(MeterId.builder()
                        .name(histogram.getName())
                        .tags(parseTags(histogram.getLabelsList()))
                        .build());
                    builder.histogram(histogram.getValuesList().stream()
                        .map(b -> new Meter.BucketAndValue(b.getBucket(), b.getCount())).collect(Collectors.toList()));
                    break;
                case METRIC_NOT_SET:
                    return;
            }

            final Meter meter = builder.build();
            if (meterData.getService() != null && meterData.getService().length() > 0) {
                serviceName = meterData.getService();
            }
            ValidateData.INSTANCE.getMeterItems().addMeter(serviceName, meter);
        }

        private List<MeterId.Tag> parseTags(List<Label> labels) {
            return labels.stream().map(l -> new MeterId.Tag(l.getName(), l.getValue())).collect(Collectors.toList());
        }
    }
}
