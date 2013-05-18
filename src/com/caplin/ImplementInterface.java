package com.caplin;

import com.caplin.forms.ChooseInterface;
import com.caplin.listener.SelectionListener;
import com.caplin.util.DocEditor;
import com.caplin.util.FileScanner;
import com.caplin.util.FileUtil;
import com.caplin.util.Runner;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
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

    private void writeInterfaceDeclaration(AnActionEvent e, String interfaces, String selection) {
        PsiFile file = e.getData(LangDataKeys.PSI_FILE);
        PsiElement constructor = FileUtil.getConstructorFromPsiFile(file);
        PsiElement elem = FileUtil.createPsiElementFromText(e, "caplin.implement(" + FileUtil.getFullClass(FileUtil.getVirtualFile(e)) + ", " + selection + ");");

        PsiElement newline = FileUtil.createPsiElementFromText(e, "\n");
        PsiElement added;

        if (constructor != null) {
            added = file.addAfter(elem, constructor);
        } else {
            added = file.add(elem);
        }

        if (added.getPrevSibling() != null && !added.getPrevSibling().getText().equals("\n")) {
            file.addBefore(newline, added);
        }

        if (added.getNextSibling() != null && !added.getNextSibling().getText().equals("\n")) {
            file.addAfter(newline, added);
        }

        PsiDocumentManager.getInstance(e.getProject()).doPostponedOperationsAndUnblockDocument(e.getData(PlatformDataKeys.EDITOR).getDocument());
        DocEditor.appendString(e, interfaces);
    }

    @Override
    public void onSelected(String selection) {
        String filePath = selection.replace('.', '/');
        VirtualFile root = FileUtil.getApplicationRoot(this.event.getData(PlatformDataKeys.VIRTUAL_FILE));

        if (filePath.indexOf("caplin") == 0) {
            VirtualFile interfaceToPass = root.findFileByRelativePath("sdk/libs/javascript/caplin/src/" + filePath + ".js");
            writeInterface(interfaceToPass, selection, this.event);
        } else {
            //TODO: Support client interfaces
        }
    }

    private void writeInterface(VirtualFile interfaceToPass, final String selection, final AnActionEvent event) {

        try {
            String contents = new String(interfaceToPass.contentsToByteArray());
            int constructorEnd = FileUtil.getConstructorEndOffsetFromText(contents);
            final String interfaces = contents.substring(constructorEnd, contents.length()).replace("\r\n", "\n").replace(selection, FileUtil.getFullClass(this.event.getData(PlatformDataKeys.VIRTUAL_FILE)));


            // PsiFile file = e.getData(LangDataKeys.PSI_FILE);

            Runner.runWriteCommand(this.event.getProject(), new Runnable() {
                public void run() {
                    writeInterfaceDeclaration(event, interfaces, selection);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



    }
}
