/*
 * Copyright 2016 TUM Technische Universität München
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package de.tumitfahrer.util.notification;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Class representing an HTTP request
 */
public class HttpRequest {
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_PROJECT_ID = "project_id";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static final String CONTENT_TYPE_FORM_ENCODED = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON = "application/json";

    private HashMap<String, String> mHeaders = new HashMap<String, String>();
    private int responseCode;
    private String responseBody;

    /**
     * Add a request header
     *
     * @param name  the header's name
     * @param value the header's value
     */
    public void setHeader(String name, String value) {
        this.mHeaders.put(name, value);
    }

    /**
     * @return this request's response code
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @return this request's response body
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * Post the request
     *
     * @param url         where to post to
     * @param requestBody the body of the request
     * @throws IOException
     */
    public void doPost(String url, String requestBody) throws IOException {


        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setFixedLengthStreamingMode(requestBody.getBytes().length);
        conn.setRequestMethod("POST");

        for (Map.Entry<String, String> s : mHeaders.entrySet()) {
            conn.setRequestProperty(s.getKey(), s.getValue());
        }
        OutputStream out = null;
        try {
            out = conn.getOutputStream();
            out.write(requestBody.getBytes());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
        }

        responseCode = conn.getResponseCode();

        InputStream inputStream = null;
        try {
            if (responseCode == 200) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }
            responseBody = getString(inputStream);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
        }


        conn.disconnect();
    }

    /**
     * Convenience method to convert an InputStream to a String.
     * <p>
     * If the stream ends in a newline character, it will be stripped.
     * <p>
     * If the stream is {@literal null}, returns an empty string.
     */
    private String getString(InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(stream));
        StringBuilder content = new StringBuilder();
        String newLine;
        do {
            newLine = reader.readLine();
            if (newLine != null) {
                content.append(newLine).append('\n');
            }
        } while (newLine != null);
        if (content.length() > 0) {
            // strip last newline
            content.setLength(content.length() - 1);
        }
        return content.toString();
    }
}