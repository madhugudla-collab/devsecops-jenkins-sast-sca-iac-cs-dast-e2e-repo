package org.t246osslab.easybuggy.exceptions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.t246osslab.easybuggy.core.servlets.AbstractServlet;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/ae" })
public class ArithmeticExceptionServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int divisor = 0; // SECURITY FIX: Initialize divisor to zero for demonstration
        if (divisor == 0) { // SECURITY FIX: Check for zero before division
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Division by zero is not allowed."); // SECURITY FIX: Handle division by zero
            return;
        }
        res.addIntHeader("ae", 1 / divisor); // SECURITY FIX: Safe division after check
    }
}