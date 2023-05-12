package FCM;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.medca.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMAppointmentNotification {

    String token;
    String title;
    String body;
    Context mContext;
    Activity mActivity;

    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";

    public FCMAppointmentNotification(String token, String title, String body, Context mContext, Activity mActivity) {
        this.token = token;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public void sendNotification(){
        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try{
            mainObj.put("to", token);


            // Data Object
            JSONObject dataObj = new JSONObject();
            dataObj.put("dataFrom", "FcmAppointmentNotification");

            // Notification Object
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("icon", R.drawable.alaram);

            mainObj.put("notification",notiObject);
            mainObj.put("data", dataObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("send_fcm", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("send_fcm", error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + "AAAAFskYIOw:APA91bEQSJSUynpdQFcAGyYz2eGPQKyD-ZRL2GM_VKeayQc-bfaX0IVHTb-vuvjBK-qnXjWx09fqN1iTtSOJKlieHKuUX-449p36IDL211bTttKEqCAlC54BkpAPKecFtdWvw7PLHOiN");
                    return header;
                }
            };
            requestQueue.add(request);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
