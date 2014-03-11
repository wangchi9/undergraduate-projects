/*
 * Represents a Stop in the system
 */

public class StopBean implements java.io.Serializable {

    private String id;
    private String title;
    private int user_id;

    public StopBean(){
        setId("");
        setTitle("");
        setUserId(0);
    }

    /**
     * @param id the Stop id to set
    */
    public void setId(String id){
        this.id = id;
    }

    /**
     * @return the Stop id
     */
    public String getId(){
        return this.id;
    }

    /**
     * @param title the Stop title to set
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * @return the Stop title
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return user_id;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.user_id = userId;
    }

}
