package cl.jsalgado.smartboxtest.auth;

/**
 * Created by joels on 16-10-2017.
 *
 */

public class User {

    private String id;
    private Rbac rbac;
    private Profile profile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rbac getRbac() {
        return rbac;
    }

    public void setRbac(Rbac rbac) {
        this.rbac = rbac;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

}