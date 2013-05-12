package com.caplin.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 12/05/13
 * Time: 18:36
 * To change this template use File | Settings | File Templates.
 */
public class FileScanner {

    public static ArrayList<String> getInterfaces(AnActionEvent e) {
        ArrayList<String> interfaces = new ArrayList<String>();
        if (e == null) {
            interfaces.add("dummy.grid.GridViewListener");
            interfaces.add("dummy.chart.ChartViewListener");
            interfaces.add("dummy.grid.ColumnModelListener");
        } else {
            interfaces = scanAndReturnInterfaces(e);
        }
        return interfaces;
    }

    private static ArrayList<String> scanAndReturnInterfaces(AnActionEvent e) {
        ArrayList<String> interfaces = new ArrayList<String>();
        VirtualFile virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        VirtualFile root = getRoot(virtualFile);

        if (root != null) {
            collectInterfaces(root.findChild("apps"), interfaces);
            collectInterfaces(root.findChild("sdk"), interfaces);
        }

        return interfaces;
    }

    private static void collectInterfaces(VirtualFile file, ArrayList<String> interfaces) {
        VirtualFile[] children = file.getChildren();
        for (int i = 0, l = children.length; i < l; i++) {
            if (!children[i].isDirectory()) {
                addIfInterface(children[i], interfaces);
            } else {
                collectInterfaces(children[i], interfaces);
            }
        }
    }

    private static void addIfInterface(VirtualFile child, ArrayList<String> interfaces) {
        if (child.getExtension() != null && child.getExtension().equals("js")) {
            try {
                String contents = new String(child.contentsToByteArray());
                if (contents.indexOf("@interface") != -1) {
                    interfaces.add(child.getNameWithoutExtension());
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private static VirtualFile getRoot(VirtualFile file) {
       VirtualFile parent = file.getParent();
       while (parent != null) {
            if (parent.findChild("sdk") != null && parent.findChild("apps") != null) {
                return parent;
            }
            parent = parent.getParent();
       };
       return null;
    }


}
