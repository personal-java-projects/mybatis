package com.example.listener;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener, ServletContextAttributeListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.getProperties().remove("log4jDirKey");
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        String log4jDir=arg0.getServletContext().getRealPath("/");
        System.setProperty("log4jDirKey", log4jDir);
    }

    @Override
    public void attributeAdded(ServletContextAttributeEvent servletContextAttributeEvent) {

    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent servletContextAttributeEvent) {

    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent servletContextAttributeEvent) {

    }
}
