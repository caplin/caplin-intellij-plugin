package com.caplin;

import com.caplin.util.DocEditor;
import com.caplin.util.FileUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 11/05/13
 * Time: 11:36
 * To change this template use File | Settings | File Templates.
 */
public class GenerateMethod extends CaplinAction {
    public void actionPerformed(AnActionEvent e) {
        StringBuilder builder = new StringBuilder("\n\n" + FileUtil.getFullClass(FileUtil.getVirtualFile(e)) + ".prototype.method = new function(){\n}");
        DocEditor.appendString(e, builder.toString());
    }
}
