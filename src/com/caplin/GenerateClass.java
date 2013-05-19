package com.caplin;

import com.caplin.util.DocEditor;
import com.caplin.util.FileUtil;
import com.caplin.util.Runner;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 11/05/13
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public class GenerateClass extends CaplinAction {

    public void actionPerformed(final AnActionEvent e) {
        Runner.runWriteCommand(e.getProject(), new Runnable() {
            public void run() {
                writeClass(e);
            }
        });
    }

    private void writeClass(AnActionEvent e) {
        final PsiFile file = e.getData(LangDataKeys.PSI_FILE);
        String fullClass = FileUtil.getFullClass(FileUtil.getVirtualFile(e));

        final PsiElement classComment = FileUtil.createPsiElementFromText(e, "/**\n * @constructor\n*/");
        file.add(classComment);

        final PsiElement classDefinition = FileUtil.createPsiElementFromText(e, fullClass + "=function(){};");
        PsiElement added = file.add(classDefinition);

        file.addAfter(FileUtil.createPsiElementFromText(e, "\n"), added);

        Document document = e.getData(PlatformDataKeys.EDITOR).getDocument();
        PsiDocumentManager.getInstance(e.getProject()).doPostponedOperationsAndUnblockDocument(document);
    }

}

