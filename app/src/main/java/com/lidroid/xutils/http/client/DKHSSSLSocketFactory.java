package com.lidroid.xutils.http.client;

import android.util.Log;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * trust all certs
 */
public class DKHSSSLSocketFactory extends SSLSocketFactory {

    private SSLContext sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);
    private static TrustManagerFactory trustManager;
    private static final String CLIENT_TRUST_PASSWORD = "123456";//信任证书密码
    private static final String CLIENT_AGREEMENT = "TLS";//使用协议
    private static final String CLIENT_TRUST_MANAGER = "X509";//
    private static final String CLIENT_TRUST_KEYSTORE = "BKS";//

    private static KeyStore trustStore;

    static {
        try {
            trustManager = TrustManagerFactory.getInstance(CLIENT_TRUST_MANAGER);
            trustStore = KeyStore.getInstance(CLIENT_TRUST_KEYSTORE);
            trustStore.load(PortfolioApplication.getInstance().getResources().openRawResource(R.raw.dkhs), CLIENT_TRUST_PASSWORD.toCharArray());
            trustManager.init(trustStore);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    private static DKHSSSLSocketFactory instance;

    public static DKHSSSLSocketFactory getSocketFactory() {
        if (instance == null) {
            try {
                instance = new DKHSSSLSocketFactory();
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
            }
        }
        return instance;
    }

    private DKHSSSLSocketFactory()
            throws UnrecoverableKeyException,
            NoSuchAlgorithmException,
            KeyStoreException,
            KeyManagementException {
        super(trustStore);

        sslContext.init(null, trustManager.getTrustManagers(), null);

        this.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}
