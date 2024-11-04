package com.github.zzzxb.annotation.action;

import com.github.zzzxb.annotation.service.AnnotationService;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * @author zzzxb
 * 2024/11/4
 */
public class AnnotationAction extends AnAction {
    AnnotationService annotationService = new AnnotationService();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Project project = editor.getProject();
        Document document = editor.getDocument();
        if (project != null) annotationService.runTask(editor, project, document);

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        boolean menuAllowed = false;
        if (editor != null && project != null) {
            menuAllowed = !editor.getCaretModel().getAllCarets().isEmpty();
        }
        e.getPresentation().setEnabledAndVisible(menuAllowed);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return super.getActionUpdateThread();
    }
}
