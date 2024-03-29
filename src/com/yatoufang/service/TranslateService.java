package com.yatoufang.service;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.yatoufang.entity.translate.Response;
import com.yatoufang.entity.translate.deepl.DeepLResult;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.utils.HttpUtils;
import com.yatoufang.utils.Md5;
import com.yatoufang.utils.StringUtil;

import java.util.*;

/**
 * @author GongHuang（hse）
 * @since 2022/6/19 0019
 */
public class TranslateService {

    // 百度翻译API
    private static final String URL = "https://fanyi-api.baidu.com/api/trans/vip/translate";
    private static final String ULTIMATE_URL = "https://fanyi-api.baidu.com/api/trans/vip/fieldtranslate";
    private static final String APPID = "20220619001252176";
    private static final String KEY = "SBdjN9EaHVsFXIXsRKJn";
    public static final String ERROR_CODE = "54003";


    // deepl API
    public static final String DEEPL_URL = "https://api-free.deepl.com/v2/translate";
    public static final String DEEPL_TOKEN = "5db718af-ae30-1991-6ff2-58ed8a965601:fx";


    /**
     * translate words
     *
     * @param querySource target words
     * @return q    string	是	请求翻译query	UTF-8编码
     * from	string	是	翻译源语言	可设置为auto
     * to	string	是	翻译目标语言	不可设置为auto
     * appid	string	是	APPID	可在管理控制台查看
     * salt	string	是	随机数	可为字母或数字的字符串
     * sign	string	是	签名	appid+q+salt+密钥的MD5值
     */
    public static String translate(String querySource) {
        long key = System.currentTimeMillis();
        HashMap<String, Object> param = Maps.newHashMap();
        String sign = Md5.md5(APPID + querySource + key + "novel" + KEY);
        param.put("appid", APPID);
        param.put("salt", key);
        param.put("sign", sign);
        param.put("from", "zh");
        param.put("to", "en");
        param.put("q", querySource);
        param.put("domain", "novel");
        return doTranslate(querySource, param, ULTIMATE_URL);
    }

    private static String doTranslate(String querySource, HashMap<String, Object> param, String ultimateUrl) {
        String result = HttpUtils.sendGet(ultimateUrl, param, true);
        Gson gson = new Gson();
        Response response = gson.fromJson(result, Response.class);
        String translateResult = response.getTranslateResult();
        if (translateResult == null ) {
            if (ERROR_CODE.equals(response.getErrorCode())) {
                NotifyService.notifyErrorInfo(NotifyKeys.REQUEST_ERROR);
            }
            return querySource;
        }
        return translateResult;
    }

    public static String translate(String querySource, boolean fromChines, boolean toEnglish) {
        long key = System.currentTimeMillis();
        HashMap<String, Object> param = Maps.newHashMap();
        String sign = Md5.md5(APPID + querySource + key + KEY);
        param.put("appid", APPID);
        param.put("salt", key);
        param.put("sign", sign);
        param.put("q", querySource);
        param.put("from", fromChines ? "zh" : "en");
        param.put("to", toEnglish ? "en" : "zh");
        return doTranslate(querySource, param, URL);
    }


    /**
     * POST /v2/translate HTTP/2
     * Host: api-free.deepl.com
     * Authorization: DeepL-Auth-Key 5db718af-ae30-1991-6ff2-58ed8a965601:fx
     * User-Agent: YourApp/1.2.3
     * Content-Length: 37
     * Content-Type: application/x-www-form-urlencoded
     * text=Hello%2C%20world!&target_lang=DE
     * ZH - Chinese
     */
    public static String translates(String worlds) {
        HashMap<String, Object> param = Maps.newHashMap();
        param.put("text", worlds);
        param.put("target_lang", "EN");
        param.put("auth_key", DEEPL_TOKEN);
        String result = HttpUtils.sendPost(DEEPL_URL, param, "application/x-www-form-urlencoded", Maps.newHashMap());
        Gson gson = new Gson();
        DeepLResult response = gson.fromJson(result, DeepLResult.class);
        if (response == null) {
            return StringUtil.EMPTY;
        }
        return response.getTranslateResult();
    }

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        System.out.println(translate("资源类型", true,true));
        System.out.println(System.currentTimeMillis() - l);
    }

}
