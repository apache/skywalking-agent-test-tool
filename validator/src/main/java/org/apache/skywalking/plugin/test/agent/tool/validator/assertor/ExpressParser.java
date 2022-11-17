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

import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.element.ElementAssertor;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.element.EqualsAssertor;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.element.EndWithAssertor;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.element.GreatThanAssertor;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.element.GreatEqualAssertor;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.element.NoopAssertor;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.element.NotBlankAssertor;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.element.NotEqualsAssertor;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.element.NotNullAssertor;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.element.NullAssertor;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.element.StartWithAssertor;

public class ExpressParser {
    public static ElementAssertor parse(String express) {
        if (express == null) {
            return new NoopAssertor();
        }

        ExpressOperator expressOperator = ExpressOperator.parse(express);
        if (expressOperator == null) {
            return new EqualsAssertor(express);
        }

        String expectedValue = ExpressOperator.getExpectedValue(expressOperator, express);

        switch (expressOperator) {
            case NULL:
                return new NullAssertor();
            case NOT_NULL:
                return new NotNullAssertor();
            case NOT_BLANK:
                return new NotBlankAssertor();
            case EQUALS:
                return new EqualsAssertor(expectedValue);
            case NOT_EQUALS:
                return new NotEqualsAssertor(expectedValue);
            case GREAT_THAN:
                return new GreatThanAssertor(expectedValue);
            case GREAT_EQUAL:
                return new GreatEqualAssertor(expectedValue);
            case START_WITH:
                return new StartWithAssertor(expectedValue);
            case END_WITH:
                return new EndWithAssertor(expectedValue);
            default:
                return new EqualsAssertor(express);
        }
    }
}
