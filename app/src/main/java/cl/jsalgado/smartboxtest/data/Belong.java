package cl.jsalgado.smartboxtest.data;

import java.util.List;

/**
 * Created by joels on 16-10-2017.
 *
 */

public class Belong {

    private String client;
    private List<String> accessGroup = null;
    private List<String> tournament = null;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public List<String> getAccessGroup() {
        return accessGroup;
    }

    public void setAccessGroup(List<String> accessGroup) {
        this.accessGroup = accessGroup;
    }

    public List<String> getTournament() {
        return tournament;
    }

    public void setTournament(List<String> tournament) {
        this.tournament = tournament;
    }

}