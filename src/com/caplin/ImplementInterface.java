package com.caplin;

import com.caplin.forms.ChooseInterface;
import com.caplin.listener.SelectionListener;
import com.caplin.util.DocEditor;
import com.caplin.util.FileScanner;
import com.caplin.util.Runner;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.idea.lang.javascript.psiutil.JSElementFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.*;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 11/05/13
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class ImplementInterface extends CaplinAction implements SelectionListener {

    public AnActionEvent event;

    public void actionPerformed(final AnActionEvent e) {
        this.event = e;
        ChooseInterface dialog = new ChooseInterface(FileScanner.getInterfaces(e), this);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void writeInterfaceDeclaration(AnActionEvent e, String interfaces) {
        PsiFile file = e.getData(LangDataKeys.PSI_FILE);
        PsiElement constructor = getConstructor(file);
        ASTNode elem = getElement("caplin.implement(" + getFullClass(e) + ", );", e);

        PsiElement newline = getElement("\n", e).getPsi();
        PsiElement added = file.addAfter(elem.getPsi(), constructor);

        if (!added.getPrevSibling().getText().equals("\n")) {
            file.addBefore(newline, added);
        }

        if (!added.getNextSibling().getText().equals("\n")) {
            file.addAfter(newline, added);
        }

        DocEditor.appendString(e, interfaces);
    }

    private PsiElement getConstructor(PsiElement file) {
        PsiElement[] list = file.getChildren();
        for (int i = 0, l = list.length; i < l; i++) {
            if (list[i].getText().indexOf("function") != -1) {
                return list[i];
            }
        }
        return list[0];
    }

    private ASTNode getElement(String code, AnActionEvent e) {
        return JSElementFactory.createElementFromText(e.getProject(), code);
    }

    @Override
    public void onSelected(String selection) {
        //To change body of implemented methods use File | Settings | File Templates.

        String path = selection.replace('.', '/');

        VirtualFile root = FileScanner.getRoot(this.event.getData(PlatformDataKeys.VIRTUAL_FILE));

        if (path.indexOf("caplin") == 0) {
            VirtualFile interfaceToPass = root.findFileByRelativePath("sdk/libs/javascript/caplin/src/" + path + ".js");
            writeInterface(interfaceToPass, this.event);
        } else {

        }
    }

    private void writeInterface(VirtualFile interfaceToPass, final AnActionEvent event) {

        try {
            String contents = new String(interfaceToPass.contentsToByteArray());
            int constructorEnd = getConstructorEndOffsetFromText(contents);
            final String interfaces = contents.substring(constructorEnd, contents.length()).replace("\r\n", "\n");
            // PsiFile file = e.getData(LangDataKeys.PSI_FILE);

            Runner.runWriteCommand(this.event.getProject(), new Runnable() {
                public void run() {
                    writeInterfaceDeclaration(event, interfaces);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



    }
}
