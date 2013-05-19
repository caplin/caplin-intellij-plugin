package com.caplin;

import com.caplin.util.FileUtil;
import com.caplin.util.Runner;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 19/05/13
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
public class GeneratePrivateMethod extends CaplinAction {
    public void actionPerformed(final AnActionEvent e) {
        Runner.runWriteCommand(e.getProject(), new Runnable() {
            public void run() {
                writePrivateMethod(e);
            }
        });
    }

    private void writePrivateMethod(AnActionEvent e) {
        HashMap data = new HashMap();
        data.put("fullClass", FileUtil.getFullClass(FileUtil.getVirtualFile(e)));
        writeTemplate(e, "privateMethod.ftl", data);
    }
}
