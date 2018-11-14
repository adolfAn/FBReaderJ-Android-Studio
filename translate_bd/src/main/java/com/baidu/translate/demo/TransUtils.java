package com.baidu.translate.demo;

public class TransUtils {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20181022000223183";
    private static final String SECURITY_KEY = "CS5vZa3BKyz9NqG29X4S";

    public static void requestNet(final String src_word, final OnGetResult listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TransApi api = new TransApi(APP_ID, SECURITY_KEY);
                listener.onGetResult(decode(api.getTransResult(src_word, "auto", "zh")));

            }
        }).start();
    }

    private static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }


}
