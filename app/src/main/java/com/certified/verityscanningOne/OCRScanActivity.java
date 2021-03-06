package com.certified.verityscanningOne;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.PackageManagerUtils;
import com.certified.verityscanningOne.Utils.PermissionUtils;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by harmis on 24/5/17.
 */

public class OCRScanActivity extends AppCompatActivity {

    private static final String CLOUD_VISION_API_KEY = "AIzaSyC2OeK6r7zlqXDsEYEC2xCcAa08w7J7euI";//
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

    private static final String TAG = OCRScanActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    private TextView mImageDetails;
    private ImageView mMainImage;


    public static Activity activity = null;
    CommonSession hJoomAppCommonSession = null;
    ImageView imageView_back = null, imageView_normal_settings = null;
    TextView textView_header_title = null;
    CommonMethods mCommonMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_scan_layout);


        try {
            activity = this;
            hJoomAppCommonSession = new CommonSession(this);
            mCommonMethods = new CommonMethods(this);

            findViewById();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void findViewById() {
        // TODO Auto-generated method stub
        try {

            // headert part
            imageView_back = (ImageView)
                    findViewById(R.id.imageview_back);
            imageView_normal_settings = (ImageView)
                    findViewById(R.id.imageview_setting);

            textView_header_title = (TextView) findViewById(R.id.textview_header_title);
            imageView_normal_settings.setVisibility(View.GONE);
            imageView_back.setVisibility(View.VISIBLE);
            textView_header_title.setVisibility(View.VISIBLE);
            textView_header_title.setText(getResources().getString(R.string.ocr_scan));
            imageView_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OCRScanActivity.this);
                    builder
                            .setMessage(R.string.dialog_select_prompt)
                            .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startGalleryChooser();
                                }
                            })
                            .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startCamera();
                                }
                            });
                    builder.create().show();
                }
            });

            mImageDetails = (TextView) findViewById(R.id.image_details);
            mMainImage = (ImageView) findViewById(R.id.main_image);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void startGalleryChooser() {
        try {
            if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                        GALLERY_IMAGE_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startCamera() {
        try {
            if (PermissionUtils.requestPermission(
                    this,
                    CAMERA_PERMISSIONS_REQUEST,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA)) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getCameraFile() {

        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
                uploadImage(data.getData());
            } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
                Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
                uploadImage(photoUri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);

                callCloudVision(bitmap);
                mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading
        try {
            mImageDetails.setText(R.string.loading_message);

            // Do the real work in an async task, because we need to use the network anyway
            new AsyncTask<Object, Void, String>() {
                @Override
                protected String doInBackground(Object... params) {
                    try {
                        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                        VisionRequestInitializer requestInitializer =
                                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                                    /**
                                     * We override this so we can inject important identifying fields into the HTTP
                                     * headers. This enables use of a restricted cloud platform API key.
                                     */
                                    @Override
                                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                            throws IOException {
                                        super.initializeVisionRequest(visionRequest);

                                        String packageName = getPackageName();
                                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                    }
                                };

                        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                        builder.setVisionRequestInitializer(requestInitializer);

                        Vision vision = builder.build();

                        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                                new BatchAnnotateImagesRequest();
                        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                            // Add the image
                            Image base64EncodedImage = new Image();
                            // Convert the bitmap to a JPEG
                            // Just in case it's a format that Android understands but Cloud Vision
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                            byte[] imageBytes = byteArrayOutputStream.toByteArray();

                            // Base64 encode the JPEG
                            base64EncodedImage.encodeContent(imageBytes);
                            annotateImageRequest.setImage(base64EncodedImage);

                            // add the features we want
                            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                                Feature labelDetection = new Feature();
                                // labelDetection.setType("LABEL_DETECTION");
                                labelDetection.setType("TEXT_DETECTION");
                                // labelDetection.setMaxResults(10);
                                add(labelDetection);
                            }});

                            // Add the list of one thing to the request
                            add(annotateImageRequest);
                        }});

                        Vision.Images.Annotate annotateRequest =
                                vision.images().annotate(batchAnnotateImagesRequest);
                        // Due to a bug: requests to Vision API containing large images fail when GZipped.
                        annotateRequest.setDisableGZipContent(true);
                        Log.d(TAG, "created Cloud Vision request object, sending request");

                        BatchAnnotateImagesResponse response = annotateRequest.execute();
                        return convertResponseToString(response);

                    } catch (GoogleJsonResponseException e) {
                        Log.d(TAG, "failed to make API request because " + e.getContent());
                    } catch (IOException e) {
                        Log.d(TAG, "failed to make API request because of other IOException " +
                                e.getMessage());
                    }
                    return "Cloud Vision API request failed. Check logs for details.";
                }

                protected void onPostExecute(String result) {
                    mImageDetails.setText(result);
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int resizedWidth = 0;
        int resizedHeight = 0;
        try {
            int originalWidth = bitmap.getWidth();
            int originalHeight = bitmap.getHeight();
            resizedWidth = maxDimension;
            resizedHeight = maxDimension;

            if (originalHeight > originalWidth) {
                resizedHeight = maxDimension;
                resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
            } else if (originalWidth > originalHeight) {
                resizedWidth = maxDimension;
                resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
            } else if (originalHeight == originalWidth) {
                resizedHeight = maxDimension;
                resizedWidth = maxDimension;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {

        Log.e("Responce ==>> ", response.toString());

        String message = "";

//        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();
//        if (labels != null) {
//            for (EntityAnnotation label : labels) {
//                message += String.format(Locale.US, "%.3f: %s", label.getScore(), label.getDescription());
//                message += "\n";
//            }
//        } else {
//            message += "nothing";
//        }


        EntityAnnotation labelsMy = response.getResponses().get(0).getTextAnnotations().get(0);
        if (labelsMy != null) {
            message += String.format(Locale.US, "%.3f: %s", labelsMy.getScore(), labelsMy.getDescription());
        } else {
            message += "nothing";
        }

        message = message.startsWith("nul:") ? message.substring(4) : message;
        message = "I found these things:\n\n" + message;

        return message;
    }
}
