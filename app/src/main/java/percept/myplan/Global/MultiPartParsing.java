package percept.myplan.Global;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import percept.myplan.Interfaces.AsyncTaskCompletedListener;
import percept.myplan.R;

/**
 * Created by percept on 16/8/16.
 */
public class MultiPartParsing {

    private Context context;
    private HashMap<String, String> map;
    private AsyncTaskCompletedListener completedListener;
    private General.PHPServices servicesName;

    public MultiPartParsing(Context _context, HashMap<String, String> _map,
                            General.PHPServices servicesName, AsyncTaskCompletedListener _completedListener) {
        this.context = _context;
        this.map = _map;
        this.completedListener = _completedListener;
        this.servicesName = servicesName;
        new MultiPartAsyncTask().execute();
    }

    class MultiPartAsyncTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            String _strURL = context.getResources().getString(R.string.server_url) + new General().getServiceName(servicesName);
            HttpPost httppost = new HttpPost(_strURL);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

//                if (!FILE_PATH.equals("")) {
//                    File sourceFile = new File(FILE_PATH);

                // Adding file data to http body

                for (String key : map.keySet()) {
                    Log.d("Multi-part params",key+"->"+map.get(key));
                    if (key.contains("image") || key.contains("video") ||
                            key.contains("audio") || key.contains("cover") || key.contains("media")) {
                        if (key.equals("media_title")) {
                            entity.addPart(key, new StringBody(map.get(key)));
                        } else if (Pattern.matches("[0-9]+", map.get(key)))
                            entity.addPart(key, new StringBody(map.get(key)));
                        else if (!TextUtils.isEmpty(map.get(key))) {
                            File _f = new File(map.get(key));
                            entity.addPart(key, new FileBody(_f));
                        }
                    } else
                        entity.addPart(key, new StringBody(map.get(key)));
                }


//                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                long totalLength = entity.getContentLength();
                System.out.println("TotalLength : " + totalLength);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (completedListener != null)
                completedListener.onTaskCompleted(response);
        }
    }
}
