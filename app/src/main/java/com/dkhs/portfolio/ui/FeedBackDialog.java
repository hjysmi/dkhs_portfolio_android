package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
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

public class FeedBackDialog extends Activity implements OnClickListener {
    public static final String HAVING = "havingDelete";

    private boolean having;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back_dialog);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        having = getIntent().getExtras().getBoolean(HAVING);
        initView();
        setListener();
    }

    private void initView() {
        if (having) {
            findViewById(R.id.dialog_button_takephone).setVisibility(View.VISIBLE);
        }
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
                Intent intents = new Intent();
                setResult(RESULT_FIRST_USER, intents);
                finish();
                break;
            case R.id.dialog_button_select:
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
            setResult(RESULT_OK, data);
            finish();
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
            if (f.exists()) {
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

    private ParseHttpListener<UserEntity> listener = new ParseHttpListener<UserEntity>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        };

        @Override
        protected UserEntity parseDateTask(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                return DataParse.parseObjectJson(UserEntity.class, json);
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
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_feed_back_dialog);

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
