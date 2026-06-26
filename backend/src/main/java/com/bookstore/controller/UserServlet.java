package com.bookstore.controller;

import com.bookstore.dao.UserDAO;
import com.bookstore.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/users/*")
public class UserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = resp.getWriter();

        String pathInfo = req.getPathInfo();

        if ("/register".equals(pathInfo)) {
            User user = gson.fromJson(req.getReader(), User.class);
            JsonObject result = new JsonObject();

            if (userDAO.usernameExists(user.getUsername())) {
                result.addProperty("success", false);
                result.addProperty("message", "Username already exists");
            } else if (userDAO.emailExists(user.getEmail())) {
                result.addProperty("success", false);
                result.addProperty("message", "Email already registered");
            } else {
                boolean success = userDAO.registerUser(user);
                result.addProperty("success", success);
                result.addProperty("message", success ? "Registration successful" : "Registration failed");
            }
            out.print(result);

        } else if ("/login".equals(pathInfo)) {
            JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);
            String username = body.get("username").getAsString();
            String password = body.get("password").getAsString();

            User user = userDAO.loginUser(username, password);
            JsonObject result = new JsonObject();
            if (user != null) {
                result.addProperty("success", true);
                result.addProperty("userId", user.getId());
                result.addProperty("username", user.getUsername());
                result.addProperty("fullName", user.getFullName());
                result.addProperty("email", user.getEmail());
                result.addProperty("role", user.getRole());
            } else {
                result.addProperty("success", false);
                result.addProperty("message", "Invalid username or password");
            }
            out.print(result);
        }
        out.flush();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(200);
    }
}
