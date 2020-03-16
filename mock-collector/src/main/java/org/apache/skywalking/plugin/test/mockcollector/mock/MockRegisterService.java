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

package org.apache.skywalking.plugin.test.mockcollector.mock;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.network.common.KeyIntValuePair;
import org.apache.skywalking.apm.network.register.v2.Endpoint;
import org.apache.skywalking.apm.network.register.v2.EndpointMapping;
import org.apache.skywalking.apm.network.register.v2.Endpoints;
import org.apache.skywalking.apm.network.register.v2.NetAddressMapping;
import org.apache.skywalking.apm.network.register.v2.NetAddresses;
import org.apache.skywalking.apm.network.register.v2.RegisterGrpc;
import org.apache.skywalking.apm.network.register.v2.ServiceInstance;
import org.apache.skywalking.apm.network.register.v2.ServiceInstanceRegisterMapping;
import org.apache.skywalking.apm.network.register.v2.ServiceInstances;
import org.apache.skywalking.apm.network.register.v2.ServiceRegisterMapping;
import org.apache.skywalking.apm.network.register.v2.Services;
import org.apache.skywalking.plugin.test.mockcollector.entity.RegistryItem;
import org.apache.skywalking.plugin.test.mockcollector.entity.ValidateData;

@Slf4j
public class MockRegisterService extends RegisterGrpc.RegisterImplBase {

    @Override
    public void doEndpointRegister(Endpoints request, StreamObserver<EndpointMapping> responseObserver) {
        for (Endpoint endpoint : request.getEndpointsList()) {
            ValidateData.INSTANCE.getRegistryItem()
                                 .registryOperationName(new RegistryItem.OperationName(endpoint.getServiceId(), endpoint
                                     .getEndpointName()));
        }
        responseObserver.onNext(EndpointMapping.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void doNetworkAddressRegister(NetAddresses request, StreamObserver<NetAddressMapping> responseObserver) {
        responseObserver.onNext(NetAddressMapping.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void doServiceInstanceRegister(ServiceInstances request,
                                          StreamObserver<ServiceInstanceRegisterMapping> responseObserver) {
        if (request.getInstancesCount() <= 0) {
            responseObserver.onNext(ServiceInstanceRegisterMapping.getDefaultInstance());
            responseObserver.onCompleted();
            return;
        }

        for (ServiceInstance serviceInstance : request.getInstancesList()) {
            int instanceId = Sequences.INSTANCE_SEQUENCE.incrementAndGet();
            ValidateData.INSTANCE.getRegistryItem()
                                 .registryInstance(
                                     new RegistryItem.Instance(serviceInstance.getServiceId(), instanceId));

            responseObserver.onNext(ServiceInstanceRegisterMapping.newBuilder()
                                                                  .addServiceInstances(KeyIntValuePair.newBuilder()
                                                                                                      .setKey(
                                                                                                          serviceInstance
                                                                                                              .getInstanceUUID())
                                                                                                      .setValue(
                                                                                                          instanceId)
                                                                                                      .build())
                                                                  .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void doServiceRegister(Services request, StreamObserver<ServiceRegisterMapping> responseObserver) {
        log.debug("receive service register.");
        if (request.getServicesCount() <= 0) {
            log.warn("The service count is empty. return the default service register mapping");
            responseObserver.onNext(ServiceRegisterMapping.getDefaultInstance());
            responseObserver.onCompleted();
            return;
        }

        for (org.apache.skywalking.apm.network.register.v2.Service service : request.getServicesList()) {
            String serviceName = service.getServiceName();
            ServiceRegisterMapping.Builder builder = ServiceRegisterMapping.newBuilder();

            if (serviceName.startsWith("localhost") || serviceName.startsWith("127.0.0.1")
                || serviceName.contains(":") || serviceName.contains("/")) {
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }

            Integer serviceId = Sequences.SERVICE_MAPPING.get(serviceName);
            if (serviceId == null) {
                serviceId = Sequences.ENDPOINT_SEQUENCE.incrementAndGet();
                Sequences.SERVICE_MAPPING.put(serviceName, serviceId);
                ValidateData.INSTANCE.getRegistryItem()
                                     .registryService(new RegistryItem.Service(serviceName, serviceId));
            }

            builder.addServices(KeyIntValuePair.newBuilder().setKey(serviceName).setValue(serviceId).build());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }
}
