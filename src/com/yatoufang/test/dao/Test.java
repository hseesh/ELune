package com.yatoufang.test.dao;

import com.intellij.openapi.util.io.FileUtil;
import com.yatoufang.action.TableScannerAction;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Table;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author GongHuang（hse）
 * @since 2021/11/20 20:05
 */
public class Test {
    public static void main(String[] args) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("runtime.log.logsystem.class", NullLogChute.class.getName());
        velocityEngine.init();
        VelocityContext context = new VelocityContext();

        Table ido = new Table("ido");
        ido.setComment("神像");
        ido.setPrimaryKey(new Field("idoId"));
        ido.setValueOf("(String name, String alias, String typeAlias)");
        context.put("table", ido);


        StringWriter stringWriter = new StringWriter();

        String text = "";
        try {
            //text = FileUtil.loadTextAndClose(Objects.requireNonNull(Test.class.getResourceAsStream("/templates/DaoTemplate.vm")));
            //text = FileUtil.loadTextAndClose(Objects.requireNonNull(Test.class.getResourceAsStream("/templates/MultiDaoImplTemplate.vm")));
            text = FileUtil.loadTextAndClose(Objects.requireNonNull(Test.class.getResourceAsStream("/templates/SingleDaoImplTemplate.vm")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Velocity.evaluate(context, stringWriter, "myLog", text);
        System.out.println(stringWriter);
    }
}
