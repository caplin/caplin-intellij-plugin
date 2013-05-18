package com.caplin.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
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
        } else {
            interfaces = scanAndReturnInterfaces(FileUtil.getVirtualFile(e));
        }
        return interfaces;
    }

    private static ArrayList<String> scanAndReturnInterfaces(VirtualFile virtualFile) {
        ArrayList<String> interfaces = new ArrayList<String>();
        VirtualFile root = FileUtil.getApplicationRoot(virtualFile);

        if (root != null) {
            collectInterfaces(root.findChild("apps"), interfaces);
            collectInterfaces(root.findFileByRelativePath("sdk/libs/javascript/caplin"), interfaces);
        }

        return interfaces;
    }

    private static void collectInterfaces(VirtualFile file, ArrayList<String> interfaces) {
        if (file != null) {
            VirtualFile[] children = file.getChildren();
            for (int i = 0, l = children.length; i < l; i++) {
                if (!children[i].isDirectory()) {
                    addIfInterface(children[i], interfaces);
                } else {
                    collectInterfaces(children[i], interfaces);
                }
            }
        }

    }

    private static void addIfInterface(VirtualFile child, ArrayList<String> interfaces) {
        try {
            if (FileUtil.isJSFile(child) && FileUtil.isInterface(child)) {
                interfaces.add(FileUtil.getNameSpace(child) + child.getNameWithoutExtension());
            }
        } catch (IOException e) {
            // If there is a file error, simply do not add the interface
        }
    }

}
