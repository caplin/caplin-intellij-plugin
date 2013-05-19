package com.caplin;

import com.caplin.util.DocEditor;
import com.caplin.util.FileUtil;
import com.caplin.util.Runner;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.intellij.idea.lang.javascript.psiutil.JSElementFactory;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 11/05/13
 * Time: 11:36
 * To change this template use File | Settings | File Templates.
 */
public class GenerateMethod extends CaplinAction {
    public void actionPerformed(final AnActionEvent e) {

        Runner.runWriteCommand(e.getProject(), new Runnable() {
            public void run() {
                writeClass(e);
            }
        });
    }

    private void writeClass(AnActionEvent e) {
        PsiFile file = e.getData(LangDataKeys.PSI_FILE);
        String fullClass = FileUtil.getFullClass(FileUtil.getVirtualFile(e));

        final PsiElement methodComment = FileUtil.createPsiElementFromText(e, "/**\n * \n*/");
        file.add(methodComment);

        String code = fullClass + ".prototype.method=function(){};";
        PsiElement methodElement = FileUtil.createPsiElementFromText(e, code);

        Document document = e.getData(PlatformDataKeys.EDITOR).getDocument();

        PsiElement added = file.add(methodElement);

        file.addAfter(FileUtil.createPsiElementFromText(e, "\n"), added);

        PsiDocumentManager.getInstance(e.getProject()).doPostponedOperationsAndUnblockDocument(document);
    }
}
