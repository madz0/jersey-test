package com.github.madz0.revolut;

import com.github.madz0.revolut.config.AppConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.ServletException;
import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main().start();
    }

    public void start() throws ServletException, LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);

        Context context = tomcat.addWebapp("/", new File(".").getAbsolutePath());

        Tomcat.addServlet(context, "jersey-container-servlet", resourceConfig());
        //context.addServletMapping("/*", "jersey-container-servlet");

        tomcat.start();
        tomcat.getServer().await();
    }

    private ServletContainer resourceConfig() {
        return new ServletContainer(new AppConfig());
    }
}