package com.caplin;

import com.caplin.util.FileUtil;
import com.caplin.util.Runner;
import com.caplin.util.Template;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 11/05/13
 * Time: 11:36
 * To change this template use File | Settings | File Templates.
 */
public class GeneratePublicMethod extends CaplinAction {
    public void actionPerformed(final AnActionEvent e) {
        Runner.runWriteCommand(e.getProject(), new Runnable() {
            public void run() {
                writePublicMethod(e);
            }
        });
    }

    private void writePublicMethod(AnActionEvent e) {
        HashMap data = new HashMap();
        data.put("fullClass", FileUtil.getFullClass(FileUtil.getVirtualFile(e)));
        writeTemplate(e, "method.ftl", data);
    }
}
