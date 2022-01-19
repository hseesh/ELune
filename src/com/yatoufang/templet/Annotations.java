package com.yatoufang.templet;

/**
 * @author hse
 * @Date: 2021/1/20
 */
public interface Annotations {

    String RESTCONTROLLER = "org.springframework.web.bind.annotation.RestController";

    String CONTROLLER = "org.springframework.stereotype.Controller";

    String POSTMAPPING = "org.springframework.web.bind.annotation.PostMapping";

    String GETMAPPING = "org.springframework.web.bind.annotation.GetMapping";

    String DELETEMAPPING = "org.springframework.web.bind.annotation.DeleteMapping";

    String PUTMAPPING = "org.springframework.web.bind.annotation.PutMapping";

    String REQUESTMAPPING = "org.springframework.web.bind.annotation.RequestMapping";

    String REQUESTBODY = "org.springframework.web.bind.annotation.RequestBody";

    String REQUESTPARAM = "org.springframework.web.bind.annotation.RequestParam";

    String PATHVARIABLE = "org.springframework.web.bind.annotation.PathVariable";

    String SERVICE = "org.springframework.stereotype.Service";

    String TABLE = "cn.daxiang.framework.database.annotation.Table";

    String COLUMN = "cn.daxiang.framework.database.annotation.Column";

    String CMD = "cn.daxiang.framework.router.annotation.Cmd";

    String DATA_FILE = "cn.daxiang.framework.dataconfig.annotation.DataFile";

    String FILED_IGNORE = "cn.daxiang.framework.dataconfig.annotation.FieldIgnore";
}
