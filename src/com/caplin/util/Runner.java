package com.caplin.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;

public class Runner
{
    public static void runReadCommand(Project project, Runnable cmd)
    {
        CommandProcessor.getInstance().executeCommand(project, new ReadAction(cmd), "Foo", "Bar");
    }

    public static void runWriteCommand(Project project, Runnable cmd)
    {
        CommandProcessor.getInstance().executeCommand(project, new WriteAction(cmd), "Foo", "Bar");
    }

    static class ReadAction implements Runnable
    {
        ReadAction(Runnable cmd)
        {
            this.cmd = cmd;
        }

        public void run()
        {
            ApplicationManager.getApplication().runReadAction(cmd);
        }

        Runnable cmd;
    }

    static class WriteAction implements Runnable
    {
        WriteAction(Runnable cmd)
        {
            this.cmd = cmd;
        }

        public void run()
        {
            ApplicationManager.getApplication().runWriteAction(cmd);
        }

        Runnable cmd;
    }

    private Runner() {}
}