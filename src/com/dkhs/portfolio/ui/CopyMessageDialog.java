package com.dkhs.portfolio.ui;

import java.io.File;
import java.io.FileNotFoundException;

import com.dkhs.portfolio.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class CopyMessageDialog extends Activity implements OnClickListener {
	private File file_go;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makemessage_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
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
			startActivityForResult(intent, 1);
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
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			Log.e("uri", uri.toString());
			ContentResolver cr = this.getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr
						.openInputStream(uri));

			} catch (FileNotFoundException e) {
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
}