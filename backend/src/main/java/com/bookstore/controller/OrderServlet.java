package com.bookstore.controller;

import com.bookstore.dao.OrderDAO;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/orders/*")
public class OrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = resp.getWriter();

        String pathInfo = req.getPathInfo();

        if ("/all".equals(pathInfo)) {
            // Admin: get all orders
            List<Order> orders = orderDAO.getAllOrders();
            out.print(gson.toJson(orders));
        } else {
            // User: get orders by user ID
            String userIdParam = req.getParameter("userId");
            if (userIdParam != null) {
                List<Order> orders = orderDAO.getOrdersByUser(Integer.parseInt(userIdParam));
                out.print(gson.toJson(orders));
            } else {
                out.print("[]");
            }
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = resp.getWriter();

        Order order = gson.fromJson(req.getReader(), Order.class);
        int orderId = orderDAO.placeOrder(order);

        if (orderId > 0) {
            List<OrderItem> items = order.getItems();
            for (OrderItem item : items) {
                item.setOrderId(orderId);
                orderDAO.reduceStock(item.getBookId(), item.getQuantity());
            }
            orderDAO.addOrderItems(items);
            out.print("{\"success\":true, \"orderId\":" + orderId + "}");
        } else {
            out.print("{\"success\":false, \"message\":\"Order placement failed\"}");
        }
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = resp.getWriter();

        String pathInfo = req.getPathInfo();  // e.g. /5/status
        String[] parts = pathInfo.split("/");
        int orderId = Integer.parseInt(parts[1]);
        String status = req.getParameter("status");

        boolean success = orderDAO.updateOrderStatus(orderId, status);
        out.print(success ? "{\"success\":true}" : "{\"success\":false}");
        out.flush();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(200);
    }
}
