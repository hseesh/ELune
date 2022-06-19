package com.yatoufang.service;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.yatoufang.entity.translate.Response;
import com.yatoufang.utils.HttpUtils;
import com.yatoufang.utils.Md5;
import com.yatoufang.utils.StringUtil;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author GongHuang（hse）
 * @since 2022/6/19 0019
 */
public class TranslateService {

    // 百度翻译API
    private static final String URL = "https://fanyi-api.baidu.com/api/trans/vip/translate";
    private static final  String APPID = "";
    private static final  String KEY = "";

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
        String replace = translateResult.replace(" ", StringUtil.UNDER_LINE);
        return replace.toUpperCase(Locale.ROOT);
    }

}
