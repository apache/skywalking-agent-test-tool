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

package org.apache.skywalking.plugin.test.agent.tool.validator.entity;

import java.util.ArrayList;
import java.util.List;

public class DataForRead implements Data {
    private List<SegmentItemForRead> segmentItems;
    private List<MeterItem> meterItems;
    private List<LogItem> logItems;

    public List<SegmentItemForRead> getSegmentItems() {
        return segmentItems;
    }

    public void setSegmentItems(List<SegmentItemForRead> segmentItems) {
        this.segmentItems = segmentItems;
    }

    public List<MeterItem> getMeterItems() {
        return meterItems;
    }

    public void setMeterItems(List<MeterItem> meterItems) {
        this.meterItems = meterItems;
    }

    public List<LogItem> getLogItems() {
        return logItems;
    }

    public void setLogItems(List<LogItem> logItems) {
        this.logItems = logItems;
    }

    @Override
    public List<SegmentItem> segmentItems() {
        if (this.segmentItems == null) {
            return null;
        }

        return new ArrayList<>(this.segmentItems);
    }

    @Override
    public List<MeterItem> meterItems() {
        if (this.meterItems == null) {
            return null;
        }

        return new ArrayList<>(this.meterItems);
    }

    @Override
    public List<LogItem> logItems() {
        if (this.logItems == null) {
            return null;
        }

        return new ArrayList<>(this.logItems);
    }

}
