package com.certified.verityscanningOne.Utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.certified.verityscanningOne.beans.Responcebean;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

/**
 * Created by Nakul Sheth on 01-08-2016.
 */
public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    String TAG="JSONParser";

    static JSONObject jsonObject = null;

    // constructor
    public JSONParser() {

    }

    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {

        String s1;
        Responcebean responcebean = new Responcebean();
        JSONObject jObj = null;
        InputStream is = null;
        String json = null;
        try {
            JSONObject jsonObject_post=new JSONObject();
            if(params!=null)
            {
                if(params.size()!=0)
                {
                    for (int i = 0; i < params.size(); i++) {
                        NameValuePair nameValuePair=params.get(i);
                        jsonObject_post.put(nameValuePair.getName(),nameValuePair.getValue());
                    }
                }
            }


            Log.e("POSTED URL", url);
            Log.e("POSTED Paramerts", jsonObject_post.toString());
            URL urlm = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlm.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");


            StringEntity stringEntity = new StringEntity(jsonObject_post.toString());
            stringEntity.setContentType(new BasicHeader("Content-Type", "application/json"));


            conn.addRequestProperty("Content-length", stringEntity.getContentLength() + "");
            conn.addRequestProperty(stringEntity.getContentType().getName(), stringEntity.getContentType().getValue());


            OutputStream os = conn.getOutputStream();
            stringEntity.writeTo(conn.getOutputStream());
            os.close();
            conn.connect();


            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                responcebean.setJsonResponceContent(readStream(conn.getInputStream()));

                Log.e("Responce", responcebean.getJsonResponceContent());
                jObj=new JSONObject(responcebean.getJsonResponceContent());
                responcebean.setIsServiceSuccess(true);
            } else if (conn.getResponseCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                responcebean.setErrorMessage("The server encountered an unexpected condition which prevented it from fulfilling the request.");
                responcebean.setIsServiceSuccess(false);


            } else {
                responcebean.setIsServiceSuccess(false);
            }


        } catch (SocketTimeoutException e) {
            responcebean.setIsServiceSuccess(false);
            responcebean.setExceptionError(true);
            CommonMethods.printFirebaseLogcat(TAG, e, null);

            responcebean.setErrorMessage(e.getMessage());
        } catch (ConnectTimeoutException e) {
            responcebean.setIsServiceSuccess(false);
            responcebean.setExceptionError(true);
            CommonMethods.printFirebaseLogcat(TAG, e, null);

            responcebean.setErrorMessage(e.getMessage());
        } catch (IOException e) {
            responcebean.setIsServiceSuccess(false);
            responcebean.setExceptionError(true);
            CommonMethods.printFirebaseLogcat(TAG, e, null);

            responcebean.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            responcebean.setIsServiceSuccess(false);
            responcebean.setExceptionError(true);

            responcebean.setErrorMessage(e.getMessage());
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }


        // return JSON String
        return jObj;

    }



    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }



    public static byte[] convertFileToByteArray(File f) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    public JSONObject sentMIMEContent(String urlstr, JSONObject jsonobject, Bitmap bitmap, String file_path) {
        String responce = null;
        JSONObject jObj_responce = null;
        try {


            Log.e("POSTED URL", urlstr);
            Log.e("POSTED Paramerts", jsonobject.toString());
            URL url = new URL(urlstr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");

            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);


            if (bitmap != null) {

                File image_file = new File(file_path);
                byte[] data = convertFileToByteArray(image_file);
                ByteArrayBody bab = new ByteArrayBody(data, file_path);
                entity.addPart("productImage", bab);

            } else {

            }

            entity.addPart("json_content", new StringBody(jsonobject.toString()));

            conn.addRequestProperty("Content-length", entity.getContentLength() + "");
            conn.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());

            OutputStream os = conn.getOutputStream();
            entity.writeTo(conn.getOutputStream());

            os.close();
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                responce = readStream(conn.getInputStream());
                Log.e("responce ===>", responce);
                jObj_responce = new JSONObject(responce);

            }


        } catch (Exception
                e) {
            e.printStackTrace();
        }
        return jObj_responce;
    }
}