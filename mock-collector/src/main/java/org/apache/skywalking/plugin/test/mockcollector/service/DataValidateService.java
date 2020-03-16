/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.plugin.test.mockcollector.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.skywalking.plugin.test.agent.tool.validator.assertor.DataAssert;
import org.apache.skywalking.plugin.test.agent.tool.validator.entity.Data;
import org.apache.skywalking.plugin.test.mockcollector.entity.ValidateData;
import org.apache.skywalking.plugin.test.mockcollector.entity.ValidateDataSerializer;
import org.yaml.snakeyaml.Yaml;

public class DataValidateService extends HttpServlet {
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(
        ValidateData.class, new ValidateDataSerializer()
    ).create();

    public static final String SERVLET_PATH = "/dataValidate";

    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp) throws ServletException, IOException {

        final Yaml yaml = new Yaml();
        final String dump = yaml.dump(yaml.load(gson.toJson(ValidateData.INSTANCE)));
        ByteArrayInputStream actualData = new ByteArrayInputStream(dump.getBytes());

        PrintWriter writer = resp.getWriter();
        DataAssert.assertEquals(
            Data.Loader.loadData(req.getInputStream()),
            Data.Loader.loadData(actualData)
        );
        writer.write("success");
        resp.setStatus(200);
        writer.flush();
    }
}
