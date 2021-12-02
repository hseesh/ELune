package com.yatoufang.test.dao;

import com.intellij.openapi.util.io.FileUtil;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Table;
import com.yatoufang.templet.ProjectKey;
import com.yatoufang.utils.DateUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.NullLogChute;

import java.io.IOException;
import java.io.StringWriter;
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

        Table ido = new Table("ido", "ido");
        ido.setComment("神像");
        ido.setPrimaryKey(new Field("idoId"));
        ido.setValueOf("(String name, String alias, String typeAlias)");
        context.put("now", DateUtil.now());
        context.put("table", ido);
        context.put("author","GongHuang(hse)");


        StringWriter stringWriter = new StringWriter();

        String text = "";
        try {
            //text = FileUtil.loadTextAndClose(Objects.requireNonNull(Test.class.getResourceAsStream("/templates/SingleDaoTemplate.vm")));
            //text = FileUtil.loadTextAndClose(Objects.requireNonNull(Test.class.getResourceAsStream("/templates/MultiDaoImplTemplate.vm")));
            text = FileUtil.loadTextAndClose(Objects.requireNonNull(Test.class.getResourceAsStream(ProjectKey.SINGLE_ENTITY_IMPL_TEMPLATE)));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Velocity.evaluate(context, stringWriter, "myLog", text);
        System.out.println(stringWriter);
    }
}
