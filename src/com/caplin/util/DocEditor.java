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
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        insertStringAt(e, editor.getDocument().getTextLength(), string );
    }

    public static void insertStringAt(final AnActionEvent e, final int offset, final String string) {
        Runner.runWriteCommand(e.getProject(), new Runnable() {
            public void run() {
                Editor editor = e.getData(PlatformDataKeys.EDITOR);
                editor.getDocument().insertString(offset, string);
                editor.getCaretModel().moveToOffset(offset);
                editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
            }
        });
    }
}
