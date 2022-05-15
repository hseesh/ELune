package com.yatoufang.test.filetype;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;

/**
 * @author GongHuang（hse）
 * @since 2022/5/9 0009
 */
public class FileTemplateFactory implements FileTemplateGroupDescriptorFactory {

    @Override
    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        FileTemplateGroupDescriptor group = new FileTemplateGroupDescriptor("Mind Map", AllIcons.Nodes.Field);
        group.addTemplate(new FileTemplateDescriptor("New Mind Map.mm", AllIcons.Nodes.Field));
        return group;
    }
}
