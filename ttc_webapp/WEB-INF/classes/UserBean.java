public class UserBean {

    /** DB User table id for this username */
    private int id;

    /** the username */
    private String username;

        /** the password */
    private String password;

    /** the first name  */
    private String firstname;

        /** the last name */
    private String lastname;

    /** the email */
    private String email;

        /** the phone */
    private String phone;

    /** whether the user is logged in or not */
    private boolean loggedIn;

    /**
     * Get the user id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Set the user id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the user name
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Set the user name
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    public String getFirstname() {
        return this.firstname;
    }

    /**
     * Set the user name
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    /**
     * Set the user name
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


        public String getEmail() {
        return this.email;
    }

    /**
     * Set the user name
     */
    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return this.phone;
    }

    /**
     * Set the user name
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }


    /**
     * Business method to actually perform login.
     */
    public boolean doLogin() {

        setLoggedIn(false);
        if (username != null && password != null)
            if (username.equals("cscc09f12") && password.equals("Secret123"))
                setLoggedIn(true);

        return getLoggedIn();
    }

    /**
     * Set whether we are currently logged in or not
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * Determine if we are currently logged in or not
     */
    public boolean getLoggedIn() {
        return loggedIn;
    }
}
