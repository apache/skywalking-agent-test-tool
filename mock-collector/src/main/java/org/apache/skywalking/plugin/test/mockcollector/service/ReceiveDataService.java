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

package org.apache.skywalking.plugin.test.mockcollector.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.skywalking.plugin.test.mockcollector.entity.ValidateData;
import org.apache.skywalking.plugin.test.mockcollector.entity.ValidateDataSerializer;
import org.eclipse.jetty.http.MimeTypes.Type;
import org.yaml.snakeyaml.Yaml;

public class ReceiveDataService extends HttpServlet {
    public static final String SERVLET_PATH = "/receiveData";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(Type.APPLICATION_JSON.asString());
        resp.setCharacterEncoding(Type.APPLICATION_JSON.getCharsetString());
        resp.setStatus(HttpServletResponse.SC_OK);
        Gson gson = new GsonBuilder().registerTypeAdapter(ValidateData.class, new ValidateDataSerializer()).create();

        Yaml yaml = new Yaml();
        Writer out = resp.getWriter();
        out.write(yaml.dump(yaml.load(gson.toJson(ValidateData.INSTANCE))));
        out.flush();
        out.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(Type.APPLICATION_JSON.asString());
        resp.setCharacterEncoding(Type.APPLICATION_JSON.getCharsetString());
        resp.setStatus(HttpServletResponse.SC_OK);
        ValidateData.INSTANCE.clearData();
        Writer out = resp.getWriter();
        out.flush();
        out.close();
    }
}
