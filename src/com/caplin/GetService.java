package com.caplin;

import com.caplin.util.FileScanner;
import com.caplin.util.FileUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 21/05/13
 * Time: 12:47
 * To change this template use File | Settings | File Templates.
 */
public class GetService extends CaplinAction {
    public void actionPerformed(AnActionEvent e) {
        FileScanner.getServices(e);
    }
}
