package allaboutecm.model;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public class Group extends Entity {
    private String name;
    private List<Musician> musicians;

    public Group(String name, List<Musician> musicians) {
        this.name = name;
        this.musicians = musicians;
    }


    public List<Musician> getMusicians() {
        return musicians;
    }

    public void setMusicians(List<Musician> musicians) {
        notNull(musicians);
        this.musicians = musicians;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        notNull(name);
        notBlank(name);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group that = (Group) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public void setsetMusicianUrl(String s) {

    }
}
