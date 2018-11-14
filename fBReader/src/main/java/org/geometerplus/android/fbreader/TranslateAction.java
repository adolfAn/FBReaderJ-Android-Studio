/*
 * Copyright (C) 2007-2015 FBReader.ORG Limited <contact@fbreader.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.translate.demo.MD5;

import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.zlibrary.ui.android.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dyg.activity.TransWebViewActivity;
import dyg.beans.CiBaWordBeanJson;
import dyg.beans.GsonBuildList;
import dyg.net.LoveFamousBookNet;
import dyg.net.LoveFamousMp3FileDownload;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class TranslateAction extends FBAndroidAction {
    private final static String TAG = "TranslateAction";
    private Activity activity;
    private Dialog dialog;
    private TextView trans_more, trans_none, phonetic_content_en, phonetic_content_us;
    private ImageView read_en, read_us;
    private LinearLayout symbolLayout, trans_phonetic;
    private int screen_height;
    private Pattern pattern = Pattern.compile("[a-z]+");
    private SoundPool mSoundPool;


    TranslateAction(FBReader baseActivity, FBReaderApp fbreader) {
        super(baseActivity, fbreader);
        this.activity = baseActivity;
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        screen_height = metrics.heightPixels;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_translate, null);
        dialog.setContentView(view);
        trans_phonetic = (LinearLayout) view.findViewById(R.id.trans_phonetic_layout);
        symbolLayout = (LinearLayout) view.findViewById(R.id.symbol_layout);
        trans_none = (TextView) view.findViewById(R.id.trans_none);
        trans_more = (TextView) view.findViewById(R.id.trans_more);

        phonetic_content_en = (TextView) view.findViewById(R.id.phonetic_content_en);
        phonetic_content_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prounce(read_en.getTag());
            }
        });

        phonetic_content_us = (TextView) view.findViewById(R.id.phonetic_content_us);
        phonetic_content_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prounce(read_us.getTag());
            }
        });
        read_en = (ImageView) view.findViewById(R.id.read_en);
        read_us = (ImageView) view.findViewById(R.id.read_us);
        read_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prounce(read_en.getTag());
            }
        });
        read_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prounce(read_us.getTag());
            }
        });
        trans_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                String string = (String) trans_more.getTag();
                if (string == null) {
                    return;
                }
                Intent intent = new Intent(activity, TransWebViewActivity.class);
                intent.putExtra("key", string);
                activity.startActivity(intent);
//                Toast.makeText(activity, "显示查看更多", Toast.LENGTH_LONG).show();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog1) {
                symbolLayout.setVisibility(View.GONE);
                trans_phonetic.setVisibility(View.GONE);
                trans_none.setVisibility(View.GONE);
                trans_more.setVisibility(View.GONE);
                trans_more.setTag(null);
                symbolLayout.removeAllViews();
            }
        });
        init();
    }

    private void prounce(final Object tag) {
        if (tag == null) {
            Toast.makeText(activity, "sorry! no word to read", Toast.LENGTH_LONG).show();
            return;
        }
        if (tag.toString().equals("")) {
            Log.e(TAG, "sorry! no word to read");
            Toast.makeText(activity, "sorry! no word to read", Toast.LENGTH_LONG).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://yy.cc").build();
        LoveFamousMp3FileDownload fileDownload = retrofit.create(LoveFamousMp3FileDownload.class);
        Call<ResponseBody> call = fileDownload.downloadMp3((String) tag);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    File file = writeResponseBodyToDisk(body, (String) tag);
                    if (file != null) {
                        final int id = mSoundPool.load(file.getAbsolutePath(), 1);
                        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener
                                () {
                            @Override
                            public void onLoadComplete(SoundPool soundPool, int sampleId, int
                                    status) {
                                mSoundPool.play(id, 1.0f, 1.0f, 1, 0, 1.0f);
                            }
                        });

                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("请求失败");
                System.out.println(t.getMessage());
                Toast.makeText(activity, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                new AlertDialog.Builder(activity).setTitle("error").setMessage(t.getMessage())
// .create().show();
            }
        });
//        retrofit.

    }

    @Override
    protected void run(final Object... params) {

        if (params[0] == null) {
            Log.i(TAG, "run: TranslateAction param is empty return");
            return;
        }

        String key = (String) params[0];
        if (TextUtils.isEmpty(key)) {
            Log.i(TAG, "run: TranslateAction translate key  is empty return");
            return;
        }
        key = key.trim().toLowerCase();
        if (TextUtils.isEmpty(key)) {
            Log.i(TAG, "run: TranslateAction translate key  is empty return");
            return;
        }
        Matcher matcher = pattern.matcher(key);
        if (matcher.find()) {
            key = matcher.toMatchResult().group();
        } else {
            Log.i(TAG, "run: TranslateAction translate key  is empty return");
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dict-co.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuildList.buildGson()))
                .build();
        LoveFamousBookNet lbn = retrofit.create(LoveFamousBookNet.class);
        HashMap map = new HashMap();
        map.put("w", key);
        map.put("type", "json");
        map.put("key", "297EB35CDF5FEEEFD6A13200E46FA720");
        Call<CiBaWordBeanJson> res = lbn.getWords(map);
        res.enqueue(new Callback<CiBaWordBeanJson>() {
            @Override
            public void onResponse(Call<CiBaWordBeanJson> call, Response<CiBaWordBeanJson>
                    response) {
                CiBaWordBeanJson ciBaWordBeanJson = response.body();
                if (ciBaWordBeanJson != null) {
                    List<CiBaWordBeanJson.SymbolsBean> symbols = ciBaWordBeanJson.getSymbols();
                    CiBaWordBeanJson.SymbolsBean symbolsBean = symbols.get(0);
                    if (symbolsBean == null) {
                        trans_none.setVisibility(View.VISIBLE);
                    } else {
                        String word_name = ciBaWordBeanJson.getWord_name();
                        trans_none.setVisibility(View.GONE);
                        trans_more.setTag(word_name);
                        // add phonetic
                        String enPhonetic = symbolsBean.getPh_en();
                        String usPhonetic = symbolsBean.getPh_am();
                        String enPhonetic_mp3 = symbolsBean.getPh_en_mp3();
                        String usPhonetic_mp3 = symbolsBean.getPh_am_mp3();
                        String tts_mp3 = symbolsBean.getPh_tts_mp3();
                        if (TextUtils.isEmpty(enPhonetic_mp3)) {
                            if (!TextUtils.isEmpty(tts_mp3)) {
                                enPhonetic_mp3 = tts_mp3;
                            } else if (!TextUtils.isEmpty(usPhonetic_mp3)) {
                                enPhonetic = usPhonetic_mp3;
                            }
                        }
                        if (TextUtils.isEmpty(usPhonetic_mp3)) {
                            if (!TextUtils.isEmpty(tts_mp3)) {
                                usPhonetic_mp3 = tts_mp3;
                            } else if (!TextUtils.isEmpty(enPhonetic_mp3)) {
                                usPhonetic_mp3 = enPhonetic_mp3;
                            }
                        }
                        phonetic_content_en.setText("[" + enPhonetic + "]");
                        phonetic_content_us.setText("[" + usPhonetic + "]");
                        read_en.setTag(enPhonetic_mp3);
                        read_us.setTag(usPhonetic_mp3);
                        // add symbol
                        TextView textView = (TextView) LayoutInflater.from(activity).inflate(R
                                .layout.dialog_symbol, null);
                        StringBuilder builder = new StringBuilder();
                        List<CiBaWordBeanJson.SymbolsBean.PartsBean> parts = symbolsBean.getParts();
                        for (int i = 0; i < parts.size(); i++) {
                            CiBaWordBeanJson.SymbolsBean.PartsBean partsBean = parts.get(i);
                            builder.append(partsBean.getPart()).append("    ").append
                                    (getTransChinese(builder, partsBean.getMeans())).append('\n');
                        }
                        textView.setText(builder.toString());
                        symbolLayout.addView(textView);
                        trans_phonetic.setVisibility(View.VISIBLE);
                        symbolLayout.setVisibility(View.VISIBLE);
                        trans_more.setVisibility(View.VISIBLE);
                    }
                    int x = (int) params[1];
                    int y = (int) params[2];
                    if ((x + y) > 0) {
                        Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager
                                .LayoutParams.WRAP_CONTENT);
                        WindowManager.LayoutParams wlp = window.getAttributes();
                        wlp.gravity = Gravity.TOP | Gravity.START;
                        int dialogH = window.getDecorView().getHeight();
                        int last_distance = screen_height - y;
                        if (dialogH < last_distance) {
                            wlp.y = y;
                            // todo arrow 朝上
                        } else {
                            wlp.y = y - 20;
                            // todo arrow 在下面朝下
                        }
                        wlp.x = 0;
                        window.setAttributes(wlp);
                    }
                    dialog.show();
                }

            }

            @Override
            public void onFailure(Call<CiBaWordBeanJson> call, Throwable t) {
                System.out.println("请求失败");
                System.out.println(t.getMessage());
                new AlertDialog.Builder(activity).setTitle("error").setMessage(t.getMessage())
                        .create().show();
            }
        });


    }

    private String getTransChinese(StringBuilder builder, List<String> means) {
        for (int i = 0; i < means.size(); i++) {
            String mean = means.get(i);
            builder.append(mean);
            if (i != means.size() - 1) {
                builder.append(";");
            }
        }
        return " ";
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= 21) {
            //SDK_INT >= 21时，才能使用SoundPool.Builder创建SoundPool
            SoundPool.Builder builder = new SoundPool.Builder();

            //可同时播放的音频流
            builder.setMaxStreams(5);

            //音频属性的Builder
            AudioAttributes.Builder attrBuild = new AudioAttributes.Builder();

            //音频类型
            attrBuild.setLegacyStreamType(AudioManager.STREAM_MUSIC);

            builder.setAudioAttributes(attrBuild.build());

            mSoundPool = builder.build();
        } else {
            //低版本的构造方法，已经deprecated了
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
    }

    private File writeResponseBodyToDisk(ResponseBody body, String url) {
        try {
            String filename = MD5.md5(url);
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(activity.getCacheDir() + File.separator +
                    filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return futureStudioIconFile;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

}
