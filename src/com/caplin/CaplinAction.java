package com.caplin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;

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

}
