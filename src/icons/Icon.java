package icons;

import com.intellij.openapi.util.IconLoader;

/**
 * @author GongHuang（hse）
 * @since 2022/2/9
 */
public interface Icon {
    javax.swing.Icon EDIT = IconLoader.getIcon("/icons/edit.svg", Icon.class);
    javax.swing.Icon CLEAR = IconLoader.getIcon("/icons/clear.svg", Icon.class);
    javax.swing.Icon RUN = IconLoader.getIcon("/icons/generator.svg", Icon.class);
    javax.swing.Icon PUSH = IconLoader.getIcon("/image/push.svg", Icon.class);
}
