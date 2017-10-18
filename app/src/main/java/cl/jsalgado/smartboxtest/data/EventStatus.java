package cl.jsalgado.smartboxtest.data;

/**
 * Created by joels on 16-10-2017.
 *
 */

public class EventStatus {

    private String id;
    private StatusName name;
    private String category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StatusName getName() {
        return name;
    }

    public void setName(StatusName name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}