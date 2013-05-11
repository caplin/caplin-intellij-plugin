package com.caplin;

import com.caplin.util.DocEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 11/05/13
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public class GenerateClass extends CaplinAction {

    public void actionPerformed(final AnActionEvent e) {
        StringBuilder builder = new StringBuilder(getFullClass(e) + " = function() {\n}");
        builder.append("\n\n" + getFullClass(e) + ".prototype.method = new function(){}");
        DocEditor.insertString(e, builder.toString());
    }

}

