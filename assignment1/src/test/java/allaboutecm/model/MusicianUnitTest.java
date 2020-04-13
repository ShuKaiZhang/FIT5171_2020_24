package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MusicianUnitTest {

    private Musician musician;

    @BeforeEach
    public void setUp() {
        musician = new Musician("Keith Jarrett");
    }

    @Test
    public void sameNameMeansSameMusician() {
        Musician musician1 = new Musician("Keith Jarrett");
        assertEquals(musician, musician1);
    }

    @Test
    @DisplayName("Musician name must be a name format")
    public void albumNameCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new Musician("KeithJarrett"));
    }
}