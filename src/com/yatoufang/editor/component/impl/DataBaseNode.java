package com.yatoufang.editor.component.impl;

import com.google.common.collect.Maps;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.yatoufang.action.TableScannerAction;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.component.BaseNode;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.Model;
import com.yatoufang.entity.Table;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.BuildUtil;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.utils.StringUtil;

import java.awt.*;
import java.util.Collections;
import java.util.Map;

/**
 * @author hse
 * @since 2022/11/29 0029
 */
public class DataBaseNode extends BaseNode {

    public DataBaseNode(Model model, Point clickedPoint) {
        super(model, clickedPoint, NodeType.DATA_BASE);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.DATA_BASE;
    }

    @Override
    public void refresh(String content) {
        nodeData.refresh(content);
        refreshBounds();
        nodeData.setAlias(nodeData.getMetaData().getName());
        getModel().setModuleName(StringUtil.toLowerCaseForFirstChar(nodeData.getMetaData().getName()));
    }

    @Override
    public Paint getPaint() {
        return new GradientPaint(0.0f, 0.0f, ColorBox.DATABASE_START.getColor(), getWidth(), getHeight(), ColorBox.DATABASE.getColor(), true);
    }

    public Map<String, String> getContent() {
        String content = nodeData.getContent();
        if (content.isEmpty()) {
            return Collections.emptyMap();
        }
        PsiClass aClass = BuildUtil.createClass(content);
        if (aClass == null) {
            return Collections.emptyMap();
        }
        Table table = new Table(aClass.getName() == null ? StringUtil.EMPTY : aClass.getName(), StringUtil.EMPTY);
        table.setFields(PSIUtil.getClassField(aClass));
        table.setComment(PSIUtil.getDescription(aClass.getDocComment()));
        PsiClassType[] extendsListTypes = aClass.getExtendsListTypes();
        for (PsiClassType extendsListType : extendsListTypes) {
            table.setMultiEntity(ProjectKeys.MULTI_ENTITY.equals(extendsListType.getClassName()));
        }
        String dao, daoImpl;
        Map<String, String> result = Maps.newHashMap();
        VelocityService velocityService = VelocityService.getInstance();
        if (table.isMultiEntity()) {
            dao = ProjectKeys.MULTI_ENTITY_TEMPLATE;
            daoImpl = ProjectKeys.MULTI_ENTITY_IMPL_TEMPLATE;
        } else {
            dao = ProjectKeys.SINGLE_ENTITY_TEMPLATE;
            daoImpl = ProjectKeys.SINGLE_ENTITY_IMPL_TEMPLATE;
        }
        dao = velocityService.execute(dao, table);
        daoImpl = velocityService.execute(daoImpl, table);
        result.put(ProjectKeys.DAO, dao);
        result.put(ProjectKeys.DAO + ProjectKeys.IMPL, daoImpl);
        return result;
    }
}
