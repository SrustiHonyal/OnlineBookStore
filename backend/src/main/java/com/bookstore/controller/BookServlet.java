package com.bookstore.controller;

import com.bookstore.dao.BookDAO;
import com.bookstore.model.Book;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/books/*")
public class BookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = resp.getWriter();

        String pathInfo = req.getPathInfo();
        String search = req.getParameter("search");
        String categoryParam = req.getParameter("category");

        if (search != null && !search.isEmpty()) {
            List<Book> books = bookDAO.searchBooks(search);
            out.print(gson.toJson(books));
        } else if (categoryParam != null) {
            List<Book> books = bookDAO.getBooksByCategory(Integer.parseInt(categoryParam));
            out.print(gson.toJson(books));
        } else if (pathInfo != null && !pathInfo.equals("/")) {
            int id = Integer.parseInt(pathInfo.substring(1));
            Book book = bookDAO.getBookById(id);
            out.print(book != null ? gson.toJson(book) : "{\"error\":\"Book not found\"}");
        } else {
            List<Book> books = bookDAO.getAllBooks();
            out.print(gson.toJson(books));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = resp.getWriter();

        Book book = gson.fromJson(req.getReader(), Book.class);
        boolean success = bookDAO.addBook(book);
        out.print(success ? "{\"success\":true}" : "{\"success\":false}");
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = resp.getWriter();

        Book book = gson.fromJson(req.getReader(), Book.class);
        boolean success = bookDAO.updateBook(book);
        out.print(success ? "{\"success\":true}" : "{\"success\":false}");
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = resp.getWriter();

        String pathInfo = req.getPathInfo();
        int id = Integer.parseInt(pathInfo.substring(1));
        boolean success = bookDAO.deleteBook(id);
        out.print(success ? "{\"success\":true}" : "{\"success\":false}");
        out.flush();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(200);
    }
}
