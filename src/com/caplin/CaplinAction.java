package com.caplin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang.CharUtils;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 11/05/13
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
abstract class CaplinAction extends AnAction {

    public void update(AnActionEvent e) {
        VirtualFile virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        e.getPresentation().setEnabled(isCaplinApp(virtualFile));
    }

    private boolean isCaplinApp(VirtualFile virtualFile) {
        VirtualFile parent = virtualFile.getParent();

        boolean hasSrc = false;
        boolean hasApps = false;

        while (parent != null) {
            if (parent.getName().equals("src")) hasSrc = true;
            if (parent.getName().equals("apps")) hasApps = true;

            if (hasSrc && hasApps) {
                return true;
            }

            parent = parent.getParent();
        }

        return false;
    }

    protected String getNameSpace(VirtualFile virtualFile) {
        String namespace = "";
        VirtualFile parent = virtualFile.getParent();

        while (parent != null && !parent.getName().equals("src")) {
            namespace = parent.getName() + "." + namespace;
            parent = parent.getParent();
        }

        return namespace;
    }

    protected String getFullClass(AnActionEvent e) {
        VirtualFile virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        return getNameSpace(virtualFile) + virtualFile.getNameWithoutExtension();
    }

    protected int getConstructorEndOffset(AnActionEvent e) {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        String text = editor.getDocument().getText();
        int brackets = 0;
        boolean started = false;

        for (int i = 0; i < text.length(); i++){
            char character = text.charAt(i);
            if (character == '{') {
                brackets++;
                started = true;
            } else if (character == '}') {
                brackets--;
                if (started) {
                    if (brackets == 0) {
                        if (text.charAt(i+1) == ';') {
                            return i+2;
                        } else {
                            return i+1;
                        }
                    }
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

}
