package com.caplin;

import com.caplin.util.FileUtil;
import com.caplin.util.Template;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 11/05/13
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
abstract class CaplinAction extends AnAction {

    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(FileUtil.isCaplinApp(e));
    }

    protected void writeTemplate(AnActionEvent e, String template, HashMap data) {
        PsiElement element = FileUtil.createPsiElementFromText(e, Template.INSTANCE.process(template, data));
        PsiFile file = e.getData(LangDataKeys.PSI_FILE);

        PsiElement added = file.add(element);
        file.addAfter(FileUtil.createPsiElementFromText(e, "\n"), added);
        file.addAfter(FileUtil.createPsiElementFromText(e, "\n"), added);

        Document document = e.getData(PlatformDataKeys.EDITOR).getDocument();
        PsiDocumentManager.getInstance(e.getProject()).doPostponedOperationsAndUnblockDocument(document);
    }

}
