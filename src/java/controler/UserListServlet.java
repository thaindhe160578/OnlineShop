/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import dal.RoleDAO;
import dal.UserDAO;
import dal.UserStatusDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Role;
import model.UserStatus;
import model.Users;

/**
 *
 * @author ducth
 */
@WebServlet(name = "UserListServlet", urlPatterns = {"/user"})
public class UserListServlet extends HttpServlet {

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
        String action = request.getParameter("action");
        UserDAO u = new UserDAO();
        RoleDAO r = new RoleDAO();
        UserStatusDAO us = new UserStatusDAO();
        if (action == null) {
             
            List<Users> user = u.selectAll();
            List<Role> role = r.selectAll();
            List<UserStatus> userstatus = us.selectAll();
            request.setAttribute("role", role);
            request.setAttribute("userstatus", userstatus);
            int page, numperpage = 6;
            int size = user.size();
            int num = (size % 6 == 0 ? (size / 6) : ((size / 6)) + 1);//so trang
            String xpage = request.getParameter("page");
            if (xpage == null) {
                page = 1;
            } else {
                page = Integer.parseInt(xpage);
            }
            int start, end;
            start = (page - 1) * numperpage;
            end = Math.min(page * numperpage, size);
            List<Users> userList = u.getListByPage(user, start, end);
            request.setAttribute("page", page);
            request.setAttribute("num", num);
            request.setAttribute("user", userList);
            request.getRequestDispatcher("user.jsp").forward(request, response);
        }

        if (action.equals("search")) {
            String searchTxt = request.getParameter("txt");
            List<Users> userSearch = u.searchName(searchTxt);
            request.setAttribute("user", userSearch);
            List<Role> ro = r.selectAll();
            List<UserStatus> userstatus = us.selectAll();
            request.setAttribute("role", ro);
            request.setAttribute("userstatus", userstatus);
            request.getRequestDispatcher("user.jsp").forward(request, response);
            if (searchTxt == null) {
                List<Users> user = u.selectAll();
                List<Role> role = r.selectAll();
                request.setAttribute("role", role);
                int page, numperpage = 6;
                int size = user.size();
                int num = (size % 6 == 0 ? (size / 6) : ((size / 6)) + 1);//so trang
                String xpage = request.getParameter("page");
                if (xpage == null) {
                    page = 1;
                } else {
                    page = Integer.parseInt(xpage);
                }
                int start, end;
                start = (page - 1) * numperpage;
                end = Math.min(page * numperpage, size);
                List<Users> userList = u.getListByPage(user, start, end);
                request.setAttribute("page", page);
                request.setAttribute("num", num);
                request.setAttribute("user", userList);
                request.getRequestDispatcher("user.jsp").forward(request, response);
            }
        }

        if (action.equals("filter")) {
            String role_id = request.getParameter("role_id");
            String status = request.getParameter("status");
            request.setAttribute("role_id", role_id);
            request.setAttribute("status", status);
            if (role_id.equals("all") && status.equals("all")) {
                response.sendRedirect("user");
            } 
            if (role_id.equals("all") && !status.equals("all")) {
                List<Role> role = r.selectAll();
                List<UserStatus> userstatus = us.selectAll();
                request.setAttribute("role", role);
                request.setAttribute("userstatus", userstatus);
                List<Users> list1 = u.getFilterByStatus(status);
                request.setAttribute("user", list1);
                request.getRequestDispatcher("user.jsp").forward(request, response);

            } 
            if (status.equals("all") && !role_id.equals("all")) {
                List<Role> role = r.selectAll();
                List<UserStatus> userstatus = us.selectAll();
                request.setAttribute("role", role);
                request.setAttribute("userstatus", userstatus);
                List<Users> list2 = u.getFilterByRole(role_id);
                request.setAttribute("user", list2);
                request.getRequestDispatcher("user.jsp").forward(request, response);

            } if(!status.equals("all") && !role_id.equals("all")) {

                List<Role> role = r.selectAll();
                List<UserStatus> userstatus = us.selectAll();
                request.setAttribute("role", role);
                request.setAttribute("userstatus", userstatus);
                List<Users> list = u.getFilter(role_id, status);
                request.setAttribute("user", list);
                request.getRequestDispatcher("user.jsp").forward(request, response);

            }
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
        processRequest(request, response);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
