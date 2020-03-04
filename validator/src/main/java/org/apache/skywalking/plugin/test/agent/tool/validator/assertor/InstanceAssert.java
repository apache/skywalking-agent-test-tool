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

import java.util.List;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.exception.RegistryInstanceOfServiceNotFoundException;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.exception.RegistryInstanceSizeNotEqualsException;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.exception.ValueAssertFailedException;
import org.apache.skywalking.plugin.test.agent.tool.validator.entity.RegistryInstance;

public class InstanceAssert {
    public static void assertEquals(List<RegistryInstance> expected, List<RegistryInstance> actual) {

        if (expected == null) {
            return;
        }

        for (RegistryInstance instance : expected) {
            RegistryInstance actualInstance = getMatchService(actual, instance);
            try {
                ExpressParser.parse(actualInstance.expressValue())
                             .assertValue(
                                 String.format("The registry instance of %s", instance.serviceName()),
                                 actualInstance
                                     .expressValue()
                             );
            } catch (ValueAssertFailedException e) {
                throw new RegistryInstanceSizeNotEqualsException(instance.serviceName(), e);
            }
        }
    }

    private static RegistryInstance getMatchService(List<RegistryInstance> actual, RegistryInstance service) {
        for (RegistryInstance registryInstance : actual) {
            if (registryInstance.serviceName().equals(service.serviceName())) {
                return registryInstance;
            }
        }
        throw new RegistryInstanceOfServiceNotFoundException(service.serviceName());
    }
}
