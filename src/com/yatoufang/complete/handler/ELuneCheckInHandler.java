package com.yatoufang.complete.handler;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.CommitExecutor;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PairConsumer;
import com.yatoufang.service.ConsoleService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author GongHuang(hse)
 * @since 2023/6/3
 */
public class ELuneCheckInHandler extends CheckinHandler {

    @Override
    public ReturnResult beforeCheckin(final CommitExecutor executor,  final PairConsumer<Object, Object> additionalDataConsumer) {
        List<VirtualFile> commitFiles = getFilesToCommit();
        ConsoleService consoleService = ConsoleService.getInstance();
        for (VirtualFile file : commitFiles) {
            consoleService.printInfo(file.getPath());
        }
        return ReturnResult.COMMIT;
    }

    private List<VirtualFile> getFilesToCommit() {
        List<VirtualFile> result = new ArrayList<>();
        final Project project = getCurrentProject();
        if (project != null) {
            final ChangeListManager changeListManager = ChangeListManager.getInstance(project);
            final Collection<Change> changes = changeListManager.getDefaultChangeList().getChanges();
            for (Change change : changes) {
                ContentRevision afterRevision = change.getAfterRevision();
                if (afterRevision != null) {
                    VirtualFile file = afterRevision.getFile().getVirtualFile();
                    if (file != null) {
                        result.add(file);
                    }
                }
            }
        }
        return result;
    }

    private Project getCurrentProject() {
        return CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext());
    }
}
