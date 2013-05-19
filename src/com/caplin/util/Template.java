package com.caplin.util;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 19/05/13
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class Template {
    public final static Template INSTANCE = new Template();
    private Configuration cfg;

    private Template() {
        try {
            cfg = new Configuration();
            cfg.setDirectoryForTemplateLoading(new File("D:/plugins/CodeCompletion/templates"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String process(String templateFile, HashMap data) {
        try {
            freemarker.template.Template template = cfg.getTemplate(templateFile);
            StringWriter processedTemplate = new StringWriter();
            template.process(data, processedTemplate);
            return processedTemplate.getBuffer().toString().replace("\r", "");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (TemplateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";
    }
}
