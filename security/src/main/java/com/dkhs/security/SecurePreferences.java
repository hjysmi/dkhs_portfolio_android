package com.dkhs.security;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zjz on 2015/6/5.
 * 对SharedPreferences的加密封装
 */
public class SecurePreferences implements SharedPreferences {
    private final SharedPreferences sp;
    private final Cipher encrypter;
    private final Cipher decrypter;


    @SuppressLint("GetInstance")
    public SecurePreferences(SharedPreferences sp, String pass) {
        this.sp = sp;


        try {
            byte[] key = MessageDigest.getInstance("SHA-256").digest(pass.getBytes());
            encrypter = Cipher.getInstance("AES");
            encrypter.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
            decrypter = Cipher.getInstance("AES");
            decrypter.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException();
    }


    @Override
    public String getString(String key, String defValue) {
        String v = sp.getString(encrypt(key), defValue == null ? null : encrypt(defValue));
        return v == null ? null : decrypt(v);
    }


    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        throw new UnsupportedOperationException();
    }


    @Override
    public int getInt(String key, int defValue) {
        return Integer.parseInt(getString(key, Integer.toString(defValue)));
    }


    @Override
    public long getLong(String key, long defValue) {
        return Long.parseLong(getString(key, Long.toString(defValue)));
    }


    @Override
    public float getFloat(String key, float defValue) {
        return Float.parseFloat(getString(key, Float.toString(defValue)));
    }


    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return Boolean.parseBoolean(getString(key, Boolean.toString(defValue)));
    }


    @Override
    public boolean contains(String key) {
        return sp.contains(encrypt(key));
    }


    @SuppressLint("CommitPrefEdits")
    @Override
    public Editor edit() {
        return new SecureEditor(sp.edit());
    }


    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        sp.registerOnSharedPreferenceChangeListener(listener);
    }


    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }


    private String encrypt(String value) {
        try {
            return Base64.encodeToString(encrypter.doFinal(value.getBytes()), Base64.DEFAULT).trim();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    private String decrypt(String value) {
        try {
            return new String(decrypter.doFinal(Base64.decode(value, Base64.DEFAULT))).trim();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    private class SecureEditor implements Editor {
        private final Editor editor;


        private SecureEditor(Editor editor) {
            this.editor = editor;
        }


        @Override
        public Editor putString(String key, String value) {
            editor.putString(encrypt(key), encrypt(value));
            return this;
        }


        @Override
        public Editor putStringSet(String key, Set<String> values) {
            throw new UnsupportedOperationException();
        }


        @Override
        public Editor putInt(String key, int value) {
            editor.putString(encrypt(key), encrypt(Integer.toString(value)));
            return this;
        }


        @Override
        public Editor putLong(String key, long value) {
            editor.putString(encrypt(key), encrypt(Long.toString(value)));
            return this;
        }


        @Override
        public Editor putFloat(String key, float value) {
            editor.putString(encrypt(key), encrypt(Float.toString(value)));
            return this;
        }


        @Override
        public Editor putBoolean(String key, boolean value) {
            editor.putString(encrypt(key), encrypt(Boolean.toString(value)));
            return this;
        }


        @Override
        public Editor remove(String key) {
            editor.remove(encrypt(key));
            return this;
        }


        @Override
        public Editor clear() {
            editor.clear();
            return this;
        }


        @Override
        public boolean commit() {
            return editor.commit();
        }


        @Override
        public void apply() {
            editor.apply();
        }
    }
}
