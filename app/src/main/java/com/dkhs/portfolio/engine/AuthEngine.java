package com.dkhs.portfolio.engine;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.ProInfoBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by xuetong on 2015/12/15.
 * 认证信息上传
 */
public class AuthEngine {


    public void upInfo(IHttpListener listener, ProInfoBean proInfoBean) {
        try {
            RequestParams params = new RequestParams();
            params.addBodyParameter("verified_type", String.valueOf(proInfoBean.verified_type));
            if (!TextUtils.isEmpty(proInfoBean.cert_no)) {
                params.addBodyParameter("cert_no", proInfoBean.cert_no);
            }
            if (!TextUtils.isEmpty(proInfoBean.cert_description)) {
                params.addBodyParameter("cert_description", proInfoBean.cert_description);
            }
            if (!TextUtils.isEmpty(proInfoBean.real_name)) {
                params.addBodyParameter("real_name", proInfoBean.real_name);
            }
            if (!TextUtils.isEmpty(proInfoBean.id_card_no)) {
                params.addBodyParameter("id_card_no", proInfoBean.id_card_no);
            }
            if (!TextUtils.isEmpty(proInfoBean.city)) {
                params.addBodyParameter("city", proInfoBean.city);
            }
            if (!TextUtils.isEmpty(proInfoBean.province)) {
                params.addBodyParameter("province", proInfoBean.province);
            }
            if (!TextUtils.isEmpty(proInfoBean.description)) {
                params.addBodyParameter("description", proInfoBean.description);
            }
            if (null != proInfoBean.org_profile) {
                params.addBodyParameter("org_profile", String.valueOf(proInfoBean.org_profile.id));
            }

            if (null != proInfoBean.photos && proInfoBean.photos.size() > 0) {
                for (int i = 0; i < proInfoBean.photos.size(); i++) {
                    params.addBodyParameter("image" + (i + 1), getImgFile(proInfoBean.photos.get(i)));
                }
            }
            params.addBodyParameter("id_card_photo_full", getImgFile(proInfoBean.id_card_photo_full));
            DKHSClient.requestLong(HttpRequest.HttpMethod.POST, DKHSUrl.Pro_verfications.pro_verfications_url, params, listener);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private File getImgFile(String img) throws Exception {
        Bitmap imageBitmap = UIUtils.getLocaleimage(img);
        File f = getTempFile();
        if (null == f) {
            f = new File(img);
        } else if (f.exists()) {
            f.delete();
        }
        FileOutputStream out = new FileOutputStream(f);
        Bitmap bitmap = UIUtils.loadBitmap(imageBitmap, img);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.flush();
        out.close();
        return f;
    }

    private File getTempFile() {
        File outputFile = null;
        try {
            File outputDir = PortfolioApplication.getInstance().getCacheDir(); // context being the Activity pointer
            outputFile = File.createTempFile("temp_upload", ".jpg", outputDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputFile;
    }
}
