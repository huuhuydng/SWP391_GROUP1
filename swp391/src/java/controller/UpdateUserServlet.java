/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author Hung Bui
 */
@WebServlet(name = "UpdateUserServlet", urlPatterns = {"/UpdateUserServlet"})
public class UpdateUserServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UpdateUserServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateUserServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            User u = (User) session.getAttribute("account");
            if (u == null) {
                response.getWriter().println("No user session found.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            String fullname = request.getParameter("fullname");
            String gender = request.getParameter("gender");
            String dob = request.getParameter("dob");
            String phone = request.getParameter("phone");

            // Kiểm tra số điện thoại có chứa khoảng trắng hay không
            if (phone.contains(" ")) {
                response.getWriter().println("Phone number cannot have white space!");
                request.getRequestDispatcher("userInfo.jsp").forward(request, response);
                return;
            }

            // Cập nhật dữ liệu vào cơ sở dữ liệu
            DAO dao = new DAO();
            boolean isUpdated = dao.updateUser(u.getAcc_id(), fullname, gender, dob, phone);

            // Xử lý kết quả sau khi cập nhật
            if (isUpdated) {
                u.setAcc_fullname(fullname);
                u.setAcc_gender(gender);
                u.setAcc_dob(dob);
                u.setAcc_phone(phone);
                session.setAttribute("account", u);
                request.getRequestDispatcher("userInfo.jsp").forward(request, response);
            } else {
                response.getWriter().println("Update Fail");
            }

        } catch (Exception e) {
            // Xử lý ngoại lệ
            e.printStackTrace();
            response.getWriter().println("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}