package com.dkhs.portfolio.ui;

import java.io.File;
import java.io.FileNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class CopyMessageDialog extends Activity implements OnClickListener {
	private File file_go;
	private UserEngineImpl mUserEngineImpl;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makemessage_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mUserEngineImpl = new UserEngineImpl();
		context = this;
		initView();
		setListener();
	}

	private void initView() {

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
			Intent intent = new Intent();
			/* 开启Pictures画面Type设定为image */
			intent.setType("image/*");
			/* 使用Intent.ACTION_GET_CONTENT这个Action */
			intent.setAction(Intent.ACTION_GET_CONTENT);
			/* 取得相片后返回本画面 */
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
				Uri uri = data.getData();
				Log.e("uri", uri.toString());
				/*Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
				   cursor.moveToFirst();
				   for (int i = 0; i < cursor.getColumnCount(); i++)
				   {// 取得图片uri的列名和此列的详细信息
				    System.out.println(i + "-" + cursor.getColumnName(i) + "-" + cursor.getString(i));
				   }
				   cursor.close();*/
				String url = uri.toString().replace("content:", "").replace("file:", "");
				/*if(!url.contains(".")){
					url = url + ".jpg";
				}*/
				File file = new File(url);
				mUserEngineImpl.setUserHead(file, listener.setLoadingDialog(context));
				ContentResolver cr = this.getContentResolver();
				Bitmap bitmap = BitmapFactory.decodeStream(cr
						.openInputStream(uri));

			} catch (Exception e) {
				Log.e("Exception", e.getMessage(), e);
			}
		}
		if (requestCode == 0x1 && resultCode == RESULT_OK) {
			/*
			 * 使用BitmapFactory.Options类防止OOM(Out Of Memory)的问题；
			 * 创建一个BitmapFactory.Options类用来处理bitmap；
			 */
			try {
				BitmapFactory.Options myoptions = new BitmapFactory.Options();
				/*
				 * 设置Options对象inJustDecodeBounds的属性为true，用于在BitmapFactory的
				 * decodeFile(String path, Options opt)后获取图片的高和宽；
				 * 而且设置了他的属性值为true后使用BitmapFactory的decodeFile()方法无法返回一张
				 * 图片的bitmap对象，仅仅是把图片的高和宽信息给Options对象；
				 */
				myoptions.inJustDecodeBounds = true;
				mUserEngineImpl.setUserHead(file_go, listener.setLoadingDialog(context));
				BitmapFactory.decodeFile(file_go.getAbsolutePath(), myoptions);
				// 根据在图片的宽和高，得到图片在不变形的情况指定大小下的缩略图,设置宽为222；
				int height = myoptions.outHeight * 222 / myoptions.outWidth;
				myoptions.outWidth = 222;
				myoptions.outHeight = height;
				// 在重新设置玩图片显示的高和宽后记住要修改，Options对象inJustDecodeBounds的属性为false;
				// 不然无法显示图片;
				myoptions.inJustDecodeBounds = false;
				// 还没完这里才刚开始,要节约内存还需要几个属性，下面是最关键的一个；
				myoptions.inSampleSize = myoptions.outWidth / 222;
				// 还可以设置其他几个属性用于缩小内存；
				myoptions.inPurgeable = true;
				myoptions.inInputShareable = true;
				myoptions.inPreferredConfig = Bitmap.Config.ARGB_4444;// 默认是Bitmap.Config.ARGB_8888
				// 成功了，下面就显示图片咯；
				Bitmap bitmat = BitmapFactory.decodeFile(
						file_go.getAbsolutePath(), myoptions);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void takePicture() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			String file_str = Environment.getExternalStorageDirectory()
					.getPath();
			File mars_file = new File(file_str + "/my_camera");
			file_go = new File(file_str + "/my_camera/file.jpg");
			// 先创建父目录，如果新创建一个文件的时候，父目录没有存在，那么必须先创建父目录，再新建文件。
			if (!mars_file.exists()) {
				mars_file.mkdirs();
			}

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 并设置拍照的存在方式为外部存储和存储的路径；

			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file_go));
			// 跳转到拍照界面;
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
            	Intent intent=new Intent();
    			setResult(RESULT_OK, intent);
    			finish();
            }
        }
    };
}