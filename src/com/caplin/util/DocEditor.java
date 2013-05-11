package com.caplin.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 */
public class DocEditor {

    public static void insertString(final AnActionEvent e, final String string) {

        Runner.runWriteCommand(e.getProject(), new Runnable() {
            public void run() {
                Editor editor = e.getData(PlatformDataKeys.EDITOR);
                int offset = editor.getCaretModel().getOffset();
                editor.getDocument().insertString(offset, string);
            }
        });

    }

    public static void appendString(final AnActionEvent e, final String string) {
        Runner.runWriteCommand(e.getProject(), new Runnable() {
            public void run() {
                Editor editor = e.getData(PlatformDataKeys.EDITOR);
                editor.getDocument().insertString(editor.getDocument().getTextLength(), string);
                editor.getCaretModel().moveToOffset(editor.getDocument().getTextLength());
                editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
            }
        });
    }
}
