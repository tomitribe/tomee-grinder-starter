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
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.superbiz;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;

import java.net.URI;

public class ColorServicePerf extends Assert {

    public static void main(String[] args) throws Exception {
        final ColorServicePerf perf = new ColorServicePerf("http://localhost:8080/tomee-rest-arquillian-1.0-SNAPSHOT/");

        perf.getColorObject();
        perf.postGreen();
        perf.getGreen();
    }

    private final CloseableHttpClient httpClient;
    private final URI webappUri;

    public ColorServicePerf(String webappUrl) {
        webappUri = URI.create(webappUrl);
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(30);
        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
    }

    public void postGreen() throws Exception {
        // POST
        {
            final HttpPost post = new HttpPost(webappUri.resolve("color/green"));
            try (CloseableHttpResponse response = httpClient.execute(post)) {
                assertEquals(204, response.getStatusLine().getStatusCode());
            }

        }
    }

    public void getGreen() throws Exception {
        // GET
        {
            final HttpGet get = new HttpGet(webappUri.resolve("color"));
            try (final CloseableHttpResponse response = httpClient.execute(get)) {
                final String content = EntityUtils.toString(response.getEntity());
                assertEquals(200, response.getStatusLine().getStatusCode());
                assertEquals("green", content);
            }
        }

    }

    public void getColorObject() throws Exception {
        final HttpGet get = new HttpGet(webappUri.resolve("color/object"));
        get.setHeader("Accept", "application/json");
        try (final CloseableHttpResponse response = httpClient.execute(get)) {
            final String content = EntityUtils.toString(response.getEntity());
            assertEquals(200, response.getStatusLine().getStatusCode());
            assertNotNull(content);
            assertTrue(content.contains("orange"));
        }
    }

}
