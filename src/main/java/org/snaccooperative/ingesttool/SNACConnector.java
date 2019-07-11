package org.snaccooperative.ingesttool;

import org.json.JSONException;
import org.json.JSONObject;
import org.snaccooperative.data.AbstractData;
import org.snaccooperative.data.Constellation;
import org.snaccooperative.data.Resource;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SNACConnector {

    private URL url = null;

    private String apiKey = null;

    private String lastMessage = null;

    public SNACConnector() {
        try {
            url = new URL("http://snac-dev.iath.virginia.edu/api/");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public SNACConnector(String key) {
        this();
        apiKey = key;

    }

    private JSONObject runQuery(String query) {

        try {

            System.err.println("SENDING QUERY \n" + query + "\n\n");
            HttpURLConnection httpcon = (HttpURLConnection) (url.openConnection());
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestMethod("PUT");
            httpcon.connect();


            // Write the query to the RestAPI
            byte[] outputBytes = query.getBytes("UTF-8");
            OutputStream os = httpcon.getOutputStream();
            os.write(outputBytes);
            os.close();

            // Read the response from the RestAPI
            String resultStr = null;
            InputStream in = new BufferedInputStream(httpcon.getInputStream());
            byte[] inputBytes = in.readAllBytes();
            resultStr = new String(inputBytes, StandardCharsets.UTF_8);
            in.close();

            lastMessage = resultStr;
            System.err.println("NEW RESPONSE \n" + lastMessage + "\n\n");

            JSONObject resultObj = null;
            resultObj = new JSONObject(resultStr);

            // Close the connection
            httpcon.disconnect();

            return resultObj;
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public Constellation writePublishConstellation(Constellation c) {

        // Insert Command
        String query = "{ \"command\" : \"insert_constellation\",\n" +
                "\"apikey\" : \"" + apiKey + "\",\n" +
                "\"constellation\" : " + Constellation.toJSON(c) + ",\n" +
                "\"message\" : \"SNAC Ingest Tool\"}";

        JSONObject resultObj = runQuery(query);

        if (resultObj == null)
            return null;

        if (resultObj.has("result")) {
            String result = resultObj.getString("result");
            if (result.equals("success")) {
                Constellation constellation = null;
                constellation = Constellation.fromJSON(resultObj.getJSONObject("constellation").toString());

                Constellation simple = new Constellation();
                simple.setID(constellation.getID());
                simple.setVersion(constellation.getVersion());

                // Publish Command
                query = "{ \"command\" : \"publish_constellation\",\n" +
                        "\"apikey\" : \"" + apiKey + "\",\n" +
                        "\"constellation\" : " + Constellation.toJSON(simple) + ",\n" +
                        "\"message\" : \"SNAC Ingest Tool Publish\"}";

                JSONObject pubResultObj = runQuery(query);

                if (pubResultObj.has("result")) {
                    String pubResult = pubResultObj.getString("result");
                    if (result.equals("success")) {
                        Constellation published = Constellation.fromJSON(pubResultObj.getJSONObject("constellation").toString());

                        constellation.setVersion(published.getVersion());
                        return constellation;
                    }
                }
            }
        }
        return null;
    }

    public Resource writeResource(Resource r) {

        r.setOperation(AbstractData.OPERATION_INSERT);

        // Insert Command
        String query = "{ \"command\" : \"insert_resource\",\n" +
                "\"apikey\" : \"" + apiKey + "\",\n" +
                "\"resource\" : " + Resource.toJSON(r) + ",\n" +
                "\"message\" : \"SNAC Ingest Tool\"}";

        JSONObject resultObj = runQuery(query);

        if (resultObj == null)
            return null;

        if (resultObj.has("result")) {
            String result = resultObj.getString("result");
            if (result.equals("success")) {
                Resource written = Resource.fromJSON(resultObj.getJSONObject("resource").toString());
                r.setID(written.getID());
                r.setVersion(written.getVersion());
                return r;
            }
        }

        return null;
    }

    public String getLastServerMessage() {
        return lastMessage;
    }

}
