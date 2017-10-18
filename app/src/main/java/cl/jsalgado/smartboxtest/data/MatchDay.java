package cl.jsalgado.smartboxtest.data;

/**
 * Created by joels on 16-10-2017.
 *
 */

public class MatchDay {

    private String start;
    private String end;
    private Name name;
    private Phase phase;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

}