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

package org.apache.skywalking.plugin.test.mockcollector;

import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.local.LocalAddress;
import java.net.InetSocketAddress;
import org.apache.skywalking.plugin.test.mockcollector.mock.MockCLRMetricReportService;
import org.apache.skywalking.plugin.test.mockcollector.mock.MockJVMMetricReportService;
import org.apache.skywalking.plugin.test.mockcollector.mock.MockManagementService;
import org.apache.skywalking.plugin.test.mockcollector.mock.MockTraceSegmentService;
import org.apache.skywalking.plugin.test.mockcollector.mock.rest.MockManagementServiceKeepAliveHandler;
import org.apache.skywalking.plugin.test.mockcollector.mock.rest.MockManagementServiceReportPropertiesHandler;
import org.apache.skywalking.plugin.test.mockcollector.mock.rest.MockTraceSegmentListCollectServletHandler;
import org.apache.skywalking.plugin.test.mockcollector.mock.rest.MockTraceSegmentSingleCollectServletHandler;
import org.apache.skywalking.plugin.test.mockcollector.service.ClearReceiveDataService;
import org.apache.skywalking.plugin.test.mockcollector.service.DataValidateService;
import org.apache.skywalking.plugin.test.mockcollector.service.GrpcAddressHttpService;
import org.apache.skywalking.plugin.test.mockcollector.service.HealthCheckService;
import org.apache.skywalking.plugin.test.mockcollector.service.ReceiveDataService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class Main {
    public static void main(String[] args) throws Exception {
        // Mock GRPC Collector
        NettyServerBuilder.forAddress(LocalAddress.ANY)
                          .forPort(19876)
                          .maxConcurrentCallsPerConnection(12)
                          .addService(new MockCLRMetricReportService())
                          .addService(new MockJVMMetricReportService())
                          .addService(new MockManagementService())
                          .addService(new MockTraceSegmentService())
                          .build()
                          .start();

        Server jettyServer = new Server(new InetSocketAddress("0.0.0.0", Integer.valueOf(12800)));
        String contextPath = "/";
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletContextHandler.setContextPath(contextPath);

        // Collector service
        servletContextHandler.addServlet(HealthCheckService.class, HealthCheckService.SERVLET_PATH);
        servletContextHandler.addServlet(GrpcAddressHttpService.class, GrpcAddressHttpService.SERVLET_PATH);
        servletContextHandler.addServlet(DataValidateService.class, DataValidateService.SERVLET_PATH);
        servletContextHandler.addServlet(ReceiveDataService.class, ReceiveDataService.SERVLET_PATH);
        servletContextHandler.addServlet(ClearReceiveDataService.class, ClearReceiveDataService.SERVLET_PATH);

        // Mock Rest API collector
        servletContextHandler.addServlet(
            MockManagementServiceKeepAliveHandler.class,
            MockManagementServiceKeepAliveHandler.SERVLET_PATH
        );
        servletContextHandler.addServlet(
            MockManagementServiceReportPropertiesHandler.class,
            MockManagementServiceReportPropertiesHandler.SERVLET_PATH
        );
        servletContextHandler.addServlet(
            MockTraceSegmentListCollectServletHandler.class,
            MockTraceSegmentListCollectServletHandler.SERVLET_PATH
        );
        servletContextHandler.addServlet(
            MockTraceSegmentSingleCollectServletHandler.class,
            MockTraceSegmentSingleCollectServletHandler.SERVLET_PATH
        );

        jettyServer.setHandler(servletContextHandler);
        jettyServer.start();
    }
}
