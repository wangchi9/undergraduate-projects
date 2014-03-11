import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

/**
 * Register servlet, used for registration and username validation
 */
public class Register extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

	String auth = "false";  // user is logged in
        int status = 0;
        boolean available = false; // stores the availability of the username
	String nextPage = request.getParameter("page");
	System.out.println(nextPage);
    HttpSession session = request.getSession(true);
        // get the userBean populated by register.jsp from the form submission
        //UserBean user = (UserBean)request.getAttribute("registerbean");
    UserBean user = (UserBean) request.getAttribute("registerbean");
        // if the userbean doesn't exists, create it
        if (user == null) {
        user = new UserBean();
        session.setAttribute("registerbean", user);
        }
        
        String username = request.getParameter(PublicConstants.USERNAME_PARAM);
        String password = request.getParameter(PublicConstants.PASSWORD_PARAM);
        String firstname = request.getParameter(PublicConstants.FIRSTNAME_PARAM);
        String lastname = request.getParameter(PublicConstants.LASTNAME_PARAM);
        String email = request.getParameter(PublicConstants.EMAIL_PARAM);
        String phone = request.getParameter(PublicConstants.PHONE_PARAM);
        // record the username and password values in a User Bean
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPhone(phone);
        // in case malicious user posts these values
        user.setLoggedIn(false);
        user.setId(0);




	System.out.println(user.getUsername());

        // check availability
        available = checkAvailability(user) == 1;
	System.out.println(available);
        
        // attempt to register the user if the username is available 
        if (available){
            // register user
            status = register(user);

            if (status == 1){ // if user successfully registered log them in

                //HttpSession session = request.getSession(true);
                user.setLoggedIn(true); // log the user in
                session.setAttribute(PublicConstants.USERBEAN_ATTR, user);
                // since user is now logged in, we will make a new empty stop list
                HashMap<String, StopBean> mystopsList = new HashMap<String, StopBean>();
                // they may have saved stops as a guest before registering
                HashMap<String, StopBean> unsaved = (HashMap<String, StopBean>) session.getAttribute("mystopList");

                if (unsaved != null && !unsaved.isEmpty()){
                    this.saveMystopsList(unsaved, user.getId()); // save the stop list
                    mystopsList.putAll(unsaved); // add them to the current stoplist
                    System.out.println("DEBUG: Adding guest mystopsList for new registered user!");
                }

                // store the playlist hashmap to the session
                session.setAttribute("mystopsList", mystopsList);

		auth = "true";
                System.out.println("DEBUG: Registered User!");
            }
        }
        else  {
            System.out.println("DEBUG: Did not register!");
            status = (available) ? 1: 0;
        }

	response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println("{\"available\":\"" + available
            + "\", \"authenticated\":\"" + auth 
            + "\", \"status\":\"" + status
	    + "\", \"page\":\"" +nextPage + "\"}");
    }

    /*
     * Given a user, attempt to register the user
     * @return int success or fail
     */
    private int register(UserBean user){

        Connection con;
        int rowsEffected;
        rowsEffected = 0;

        // use prepared statement for security
        String searchQuery = "INSERT INTO Users (username, password, first_name, last_name, email, phone) VALUES" 
        + " (?,?,?,?,?,?)";

        // connect to db and retrieve routes
        try {
            // get connection pool
            DataSource dbcp = (DataSource)getServletContext().getAttribute("dbpool");
            con = dbcp.getConnection();

            PreparedStatement searchQueryP = con.prepareStatement(searchQuery,PreparedStatement.RETURN_GENERATED_KEYS);

            // set the query parameters appropriately
            searchQueryP.setString(1, user.getUsername());
            searchQueryP.setString(2, user.getPassword());
            searchQueryP.setString(3, user.getFirstname());
            searchQueryP.setString(4, user.getLastname());
            searchQueryP.setString(5, user.getEmail());
            searchQueryP.setString(6, user.getPhone());

            rowsEffected = searchQueryP.executeUpdate();

            // get the id of the newly-registered user
            // from the autogenerated key from previous insert
            ResultSet rs = searchQueryP.getGeneratedKeys();

            if(rs != null && rs.next()){ // if key was generated
                user.setId(rs.getInt(1)); // set the new id
            }

            // close prep statement
            searchQueryP.close();

            //close db connection
            con.close();

        }

        catch(SQLException ex) {
          System.err.println("SQLException: " + ex.getMessage());
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        return rowsEffected;

    }

    /*
     * returns 1 if username is available 0 if not.
     */
    private int checkAvailability(UserBean user){

        Connection con;
        int status;
        status = 1;

        String searchQuery = "SELECT * FROM Users WHERE username = ?";

        // connect to db and retrieve routes
        try {
            // get connection pool
            DataSource dbcp = (DataSource)getServletContext().getAttribute("dbpool");
            con = dbcp.getConnection();

            PreparedStatement searchQueryP = con.prepareStatement(searchQuery);

            // set the username
            searchQueryP.setString(1, user.getUsername());  // case sensitive

            ResultSet rs = searchQueryP.executeQuery();

            if (rs.next()){ // if record is found username exists
                status = 0; //unavailable
            }

            // close prep statement
            searchQueryP.close();

            //close db connection
            con.close();

        }

        catch(SQLException ex) {
          System.err.println("SQLException: " + ex.getMessage());
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        return status;

    }

    /*
     * given a hashmap of playlist and a userid, save the playlist for the user
     */
    private int saveMystopsList(HashMap<String, StopBean> mystopsList, int userid){

        Connection con;
        int rowsEffected;
        rowsEffected = 0;

        String searchQuery = "INSERT INTO MyStops (stopid, stop_title, user_id) VALUES"
                + "(?,?,?)";

        // connect to db and retrieve routes
        try {
            // get connection pool
            DataSource dbcp = (DataSource)getServletContext().getAttribute("dbpool");
            con = dbcp.getConnection();

            PreparedStatement searchQueryP = con.prepareStatement(searchQuery);

            Iterator iterator = mystopsList.keySet().iterator();

            while(iterator.hasNext()){ // for each stop

                String key = (String)iterator.next();
                // populate the prepared statement params with the StopBean
                StopBean stop = mystopsList.get(key);
                searchQueryP.setString(1, stop.getId());
                searchQueryP.setString(2, stop.getTitle());
                searchQueryP.setInt(3, userid);

                rowsEffected = searchQueryP.executeUpdate();
	System.out.println("DEBUG, saving new user stopslist: " + stop.getTitle());
            }



            // close prep statement
            searchQueryP.close();


            //close db connection
            con.close();

        }

        catch(SQLException ex) {
          System.err.println("SQLException: " + ex.getMessage());
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        return rowsEffected;

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
        return "Create User Accounts";
    }// </editor-fold>

}
