package org.t246osslab.easybuggy.errors;

import java.io.IOException;
import java.util.Properties;
import java.security.SecureRandom; // SECURITY FIX: Use SecureRandom for better randomness

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.t246osslab.easybuggy.core.servlets.AbstractServlet;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/oome4" })
public class OutOfMemoryErrorServlet4 extends AbstractServlet {

    private final SecureRandom r = new SecureRandom(); // SECURITY FIX: Save and re-use this SecureRandom

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Properties properties = System.getProperties();
        while (true) {
            properties.put(r.nextInt(), "value");
        }
    }
}