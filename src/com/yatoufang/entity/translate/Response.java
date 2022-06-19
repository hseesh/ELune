package com.yatoufang.entity.translate;

import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/6/19 0019
 */
public class Response {
    private List<Result> trans_result;

    private String error_code;

    private String error_msg;

    public List<Result> getResult() {

        return trans_result;
    }

    public void setResult(List<Result> result) {
        this.trans_result = result;
    }

    public String getErrorCode() {
        return error_code;
    }

    public void setErrorCode(String errorCode) {
        this.error_code = errorCode;
    }

    public String getErrorMsg() {
        return error_msg;
    }

    public void setErrorMsg(String errorMsg) {
        this.error_msg = errorMsg;
    }

    public String getTranslateResult(){
        if(trans_result != null && trans_result.size() > 0){
            return trans_result.get(0).getDst();
        }
        return null;
    }

}
