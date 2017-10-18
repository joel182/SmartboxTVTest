package cl.jsalgado.smartboxtest.data;

import java.util.List;

/**
 * Created by joels on 16-10-2017.
 *
 */

public class Data {

    private List<Section> sections = null;
    private List<Item> items = null;
    private Pagination pagination;

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}