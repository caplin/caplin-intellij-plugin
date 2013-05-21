package com.caplin.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            interfaces = scanAndReturnInterfaces(e);
        }
        return interfaces;
    }

    public static HashSet<String> getServices(AnActionEvent e) {
        HashSet<String> availableServices = new HashSet<String>();
        if (e == null) {
            availableServices.add("caplin.service.Service");
        } else {
            availableServices = scanAndReturnServices(e);
        }
        return availableServices;
    }

    private static HashSet<String> scanAndReturnServices(AnActionEvent e) {
        HashSet<String> services = new HashSet<String>();
        VirtualFile root = FileUtil.getApplicationRoot(e);

        if (root != null) {
            collectServices(root.findChild("apps"), services);
            collectServices(root.findFileByRelativePath("sdk/libs/javascript/caplin"), services);
        }

        return services;
    }

    private static ArrayList<String> scanAndReturnInterfaces(AnActionEvent e) {
        ArrayList<String> interfaces = new ArrayList<String>();
        VirtualFile root = FileUtil.getApplicationRoot(e);

        if (root != null) {
            collectInterfaces(root.findChild("apps"), interfaces);
            collectInterfaces(root.findFileByRelativePath("sdk/libs/javascript/caplin"), interfaces);
        }

        return interfaces;
    }

    private static void collectServices(VirtualFile file, HashSet<String> services) {
        if (file != null) {
            VirtualFile[] children = file.getChildren();
            for (int i = 0, l = children.length; i < l; i++) {
                if (!children[i].isDirectory()) {
                    findAndAddServices(children[i], services);
                } else {
                    collectServices(children[i], services);
                }
            }
        }

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

    private static void findAndAddServices(VirtualFile child, HashSet<String> services) {
        try {
            if (FileUtil.isJSFile(child)) {

                String contents = new String(child.contentsToByteArray());
                Pattern pattern = Pattern.compile("registerService[(]\".*\"");
                Matcher matcher = pattern.matcher(contents);

                while (matcher.find()) {
                    String match = contents.substring(matcher.start(), matcher.end());
                    match = match.replace("registerService(", "");
                    match = match.replace("\"", "");
                    services.add(match);
                }
            }
        } catch (IOException e) {
            // If there is a file error, simply do not add the interface
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
