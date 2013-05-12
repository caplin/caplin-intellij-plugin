package com.caplin;

import com.caplin.forms.ChooseInterface;
import com.caplin.util.FileScanner;
import com.caplin.util.Runner;
import com.intellij.lang.ASTNode;
import org.intellij.idea.lang.javascript.psiutil.JSElementFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.*;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 11/05/13
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class ImplementInterface extends CaplinAction {
    public void actionPerformed(final AnActionEvent e) {
        ChooseInterface dialog = new ChooseInterface(FileScanner.getInterfaces(e));
        dialog.pack();
        dialog.setVisible(true);

        Runner.runWriteCommand(e.getProject(), new Runnable() {
            public void run() {
                writeInterfaceDeclaration(e);
            }
        });
    }



    private void writeInterfaceDeclaration(AnActionEvent e) {
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
}
