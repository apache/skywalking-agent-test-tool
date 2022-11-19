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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExpressOperator {
    NULL("null", false),
    NOT_NULL("not null", false),
    NOT_BLANK("not blank", false),
    NOT_EQUALS("nq ",true),
    EQUALS("eq ",true),
    GREAT_THAN("gt ",true),
    GREAT_EQUAL("ge ",true),
    START_WITH("start with ",true),
    END_WITH("end with ",true),
    ;
    private final String prefix;
    private final boolean hasValue;

    public static ExpressOperator parse(String express) {
        if (express == null) {
            return null;
        }

        String expressTrim = express.trim();
        for (ExpressOperator operator : ExpressOperator.values()) {
            if (expressTrim.startsWith(operator.getPrefix())) {
                return operator;
            }
        }

        return null;
    }

    public static String getExpectedValue(ExpressOperator operator, String express) {
        if (operator.hasValue) {
            return express.substring(operator.prefix.length()).trim();
        }
        return null;
    }
}
