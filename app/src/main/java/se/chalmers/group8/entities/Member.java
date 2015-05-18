package se.chalmers.group8.entities;

/**
 * Created by Matthias on 06.05.2015.
 */
public class Member {
    private String id;
    private String name;
    private String initials;

    public Member(String id, String name, String initials){
        this.id = id;
        this.name = name;
        this.initials = initials;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }
}
