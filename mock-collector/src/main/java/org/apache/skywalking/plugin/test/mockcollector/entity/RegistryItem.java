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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryItem {
    /**
     * serviceName, serviceId
     */
    private final Map<String, Integer> services;
    /**
     * serviceName, operationName
     */
    private final Map<String, Set<String>> operationNames;
    /**
     * serviceName, instanceId
     */
    private final Map<String, List<Integer>> instanceMapping;
    /**
     * serviceName, count
     */
    private final Map<String, Integer> heartBeats;

    public RegistryItem() {
        services = new ConcurrentHashMap<>();
        operationNames = new ConcurrentHashMap<>();
        instanceMapping = new ConcurrentHashMap<>();
        heartBeats = new ConcurrentHashMap<>();
    }

    public void registryApplication(Service service) {
        services.putIfAbsent(service.serviceName, service.serviceId);
    }

    public void registryOperationName(OperationName operationName) {
        String serviceName = findServiceName(operationName.serviceId);
        Set<String> operationNameList = operationNames.get(serviceName);
        if (operationNameList == null) {
            operationNameList = new HashSet<>();
            operationNames.put(serviceName, operationNameList);
        }
        operationNameList.add(operationName.operationName);
    }

    public void registryInstance(Instance instance) {
        String serviceName = findServiceName(instance.serviceId);
        List<Integer> instances = instanceMapping.get(serviceName);
        if (instances == null) {
            instances = new ArrayList<>();
            instanceMapping.put(serviceName, instances);
        }

        if (!instances.contains(instance)) {
            instances.add(instance.instanceId);
        }
    }

    public String findServiceName(int id) {
        for (Map.Entry<String, Integer> entry : services.entrySet()) {
            if (entry.getValue() == id) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("Cannot found the name of serviceId [" + id + "].");
    }

    public void registryHeartBeat(HeartBeat heartBeat) {
        for (Map.Entry<String, List<Integer>> entry : instanceMapping.entrySet()) {
            if (entry.getValue().contains(heartBeat.instanceID)) {
                Integer count = heartBeats.get(entry.getKey());
                if (count != null) {
                    heartBeats.put(entry.getKey(), 0);
                    heartBeats.put(entry.getKey(), count++);
                }
            }
        }
    }

    public static class OperationName {
        int serviceId;
        String operationName;

        public OperationName(int serviceId, String operationName) {
            this.serviceId = serviceId;
            this.operationName = operationName;
        }
    }

    public static class Service {
        String serviceName;
        int serviceId;

        public Service(String serviceName, int serviceId) {
            this.serviceName = serviceName;
            this.serviceId = serviceId;
        }
    }

    public static class Instance {
        int serviceId;
        int instanceId;

        public Instance(int serviceId, int instanceId) {
            this.serviceId = serviceId;
            this.instanceId = instanceId;
        }
    }

    public static class HeartBeat {
        private int instanceID;

        public HeartBeat(int instanceID) {
            this.instanceID = instanceID;
        }
    }

    public Map<String, Integer> getServices() {
        return services;
    }

    public Map<String, Set<String>> getOperationNames() {
        return operationNames;
    }

    public Map<String, List<Integer>> getInstanceMapping() {
        return instanceMapping;
    }

    public Map<String, Integer> getHeartBeats() {
        return heartBeats;
    }
}
