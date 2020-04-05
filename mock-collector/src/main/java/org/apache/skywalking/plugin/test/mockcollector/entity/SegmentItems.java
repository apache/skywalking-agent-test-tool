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

import java.util.HashMap;
import java.util.Map;

public class SegmentItems {
    private Map<String, SegmentItem> segmentItems;

    public SegmentItems() {
        this.segmentItems = new HashMap<>();
    }

    public SegmentItems addSegmentItem(String serviceName, Segment segment) {
        SegmentItem segmentItem = segmentItems.get(serviceName);
        if (segmentItem == null) {
            segmentItem = new SegmentItem(serviceName);
            segmentItems.put(serviceName, segmentItem);
        }
        segmentItem.addSegments(segment);
        return this;
    }

    public Map<String, SegmentItem> getSegmentItems() {
        return segmentItems;
    }
}
