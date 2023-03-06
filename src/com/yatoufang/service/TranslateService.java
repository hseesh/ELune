package com.yatoufang.service;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.yatoufang.entity.translate.Response;
import com.yatoufang.entity.translate.deepl.DeepLResult;
import com.yatoufang.utils.HttpUtils;
import com.yatoufang.utils.Md5;

import java.util.*;

/**
 * @author GongHuang（hse）
 * @since 2022/6/19 0019
 */
public class TranslateService {

    // 百度翻译API
    private static final String URL = "https://fanyi-api.baidu.com/api/trans/vip/translate";
    private static final String APPID = "20220619001252176";
    private static final String KEY = "SBdjN9EaHVsFXIXsRKJn";


    // deepl API
    public static final String DEEPL_URL = "https://api-free.deepl.com/v2/translate";
    public static final String DEEPL_TOKEN = "5db718af-ae30-1991-6ff2-58ed8a965601:fx";

    private static TranslateService service;

    public static TranslateService getInstance() {
        if (service == null) {
            service = new TranslateService();
        }
        return service;
    }

    /**
     * translate words
     * @param querySource target words
     * @return
     * q    string	是	请求翻译query	UTF-8编码
     * from	string	是	翻译源语言	可设置为auto
     * to	string	是	翻译目标语言	不可设置为auto
     * appid	string	是	APPID	可在管理控制台查看
     * salt	string	是	随机数	可为字母或数字的字符串
     * sign	string	是	签名	appid+q+salt+密钥的MD5值
     */
    public String action(String querySource) {
        long key = System.currentTimeMillis();
        HashMap<String, Object> param = Maps.newHashMap();
        String sign = Md5.md5(APPID + querySource + key + KEY);
        param.put("appid", APPID);
        param.put("salt", key);
        param.put("sign", sign);
        param.put("from", "zh");
        param.put("to", "en");
        param.put("q", querySource);
        String result = HttpUtils.sendGet(URL, param, true);
        Gson gson = new Gson();
        Response response = gson.fromJson(result, Response.class);
        String translateResult = response.getTranslateResult();
        if(translateResult == null){
            return querySource;
        }
        return translateResult;
    }


    /**
     * POST /v2/translate HTTP/2
     * Host: api-free.deepl.com
     * Authorization: DeepL-Auth-Key 5db718af-ae30-1991-6ff2-58ed8a965601:fx
     * User-Agent: YourApp/1.2.3
     * Content-Length: 37
     * Content-Type: application/x-www-form-urlencoded
     * text=Hello%2C%20world!&target_lang=DE
     */
    public static String translate(String worlds) {
        HashMap<String, Object> param = Maps.newHashMap();
        param.put("text", worlds);
        param.put("target_lang", "EN");
        param.put("auth_key", DEEPL_TOKEN);
        String result = HttpUtils.sendPost(DEEPL_URL, param, "application/x-www-form-urlencoded", Maps.newHashMap());
        Gson gson = new Gson();
        DeepLResult response = gson.fromJson(result, DeepLResult.class);
        return response.getTranslateResult();
    }

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        //System.out.println(translate("龙神BOSS=五绝塔=天道四灵塔=弑神令=灵草=神晶"));
        System.out.println(translate("攻击"));
        System.out.println(System.currentTimeMillis() - l);
    }

}
