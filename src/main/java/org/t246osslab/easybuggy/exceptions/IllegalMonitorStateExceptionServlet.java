package org.t246osslab.easybuggy.exceptions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.t246osslab.easybuggy.core.servlets.AbstractServlet;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/imse" })
public class IllegalMonitorStateExceptionServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Thread thread = new Thread();
        thread.start();
        synchronized (thread) { // SECURITY FIX: Move wait() into synchronized block to hold the monitor
            try {
                thread.wait(); // SECURITY FIX: Ensure wait() is called within synchronized block
            } catch (InterruptedException e) {
                log.error("InterruptedException occurs: ", e);
            }
        }
    }
}