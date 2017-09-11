package com.certified.verityscanningOne.Utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.certified.verityscanningOne.R;
import com.certified.verityscanningOne.beans.ActivitiesBean;
import com.certified.verityscanningOne.beans.CommentBean;
import com.certified.verityscanningOne.beans.HomeBean;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class GlobleInterfaceImple<T> implements GlobleInterface<T> {

    static JSONObject jsonObject = null;

    @Override
    public boolean emailValidator(String email) {
        // TODO Auto-generated method stub

        Matcher matcher = null;
        try {
            Pattern pattern;
            final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return matcher != null ? matcher.matches() : false;
    }

    @Override
    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        // Making HTTP request


        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON ===>", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    @Override
    public JSONObject getJSONFromUrlWithoutParams(String url) {
        InputStream is = null;
        String json = "";
        JSONObject jsonObject = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is != null ? is : null, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);

            }

            is.close();
            json = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // try parse the string to a JSON object
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // return JSON String
        return jsonObject;

    }

    public JSONObject getJSONFromUrlForDirectImageUploading(String url,
                                                            MultipartEntity reqEntity) {
        HttpEntity resEntity;

        try {
			/*HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(
					CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			HttpPost post = new HttpPost(url);

			post.setEntity(reqEntity);*//*
			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();
			final String response_str = EntityUtils.toString(resEntity);
			if (resEntity != null) {
				Log.i("RESPONSE", response_str);
				jsonObject = new JSONObject(response_str);
			}*/
        } catch (Exception ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
        return jsonObject;

    }


    // custome toast message method
    public void customTostDialog(Activity mActivity, String message) {

        try {
            View layout = mActivity.getLayoutInflater().inflate(
                    R.layout.custome_toast_dialog_screen, null);

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(message);
            Toast toast = new Toast(mActivity);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @Override
    public void loadNewIntent(Activity activity, Class<T> to_screen) {
        // TODO Auto-generated method stub

        Intent intent = new Intent(activity, to_screen);
        activity.startActivity(intent);
    }



    private void setUpActivityDetails(ActivitiesBean activitiesBean_local,
                                      JSONObject jsonObject_local) {
        // TODO Auto-generated method stub
        try {

            activitiesBean_local.setPostType(jsonObject_local
                    .getString(JSONConstants.ACTIVITY_STATUSTYPE));

            activitiesBean_local.setActivity_ID(jsonObject_local
                    .getString(JSONConstants.ACTIVITY_MAINID));

            activitiesBean_local
                    .setLoggedUserAvtar(jsonObject_local
                            .getString(JSONConstants.ACTIVITY_USER_AVTAR_ORIGINAL));

            activitiesBean_local.setActivity_username(jsonObject_local
                    .getString(JSONConstants.ACTIVITY_USERNAME));

            activitiesBean_local.setPostCreated(jsonObject_local
                    .getString(JSONConstants.ACTIVITY_CRAEATED));

            activitiesBean_local.setPostedContentText(jsonObject_local
                    .getString(JSONConstants.ACTIVITY_TITLE));

            activitiesBean_local.setLike(jsonObject_local
                    .getString(JSONConstants.ACTIVITY_LIKE));

            activitiesBean_local.setVideoThumnail(jsonObject_local
                    .getString(JSONConstants.ACTIVITY_VIDEO_IMAGE));

            activitiesBean_local.setVideoDescription(jsonObject_local
                    .getString(JSONConstants.ACTIVITY_VIDEO_DESC));

            activitiesBean_local.setVideoURL(jsonObject_local
                    .getString(JSONConstants.ACTIVITY_VIDEOPATH));

            activitiesBean_local
                    .setLoggedUserAvtar(jsonObject_local
                            .getString(JSONConstants.ACTIVITY_USER_AVTAR_ORIGINAL));

            activitiesBean_local
                    .setPostedImageThumbBig((jsonObject_local
                            .getString(JSONConstants.ACTIVITY_ORIGINALPHOTO_BIG)));

            activitiesBean_local
                    .setPostedImageThumbSmall((jsonObject_local
                            .getString(JSONConstants.ACTIVITY_ORIGINALPHOTO_SMALL)));

            activitiesBean_local
                    .setLoggedUserAvtar(jsonObject_local
                            .getString(JSONConstants.ACTIVITY_USER_AVTAR_ORIGINAL));

            activitiesBean_local.setCommentCount(jsonObject_local
                    .getInt(JSONConstants.ACTIVITY_COMMENTSCOUNT));

            activitiesBean_local.setIsliked(jsonObject_local
                    .getString(JSONConstants.ACTIVITY_ISLIKE));
            JSONArray jsonArray_comments = jsonObject_local
                    .getJSONArray(JSONConstants.ACTIVITY_COMMENTARRAY_MAIN);

            ArrayList<CommentBean> arrayListCommentBeans = new ArrayList<CommentBean>();
            for (int i = 0; i < jsonArray_comments.length(); i++) {

                JSONObject jsonObject_comment = jsonArray_comments
                        .getJSONObject(i);

                CommentBean commentBean = new CommentBean();
                commentBean
                        .setCommentContent((jsonObject_comment
                                .getString(JSONConstants.ACTIVITY_COMMENT_COMMENT)));

                commentBean
                        .setCommentPostedDate((jsonObject_comment
                                .getString(JSONConstants.ACTIVITY_COMMENT_CREATEDDATE)));

                commentBean
                        .setCommentPostedBy((jsonObject_comment
                                .getString(JSONConstants.ACTIVITY_COMMENT_POST_BY)));

                arrayListCommentBeans.add(commentBean);
            }

            activitiesBean_local.setArrayListCommentList(arrayListCommentBeans);

        } catch (Exception exception) {

        }
    }








    @Override
    public ArrayList<HomeBean> getDynamicalHomeItems() {
        // TODO Auto-generated method stub

        JSONObject jsonObject_parent = null;
        String success_value = null;
        ArrayList<HomeBean> arrayListHomeBeans_parent = new ArrayList<HomeBean>();
        HomeBean homeBean = new HomeBean();

        try {

            jsonObject_parent = getJSONFromUrlWithoutParams(JsonURL.GET_HOME_ITEMS);
            if (jsonObject_parent == null) {
                homeBean.setHomeServiceSuccess(false);
                arrayListHomeBeans_parent.add(homeBean);

            } else {
                success_value = jsonObject_parent
                        .getString(JSONConstants.SUCCESS);

                if (success_value == null || success_value.equals("")
                        || success_value.equals("0")) {
                    homeBean.setHomeServiceSuccess(false);
                    arrayListHomeBeans_parent.add(homeBean);

                } else {
                    if (success_value.equals("1")) {

                        JSONArray jsonArray_menu = jsonObject_parent
                                .getJSONArray(JSONConstants.GET_HOME_MAIN_MENU);

                        if (jsonArray_menu == null) {
                            homeBean.setHomeServiceSuccess(false);
                            arrayListHomeBeans_parent.add(homeBean);

                        } else {

                            if (jsonArray_menu.length() == 0) {
                                homeBean.setHomeServiceSuccess(false);
                                arrayListHomeBeans_parent.add(homeBean);

                            } else {
                                for (int i = 0; i < jsonArray_menu.length(); i++) {

                                    JSONObject jsonObject_single_parent_home = jsonArray_menu
                                            .getJSONObject(i);

                                    HomeBean homeBean_local = new HomeBean();

                                    homeBean_local.setHomeServiceSuccess(true);

                                    homeBean_local
                                            .setHomeParentArticleID((jsonObject_single_parent_home
                                                    .getString(JSONConstants.GET_HOME_ID)));

                                    homeBean_local
                                            .setHomeParentTitle((jsonObject_single_parent_home
                                                    .getString(JSONConstants.GET_HOME_TITLE)));

                                    homeBean_local
                                            .setHomeParentLink((jsonObject_single_parent_home
                                                    .getString(JSONConstants.GET_HOME_LINK)));

                                    homeBean_local
                                            .setHomeParentParams((jsonObject_single_parent_home
                                                    .getString(JSONConstants.GET_HOME_PARAMS)));

                                    homeBean_local
                                            .setHomeParentServiceName((jsonObject_single_parent_home
                                                    .getString(JSONConstants.GET_HOME_SERVICE_NAME)));

                                    homeBean_local
                                            .setHomeParentIconLink((jsonObject_single_parent_home
                                                    .getString(JSONConstants.GET_HOME_PARAMS)));

                                    homeBean_local
                                            .setHomeParentArticleID((jsonObject_single_parent_home
                                                    .getString(JSONConstants.GET_HOME_ART_ID)));

                                    arrayListHomeBeans_parent
                                            .add(homeBean_local);

                                }
                            }
                        }

                    } else {

                        homeBean.setHomeServiceSuccess(false);
                        arrayListHomeBeans_parent.add(homeBean);
                    }
                }

            }
        } catch (Exception exception) {

            String string = exception.getMessage();
            System.out.println(string);
        }

        return arrayListHomeBeans_parent;
    }


}
