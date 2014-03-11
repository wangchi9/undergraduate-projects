import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

/*
 * Stoplist servlet, retrieves the user's stoplist Stops
 */
public class MystopsList extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        // get the Stops in the session
        // these should reflect the database if the user is logged in

        HashMap<String, StopBean> StopsMap = (HashMap<String, StopBean>)session.getAttribute("mystopsList");

        // attach it to the current request only, since we dont need this across multiple requests
        request.setAttribute("mystopsList", StopsMap);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/mystopsList.jsp");
        if(dispatcher!=null){
            System.out.println("list request");
        dispatcher.forward(request, response);}

    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    public String getServletInfo() {
        return "Disstop user's Stoplist";
    }// </editor-fold>

}
