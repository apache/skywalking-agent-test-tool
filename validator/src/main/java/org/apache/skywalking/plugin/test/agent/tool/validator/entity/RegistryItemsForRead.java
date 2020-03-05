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
import java.util.Map;

public class RegistryItemsForRead implements RegistryItems {
    private List<Map<String, String>> services;
    private List<Map<String, String>> instances;
    private List<Map<String, List<String>>> operationNames;

    public List<Map<String, String>> getServices() {
        return services;
    }

    public void setServices(List<Map<String, String>> services) {
        this.services = services;
    }

    public List<Map<String, String>> getInstances() {
        return instances;
    }

    public void setInstances(List<Map<String, String>> instances) {
        this.instances = instances;
    }

    public List<Map<String, List<String>>> getOperationNames() {
        return operationNames;
    }

    public void setOperationNames(List<Map<String, List<String>>> operationNames) {
        this.operationNames = operationNames;
    }

    @Override
    public List<RegistryService> services() {
        if (this.services == null) {
            return null;
        }

        List<RegistryService> registryServices = new ArrayList<>();
        for (Map<String, String> registryService : services) {
            String serviceName = new ArrayList<String>(registryService.keySet()).get(0);
            String express = String.valueOf(registryService.get(serviceName));
            registryServices.add(new RegistryService.Impl(serviceName, express));
        }
        return registryServices;
    }

    @Override
    public List<RegistryInstance> instances() {
        if (this.instances == null) {
            return null;
        }

        List<RegistryInstance> registryInstances = new ArrayList<>();
        instances.forEach((registryInstance) -> {
            String serviceName = new ArrayList<String>(registryInstance.keySet()).get(0);
            String express = String.valueOf(registryInstance.get(serviceName));
            registryInstances.add(new RegistryInstance.Impl(serviceName, express));
        });
        return registryInstances;
    }

    @Override
    public List<RegistryOperationName> operationNames() {
        if (this.operationNames == null) {
            return null;
        }

        List<RegistryOperationName> registryOperationNames = new ArrayList<>();
        operationNames.forEach((registryInstance) -> {
            String serviceName = new ArrayList<String>(registryInstance.keySet()).get(0);
            List<String> express = registryInstance.get(serviceName);
            registryOperationNames.add(new RegistryOperationName.Impl(serviceName, express));
        });
        return registryOperationNames;
    }
}
