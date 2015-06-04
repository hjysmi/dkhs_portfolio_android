package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PickupPhotoActivity extends Activity implements OnClickListener {
    private File file_go;
    private UserEngineImpl mUserEngineImpl;
    private Context context;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makemessage_dialog);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mUserEngineImpl = new UserEngineImpl();
        context = this;
        initView();
        setListener();
    }

    private void initView() {
        findViewById(R.id.dialog_bg).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

    }

    private void setListener() {
        findViewById(R.id.dialog_button_takephone).setOnClickListener(this);
        findViewById(R.id.dialog_button_select).setOnClickListener(this);
        findViewById(R.id.dialog_button_cancle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.dialog_button_takephone:
                takePicture();
                break;
            case R.id.dialog_button_select:
                /*
                 * Intent intent = new Intent();
                 * 开启Pictures画面Type设定为image
                 * intent.setType("image/*");
                 * 使用Intent.ACTION_GET_CONTENT这个Action
                 * intent.setAction(Intent.ACTION_GET_CONTENT);
                 */
                /* 取得相片后返回本画面 */
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 5);
                break;
            case R.id.dialog_button_cancle:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5 && resultCode == RESULT_OK) {

            try {
                String file_str = Environment.getExternalStorageDirectory().getPath();
                Uri uri = data.getData();

                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                String path = cursor.getString(column_index);
                Bitmap imageBitmap = UIUtils.getimage(path);
                File f = new File(file_str + "/my_camera/file.jpg");
                if (f.exists()) {
                    f.delete();
                }
                FileOutputStream out = new FileOutputStream(f);
                imageBitmap = UIUtils.loadBitmap(imageBitmap, path);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                mUserEngineImpl.setUserHead(f, listener.setLoadingDialog(context));
                /*
                 * ContentResolver cr = this.getContentResolver();
                 * Bitmap bitmap = BitmapFactory.decodeStream(cr
                 * .openInputStream(uri));
                 */

            } catch (Exception e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            try {
                String file_str = Environment.getExternalStorageDirectory().getPath();
                Uri uri = null;
                if (null != data) {
                    uri = data.getData();
                }
                if (null == uri && photoUri != null) {
                    uri = photoUri;
                }
                if (uri == null) {
                    return;
                }
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                String path = cursor.getString(column_index);
                Bitmap imageBitmap = UIUtils.getimage(path);
                File f = new File(file_str + "/my_camera/file.jpg");
                if (f.exists()) {
                    f.delete();
                }
                FileOutputStream out = new FileOutputStream(f);
                imageBitmap = UIUtils.loadBitmap(imageBitmap, path);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                mUserEngineImpl.setUserHead(f, listener.setLoadingDialog(context));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void copyImg(String path) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(path);
            String file_str = Environment.getExternalStorageDirectory().getPath();
            File mars_file = new File(file_str + "/my_camera");
            File f = new File(mars_file, "file.jpg");
            if (f.exists() && !f.toString().equals(path)) {
                f.delete();
            }
            if (oldfile.exists()) { // 文件不存在时
                InputStream inStream = new FileInputStream(path); // 读入原文件
                FileOutputStream fs = new FileOutputStream(file_str + "/my_camera/file.jpg");
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            Bitmap imageBitmap;// = BitmapFactory.decodeFile(file_str +"/my_camera/file.jpg");
            imageBitmap = UIUtils.getimage(file_str + "/my_camera/file.jpg");
            imageBitmap = UIUtils.loadBitmap(imageBitmap, path);
            f = new File(mars_file, "file.jpg");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void takePicture() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String file_str = Environment.getExternalStorageDirectory().getPath();
            File mars_file = new File(file_str + "/my_camera");
            file_go = new File(file_str + "/my_camera/file.jpg");
            // 先创建父目录，如果新创建一个文件的时候，父目录没有存在，那么必须先创建父目录，再新建文件。
            if (!mars_file.exists()) {
                mars_file.mkdirs();
            }

            // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 并设置拍照的存在方式为外部存储和存储的路径；

            // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file_go));
            // 跳转到拍照界面;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            ContentValues values = new ContentValues();
            values.put(Media.TITLE, filename);

            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, 0x1);
        } else {
            Toast.makeText(this, "请先安装好sd卡", Toast.LENGTH_LONG).show();
        }
    }

    private ParseHttpListener<UserEntity> listener = new ParseHttpListener<UserEntity>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        };

        @Override
        protected UserEntity parseDateTask(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                UserEntity ue = DataParse.parseObjectJson(UserEntity.class, json);
                return ue;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(UserEntity entity) {

            // PromptManager.closeProgressDialog();
            if (null != entity) {
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL,
                        entity.getAvatar_md());
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    };
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_dialog_getphoto);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }
}
