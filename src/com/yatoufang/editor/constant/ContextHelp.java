package com.yatoufang.editor.constant;

import com.google.common.collect.Maps;
import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.editor.model.MetaData;
import com.yatoufang.entity.Param;
import com.yatoufang.templet.Expression;
import com.yatoufang.templet.NativeWords;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.DataUtil;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hse
 * @since 2023/2/8 0008
 */
public class ContextHelp {

    public static Map<String, Collection<Param>> buildContext(List<AbstractNode> nodes) {
        Map<String, Collection<Param>> result = Maps.newHashMap();
        Collection<Param> defaultList = Lists.newArrayList();
        String list = List.class.getSimpleName();
        String map = Map.class.getSimpleName();
        for (AbstractNode node : nodes) {
            if (node.getNodeData().getAlias().isEmpty()) {
                continue;
            }
            MetaData metaData = node.getNodeData().getMetaData();
            result.put(metaData.getName(), metaData.getPramList());
            String alias = node.getNodeData().getAlias();
            String paramName = StringUtil.toLowerCaseForFirstChar(alias);
            Param listParam = new Param(paramName + list);
            listParam.setTypeAlias(String.format(Expression.COLLECTION_TYPE, alias));
            listParam.setDescription(metaData.getDescription() + NativeWords.ARRAY);
            listParam.setDefaultValue(DataUtil.getInitValue(list));
            defaultList.add(listParam);
            Param key = metaData.getKey();
            if (key == null) {
                continue;
            }
            String description = String.format(Expression.MAP_DES_TYPE, key.getDescription(), metaData.getDescription());
            Param mapParam = new Param(paramName + map);
            mapParam.setTypeAlias(String.format(Expression.MAP_TYPE, key.getTypeAlias(), alias));
            mapParam.setDescription(metaData.getDescription() + description + map);
            mapParam.setDefaultValue(DataUtil.getInitValue(map));
            defaultList.add(mapParam);
            //self
            Param self = new Param(paramName);
            self.setTypeAlias(alias);
            self.setDescription(metaData.getDescription());
            defaultList.add(mapParam);
        }
        result.put(ProjectKeys.DEFAULT_NODE, defaultList);
        return result;
    }
}
