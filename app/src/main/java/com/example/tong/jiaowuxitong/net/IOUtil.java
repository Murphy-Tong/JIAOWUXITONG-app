package com.example.tong.jiaowuxitong.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by TONG on 2017/1/16.
 */
public class IOUtil {
    private static final String suffix = ".obj";

    public static void writeObj(final Context context, final Serializable serializable, final String name, @Nullable final CallBack callBack) {
        new AsyncTask<String, Void, Boolean>() {

            @Override
            protected void onPostExecute(Boolean o) {
                if (callBack != null)
                    callBack.onPost(o);

            }

            @Override
            protected Boolean doInBackground(String... params) {
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    File file = context.getExternalFilesDir(null);
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    TestUtil.print(file.getPath());
                    fos = new FileOutputStream(file + name + suffix);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(serializable);
                    oos.flush();
                    return true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    close(fos);
                    close(oos);
                }
            }
        }.execute(name);

    }


    public static void writeString(Context context, String string, String filename, String name, CallBack callBack) {
        if (context == null || TextUtils.isEmpty(string) || TextUtils.isEmpty(filename) || TextUtils.isEmpty(name))
            return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(name, string);
        edit.commit();
//        com.example.tong.jiaowuxitong.TestUtil.toast(context, "writeString ok");
    }

    public static void writeFloatSet(Context context, float[] fs, String filename, String name, CallBack callBack) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Set<String> ss = new TreeSet<>();
        for (float f : fs
                ) {
            ss.add(String.valueOf(f));
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putStringSet(name, ss);
        edit.commit();

    }

    private static class MySet extends HashSet {

    }

    public static void delete(Context context,String fileName,String name){
        if(context==null||TextUtils.isEmpty(fileName)) return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(name);
        edit.commit();
    }

    public static void writeStringSet(Context context, String[] fs, String filename, String name, CallBack callBack) {
        if (context == null || fs == null || fs.length == 0 || TextUtil.isEmpty(name) || TextUtil.isEmpty(filename))
            return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);

        StringBuffer stringBuffer = new StringBuffer();
        for (String s : fs
                ) {
            if (TextUtil.isEmpty(s)) continue;
            stringBuffer.append(s);
            stringBuffer.append(",");
        }
        if (stringBuffer.length() > 0)
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(name, stringBuffer.toString());
        edit.commit();
//        com.example.tong.jiaowuxitong.TestUtil.toast(context, "writeStringSet ok");

    }


    public static float[] readFloatSet(Context context, String filename, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Set<String> ss = sharedPreferences.getStringSet(name, null);
        if (ss == null) return null;
        Iterator<String> iterator = ss.iterator();
        float[] fs = new float[ss.size()];
        int i = 0;
        while (iterator != null && iterator.hasNext()) {
            fs[i++] = Float.parseFloat(iterator.next());
        }
        return fs;
    }


    public static String readString(Context context, String filename, String name) {
        if (context == null || TextUtils.isEmpty(filename) || TextUtils.isEmpty(name)) return null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
//        com.example.tong.jiaowuxitong.TestUtil.toast(context, "readString ok");
        return sharedPreferences.getString(name, null);
    }

    public static String[] readStringSet(Context context, String filename, String name) {
        if (TextUtil.isEmpty(name) || TextUtil.isEmpty(filename)) return null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String ss = sharedPreferences.getString(name, null);
        if (TextUtils.isEmpty(ss)) return null;
        String[] fs = ss.split(",");
//        com.example.tong.jiaowuxitong.TestUtil.toast(context, "readStringSet ok");
        return fs;
    }

    public static void deleteFile(Context context, String optsfile, String evaChoose) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(optsfile, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(evaChoose);
        edit.commit();
//        com.example.tong.jiaowuxitong.TestUtil.toast(context, "deleteFile");
    }


    public static interface CallBack {
        void onPost(Object object);
    }


    public static void readObj(final Context context, final String name, final CallBack callBack) {
        new AsyncTask<String, Void, Object>() {

            @Override
            protected void onPostExecute(Object o) {
                if (callBack != null)
                    callBack.onPost(o);

            }

            @Override
            protected Object doInBackground(String... params) {
                if (context == null) return null;
                File file = context.getExternalFilesDir(name + suffix);
                if (!file.exists()) return null;
                FileInputStream fis = null;
                ObjectInputStream ois = null;
                Object o = null;
                try {
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    o = ois.readObject();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (StreamCorruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    close(fis);
                    close(ois);

                }
                return (Serializable) o;
            }
        }.execute(name);

    }

    private static void close(InputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fis = null;
            }
        }
    }

    private static void close(OutputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fis = null;
            }
        }

    }
}
