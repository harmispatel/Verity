package com.certified.verityscanningOne;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.certified.verityscanningOne.Utils.CommonKeyword;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.CompressImageUtil;

import java.io.File;

/**
 * Created by harmis on 3/3/17.
 */

public class AddProductActivity extends BaseActivity {

    public static Activity activity = null;
    TextView txtUpcCode = null, txtSelectImage = null;
    EditText edtName = null;
    ImageView imgProductImage;
    TextView txtOk, txtCancel = null;

    String strName;

    CommonSession hJoomAppCommonSession = null;
    String TAG = "AddProductActivity";
    View view_add_product = null;

    String upcCode;

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    Bitmap bitmap = null;
    String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            activity = this;
            hJoomAppCommonSession = new CommonSession(this);
            imageView_normal_settings.setVisibility(View.GONE);
            imageView_back.setVisibility(View.VISIBLE);
            imageView_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);

                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                upcCode = bundle.getString("upcCode");
            }

            findViewById();
            setUI();

        } catch (Exception e) {
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        try {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startDialog() {
        try {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                    AddProductActivity.this);
            myAlertDialog.setTitle(getResources().getString(R.string.profile_image_select_title));
            myAlertDialog.setMessage(getResources().getString(R.string.profile_image_select_message));

            myAlertDialog.setPositiveButton(getResources().getString(R.string.profile_image_opt1),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent pictureActionIntent = null;

                            pictureActionIntent = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(
                                    pictureActionIntent,
                                    GALLERY_PICTURE);

                        }
                    });

            myAlertDialog.setNegativeButton(getResources().getString(R.string.profile_image_opt2),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment
                                    .getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(f));

                            startActivityForResult(intent,
                                    CAMERA_REQUEST);

                        }
                    });
            myAlertDialog.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setUI() {
        // TODO Auto-generated method stub
        try {

            txtSelectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            txtOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        strName = edtName.getText().toString();


                        if (strName == null || strName.equalsIgnoreCase("")) {
                            mCommonMethods.mToast("Enter Product Name");
                        } else {

                            if (bitmap == null) {
                                mCommonMethods.mToast("Select Image");
                                return;
                            }

                            if (CommonMethods.isNetworkConnected(AddProductActivity.this)) {
                                new AddProductAsynctask().execute();
                            } else {
                                mCommonMethods.mToast("No Internet Connection");
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void findViewById() {
        // TODO Auto-generated method stub
        try {
            view_add_product = getLayoutInflater().inflate(R.layout.add_product_layout, null);

            txtUpcCode = (TextView) view_add_product.findViewById(R.id.txtUpcCode);
            txtSelectImage = (TextView) view_add_product.findViewById(R.id.selectImageTxt);
            edtName = (EditText) view_add_product.findViewById(R.id.edittext_product_name);
            imgProductImage = (ImageView) view_add_product.findViewById(R.id.imageViewUProduct);
            txtOk = (TextView) view_add_product.findViewById(R.id.add_product_ok);
            txtCancel = (TextView) view_add_product.findViewById(R.id.add_product_cancel);

            frameLayout_container.addView(view_add_product);

            txtUpcCode.setText("UPC :" + upcCode);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();


        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            bitmap = null;
            selectedImagePath = null;

            File f = new File(Environment.getExternalStorageDirectory()
                    .toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            if (!f.exists()) {

                Toast.makeText(getBaseContext(),
                        getResources().getString(R.string.camera_error_message), Toast.LENGTH_LONG)
                        .show();
                return;
            }

            try {

                CompressImageUtil compressUtil = new CompressImageUtil(getApplicationContext());
                selectedImagePath = compressUtil.compressImage(f.getAbsolutePath(), imgProductImage);
                bitmap = compressUtil.getBitmap();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {

            bitmap = null;
            selectedImagePath = null;

            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);
                c.close();

                CompressImageUtil compressUtil = new CompressImageUtil(getApplicationContext());
                selectedImagePath = compressUtil.compressImage(selectedImagePath, imgProductImage);
                bitmap = compressUtil.getBitmap();


            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.camera_cancelled),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }


    @SuppressLint("NewApi")
    public class AddProductAsynctask extends AsyncTask<String, String, String> {


        ProgressDialog progress;

        @SuppressLint("NewApi")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            try {

                progress = new ProgressDialog(AddProductActivity.this);
                progress.setMessage("please wait...");
                progress.setIndeterminate(true);
                progress.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String mbean = null;
            try {

                mbean = mCommonMethods.addProduct(upcCode, strName, bitmap, selectedImagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mbean;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {

                progress.dismiss();

                if (result.equalsIgnoreCase(CommonKeyword.TRUE)) {
                    bitmap = null;
                    mCommonMethods.mToast("Product Added successFully");

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", upcCode);
                    setResult(RESULT_OK, returnIntent);
                    finish();

                } else {
                    mCommonMethods.mToast(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}

