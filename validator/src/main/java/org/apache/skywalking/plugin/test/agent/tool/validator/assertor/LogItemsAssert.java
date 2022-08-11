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

import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.exception.ActualLogItemEmptyException;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.exception.LogItemNotFoundException;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.exception.LogsSizeNotEqualsException;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.exception.ValueAssertFailedException;
import org.apache.skywalking.plugin.test.agent.tool.validator.entity.LogItem;

import java.util.List;

@Slf4j
public class LogItemsAssert {

    public static void assertEquals(List<LogItem> expected, List<LogItem> actual) {
        if (expected == null) {
            log.info("ignore log items. because expected log item is null.");
            return;
        }

        for (LogItem item : expected) {
            LogItem actualLogItem = findLogItem(actual, item);
            try {
                assertLogSize(item.getLogSize(), actualLogItem.getLogSize());
            } catch (ValueAssertFailedException e) {
                throw new LogsSizeNotEqualsException(
                    item.getServiceName(), item.getLogSize(), actualLogItem.getLogSize());
            }
            LogAssert.assertEquals(item, actualLogItem);
        }
    }

    private static void assertLogSize(String expected, String actual) {
        if (expected == null) {
            return;
        }
        ExpressParser.parse(expected).assertValue("log size", actual);
    }

    private static LogItem findLogItem(List<LogItem> actual, LogItem expected) {
        if (actual == null) {
            throw new ActualLogItemEmptyException(expected);
        }

        for (LogItem logItem : actual) {
            if (expected.getServiceName().equals(logItem.getServiceName())) {
                return logItem;
            }
        }

        throw new LogItemNotFoundException(expected.getServiceName());
    }
}
