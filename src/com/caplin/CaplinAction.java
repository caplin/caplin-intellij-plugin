package com.caplin;

import com.caplin.util.FileUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 11/05/13
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
abstract class CaplinAction extends AnAction {

    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(FileUtil.isCaplinApp(e));
    }

}
