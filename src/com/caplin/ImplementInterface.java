package com.caplin;

import com.caplin.util.DocEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 11/05/13
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class ImplementInterface extends CaplinAction {
    public void actionPerformed(AnActionEvent e) {
        StringBuilder builder = new StringBuilder("\ncaplin.implement(" + getFullClass(e) + ", );\n");
        DocEditor.insertStringAt(e, getConstructorEndOffset(e), builder.toString());
    }
}
