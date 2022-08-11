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

public class ValidateData {
    public static ValidateData INSTANCE = new ValidateData();
    private SegmentItems segmentItem;
    private MeterItems meterItems;
    private LogItems logItems;

    private  ValidateData() {
        segmentItem = new SegmentItems();
        meterItems = new MeterItems();
        logItems = new LogItems();
    }

    public synchronized SegmentItems getSegmentItem() {
        return segmentItem;
    }

    public synchronized void clearData() {
        INSTANCE.segmentItem = new SegmentItems();
        INSTANCE.meterItems = new MeterItems();
        INSTANCE.logItems = new LogItems();
    }

    public synchronized MeterItems getMeterItems() {
        return meterItems;
    }

    public synchronized LogItems getLogItems() {
        return logItems;
    }

}
