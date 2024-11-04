package com.github.zzzxb.annotation.service;

import com.github.zzzxb.annotation.pojo.TaskInfo;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zzzxb
 * 2024/11/4
 */
public class AnnotationService {
    private final String COMMENT = "-- ";

    public void runTask(Editor editor, Project project, Document document) {
        List<Runnable> runnableList = selectLineComment(editor, document);
        if(!runnableList.isEmpty()) {
            for (Runnable runnable : runnableList) {
                WriteCommandAction.runWriteCommandAction(project, runnable);
            }
        }
    }

    private List<Runnable> selectLineComment(Editor editor, Document document) {
        List<Runnable> runnableList = new LinkedList<>();
        SelectionModel selectionModel = editor.getSelectionModel();
        VisualPosition selectionStartPosition = selectionModel.getSelectionStartPosition();
        VisualPosition selectionEndPosition = selectionModel.getSelectionEndPosition();
        if (selectionStartPosition != null && selectionEndPosition != null) {
            int startLine = selectionStartPosition.getLine();
            int endLine = selectionEndPosition.getLine();
            int delta = endLine - startLine;
            int increment = 0;
            for (int i = 0; i <= delta; i++) {
                int line = startLine + i;
                TaskInfo taskInfo = singleLineOperation(document, line, increment);
                increment = increment + taskInfo.incr;
                runnableList.add(taskInfo.runnable);
            }
        }
        return runnableList;
    }

    private TaskInfo singleLineOperation(Document document, int currentLine, int increment) {
        int lineStartOffset = document.getLineStartOffset(currentLine);
        int lineEndOffset = document.getLineEndOffset(currentLine);
        return singleLineOffsetOperation(document, lineStartOffset, lineEndOffset, increment);
    }

    private TaskInfo singleLineOffsetOperation(Document document, int currentLineStartOffset, int currentLineEndOffset, int increment) {
        Runnable runnable = null;
        int inc = 0;
        String text = document.getText(new TextRange(currentLineStartOffset, currentLineEndOffset));
        if (text.trim().startsWith(COMMENT)) {
            String replaced = text.replaceFirst(COMMENT, "");
            runnable = () -> document.replaceString(currentLineStartOffset + increment, currentLineEndOffset + increment, replaced);
            inc = -COMMENT.length();
        } else if (text.trim().startsWith(COMMENT.trim())) {
            String replaced = text.replaceFirst(COMMENT.trim(), "");
            runnable = () -> document.replaceString(currentLineStartOffset + increment, currentLineEndOffset + increment, replaced);
            inc = -COMMENT.trim().length();
        }else {
            runnable = () -> document.insertString(currentLineStartOffset + increment, COMMENT);
            inc = COMMENT.length();
        }
        return new TaskInfo(runnable, inc);
    }
}
