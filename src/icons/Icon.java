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
    javax.swing.Icon ROBOT = IconLoader.getIcon("/icons/robot.png", Icon.class);

    /**
     * menu icon
     */
    javax.swing.Icon EDITOR = IconLoader.getIcon("/image/menu/editor.svg", Icon.class);
    javax.swing.Icon VIEW = IconLoader.getIcon("/image/menu/view.svg", Icon.class);
    javax.swing.Icon VIEW_ALL = IconLoader.getIcon("/image/menu/view_all.svg", Icon.class);
    javax.swing.Icon SHORT_CUT = IconLoader.getIcon("/image/menu/shot_cut.svg", Icon.class);
    javax.swing.Icon ADD_CHILD = IconLoader.getIcon("/image/menu/add_child.svg", Icon.class);


}
