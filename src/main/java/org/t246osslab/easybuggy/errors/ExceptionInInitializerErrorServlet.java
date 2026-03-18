package org.t246osslab.easybuggy.errors;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.servlets.AbstractServlet;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/eie" })
public class ExceptionInInitializerErrorServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            Class<?> cl = Class.forName("org.t246osslab.easybuggy.errors.InitializerErrorThrower");
            Constructor<?> cunstructor = cl.getConstructor();
            cunstructor.newInstance(new Object[] { null });
        } catch (Exception e) {
            log.error("Exception occurs: {}", e); // SECURITY FIX: Use format specifiers instead of string concatenation
        }
    }
}

class InitializerErrorThrower {
    static {
        int divisor = 1; // SECURITY FIX: Avoid division by zero
        if (divisor != 0) { // SECURITY FIX: Check for zero before division
            LoggerFactory.getLogger(InitializerErrorThrower.class).debug("clinit" + (1 / divisor)); // SECURITY FIX: Avoid division by zero
        }
    }
    
    private InitializerErrorThrower(){
        // this constructor is added to suppress sonar advice
    }
}