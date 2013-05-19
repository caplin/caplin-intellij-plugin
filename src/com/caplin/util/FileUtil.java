package com.caplin.util;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.javascript.JavaScriptSupportLoader;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 15/05/13
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */
public class FileUtil {
    public static boolean isCaplinApp(VirtualFile virtualFile) {
        VirtualFile parent = virtualFile.getParent();

        boolean hasSrc = false;
        boolean hasApps = false;

        while (parent != null) {
            if (parent.getName().equals("src")) hasSrc = true;
            if (parent.getName().equals("apps")) hasApps = true;

            if (hasSrc && hasApps) {
                return true;
            }

            parent = parent.getParent();
        }

        return false;
    }

    public static boolean isCaplinApp(AnActionEvent event) {
        return isCaplinApp(getVirtualFile(event));
    }

    public static String getNameSpace(VirtualFile virtualFile) {
        String namespace = "";
        VirtualFile parent = virtualFile.getParent();

        while (parent != null) {
            if (parent.getName().equals("src")) {
                return namespace;
            } else {
                namespace = parent.getName() + "." + namespace;
                parent = parent.getParent();
            }
        }

        // No src folder, do not try to build a namespace
        return "";
    }

    public static VirtualFile getVirtualFile(AnActionEvent e) {
        return e.getData(PlatformDataKeys.VIRTUAL_FILE);
    }

    public static String getFullClass(VirtualFile file) {
        return getNameSpace(file) + file.getNameWithoutExtension();
    }

    public static String getFullClass(AnActionEvent event) {
        return getFullClass(getVirtualFile(event));
    }

    /**
     * Returns the end index of the constructor if one is found. If no constuctor is found then 0 is returned.
     * @param text The JavaScript to scan
     * @return
     */
    public static int getConstructorEndOffsetFromText(String text) {
        int brackets = 0;
        boolean started = false;
        boolean ignoringComments = false;

        for (int i = 0; i < text.length(); i++){
            char character = text.charAt(i);
            if (!ignoringComments && character == '{') {
                brackets++;
                started = true;
            } else if (!ignoringComments && character == '}') {
                brackets--;
                if (started) {
                    if (brackets == 0) {
                        if (i+1 < text.length() && text.charAt(i+1) == ';') {
                            return i+2;
                        } else {
                            return i+1;
                        }
                    }
                }
            }  else if (character == '/' && text.charAt(i+1) == '*') {
                i = i + 1;
                ignoringComments = true;
            } else if (character == '*' && text.charAt(i+1) == '/') {
                i = i + 1;
                ignoringComments = false;
            }
        }
        return 0;
    }

    /*
    Get the constructor element from the PsiFile. If no constructor is found just return the first
    javascript block.
     */
    public static PsiElement getConstructorFromPsiFile(PsiElement file) {
        PsiElement[] list = file.getChildren();

        for (int i = 0, l = list.length; i < l; i++) {
            if (list[i].getText().indexOf("function") != -1) {
                return list[i];
            }
        }

        if (list.length > 0) {
            return list[0];
        } else {
            return null;
        }

    }

    public static PsiElement createPsiFileFromText(AnActionEvent e, String code) {
        FileType jsType = FileTypeManager.getInstance().getFileTypeByExtension("js");
        PsiFile file =  PsiFileFactory.getInstance(e.getProject()).createFileFromText("dummy.js", jsType, code);
        return file;
    }

    public static PsiElement createPsiElementFromText(AnActionEvent e, String code) {
        return createPsiFileFromText(e, code).getFirstChild();
    }

    public static VirtualFile getApplicationRoot(VirtualFile file) {
       VirtualFile parent = file.getParent();
       while (parent != null) {
            if (parent.findChild("sdk") != null && parent.findChild("apps") != null) {
                return parent;
            }
            parent = parent.getParent();
       }
       return null;
    }

    public static VirtualFile getApplicationRoot(AnActionEvent event) {
        return getApplicationRoot(getVirtualFile(event));
    }

    public static boolean isInterface(VirtualFile file) throws IOException {
        String contents = null;
        Boolean isInterface = false;
        contents = new String(file.contentsToByteArray());
        if (contents.contains("@interface")) {
            isInterface = true;
        }
        return isInterface;
    }

    public static boolean isJSFile(VirtualFile file) {
        return file != null && file.getExtension() != null && file.getExtension().equals("js");
    }
}
