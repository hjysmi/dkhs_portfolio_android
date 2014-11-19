package com.dkhs.portfolio.ui;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FeedBackBean;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PromptManager;
/**
 * 意见反馈
 * @author weiting
 *
 */
public class FeedBackActivity extends ModelAcitivity implements OnClickListener{
	private Button btnCancle;
	private Button btnSave;
	private EditText feedEditText;
	private ImageView feedImageLoad;
	private EditText feedEditCom;
	private File imageFile;
	private Context context;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_feed_back);
		setTitle(R.string.feed_back_title);
		context = this;
		initView();
		initListener();
	}
	private void initView(){
		feedEditText = (EditText) findViewById(R.id.feed_edit_text);
		feedImageLoad = (ImageView) findViewById(R.id.feed_image_load);
		feedEditCom = (EditText) findViewById(R.id.feed_edit_com);
		btnCancle = getBtnBack();
		btnSave = getRightButton();
		
		
		btnCancle.setText("取消");
		btnCancle.setBackgroundResource(R.drawable.white_black_selector);
		btnCancle.setCompoundDrawables(null, null, null, null);
		btnSave.setText("提交");
		btnSave.setBackgroundResource(R.drawable.white_black_selector);
	}
	private void initListener(){
		btnSave.setOnClickListener(this);
		feedImageLoad.setOnClickListener(this);
	}
	public void setSign() {
        try {
			UserEngineImpl engine = new UserEngineImpl();
			if(TextUtils.isEmpty(feedEditText.getText().toString())){
				PromptManager.showToast(R.string.sign_text_notice);
				return;
			}
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			engine.setFeedBack("portfolio_android",version, feedEditText.getText().toString(), feedEditCom.getText().toString(), (null == imageFile)? null : imageFile, listener);
			listener.setLoadingDialog(context).beforeRequest();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	private ParseHttpListener<FeedBackBean> listener = new ParseHttpListener<FeedBackBean>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        };

        @Override
        protected FeedBackBean parseDateTask(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                FeedBackBean ue = DataParse.parseObjectJson(FeedBackBean.class, json);
                return ue;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(FeedBackBean entity) {

            // PromptManager.closeProgressDialog();
            if (null != entity) {
            	PromptManager.showToast(R.string.feed_text_success);
                finish();
            }
        }
    };
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_right:
			setSign();
			break;
		case R.id.feed_image_load:
			Intent intent  = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			/* 开启Pictures画面Type设定为image */
			/*intent.setType("image/*");
			 使用Intent.ACTION_GET_CONTENT这个Action 
			intent.setAction(Intent.ACTION_GET_CONTENT);*/
			/* 取得相片后返回本画面 */
			startActivityForResult(intent, 5);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
	if (requestCode == 5 && resultCode == RESULT_OK) {
				
				try {
					Uri uri = data.getData();
					
					String[] proj = {MediaStore.Images.Media.DATA};
		            Cursor cursor = managedQuery(uri, proj, null, null, null); 
		            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		            cursor.moveToFirst();
	
		            String path = cursor.getString(column_index);
		            imageFile = new File(path);
					ContentResolver cr = this.getContentResolver();
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					feedImageLoad.setImageBitmap(bitmap);
				} catch (Exception e) {
					Log.e("Exception", e.getMessage(), e);
				}
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	
}
