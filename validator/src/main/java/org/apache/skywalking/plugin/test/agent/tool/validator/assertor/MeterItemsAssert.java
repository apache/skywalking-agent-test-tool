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

package org.apache.skywalking.plugin.test.agent.tool.validator.assertor;

import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.exception.MeterItemNotFoundException;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.exception.MeterSizeNotEqualsException;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.exception.ValueAssertFailedException;
import org.apache.skywalking.plugin.test.agent.tool.validator.entity.MeterItem;

import java.util.List;

@Slf4j
public class MeterItemsAssert {

    public static void assertEquals(List<MeterItem> excepted, List<MeterItem> actual) {
        for (MeterItem item : excepted) {
            MeterItem actualMeterItem = findMeterItem(actual, item);
            try {
                assertMeterSize(item.getMeterSize(), actualMeterItem.getMeterSize());
            } catch (ValueAssertFailedException e) {
                throw new MeterSizeNotEqualsException(
                    item.getServiceName(), item.getMeterSize(), actualMeterItem.getMeterSize());
            }

            MeterAssert.assertEquals(item, actualMeterItem);
        }
    }

    private static void assertMeterSize(String expected, String actual) {
        if (expected == null) {
            return;
        }
        ExpressParser.parse(expected).assertValue("meter size", actual);
    }

    /**
     * Find same service meter item
     */
    private static MeterItem findMeterItem(List<MeterItem> actual, MeterItem expected) {
        for (MeterItem segmentItem : actual) {
            if (expected.getServiceName().equals(segmentItem.getServiceName())) {
                return segmentItem;
            }
        }

        throw new MeterItemNotFoundException(expected.getServiceName());
    }
}
