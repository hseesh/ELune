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


    /**
     * editor menu
     */
    javax.swing.Icon ITEM_EDIT = IconLoader.getIcon("/image/editorMenu/item_editor.svg", Icon.class);
    javax.swing.Icon ITEM_EXPORT = IconLoader.getIcon("/image/editorMenu/item_export.svg", Icon.class);
    javax.swing.Icon ITEM_SAVE = IconLoader.getIcon("/image/editorMenu/item_save.svg", Icon.class);
    javax.swing.Icon ITEM_RENAME = IconLoader.getIcon("/image/editorMenu/item_rename.svg.svg", Icon.class);
    javax.swing.Icon ITEM_ZOOM_IN = IconLoader.getIcon("/image/editorMenu/item_zoom_in.svg", Icon.class);
    javax.swing.Icon ITEM_ZOOM_OUT = IconLoader.getIcon("/image/editorMenu/item_zoom_out.svg", Icon.class);
    javax.swing.Icon NODE_CONFIG = IconLoader.getIcon("/image/editorMenu/node_config.svg", Icon.class);
    javax.swing.Icon NODE_DATA_BASE = IconLoader.getIcon("/image/editorMenu/node_data_base.svg", Icon.class);
    javax.swing.Icon NODE_PUSH = IconLoader.getIcon("/image/editorMenu/node_push.svg", Icon.class);
    javax.swing.Icon NODE_ENTITY = IconLoader.getIcon("/image/editorMenu/node_entity.svg", Icon.class);
    javax.swing.Icon NODE_RETURN = IconLoader.getIcon("/image/editorMenu/node_return.svg", Icon.class);
    javax.swing.Icon NODE_SETTING = IconLoader.getIcon("/image/editorMenu/node_setting.svg", Icon.class);
}
