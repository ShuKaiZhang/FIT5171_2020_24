package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GroupUnitTest {
    private Group group;
    private ArrayList<Musician> jan;

    @BeforeEach
    public void setUp() {
        Musician musician1 = new Musician("Jan Garbarek");
        Musician musician2 = new Musician("Terje Rypdal");
        Musician musician3 = new Musician("Arild Andersen");
        Musician musician4 = new Musician("Jon Christensen");
        jan= new ArrayList<Musician>();
        jan.add(musician1);
        jan.add(musician2);
        jan.add(musician3);
        jan.add(musician4);
        group = new Group("Jan Garbarek Quartet", jan);
    }

    @Test
    public void sameNameMeansSameGroup() {
        Group group1 = new Group("Jan Garbarek Quartet", jan);
        assertEquals(group, group1);
    }

    @Test
    @DisplayName("Group name cannot be null")
    public void groupNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> group.setName(null));
    }

    @Test
    @DisplayName("Group musician cannot be null")
    public void groupMusicianCannotBeNull() {
        assertThrows(NullPointerException.class, () -> group.setMusicians(null));
    }

}