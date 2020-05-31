package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

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

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t", "KeithJarrett", " KeithJarrett "})
    @DisplayName("Musician name must be a name format")
    public void musicianNameMustBeAName(String arg) {
        assertThrows(IllegalArgumentException.class, () -> new Musician(arg));
    }

    @Test
    @DisplayName("Albums cannot be blank")
    public void albumsCannotBeBlank() {
        Set<Album> arg = new HashSet<>();
        arg.add(null);
        assertThrows(IllegalArgumentException.class, () -> musician.setAlbums(arg));
    }

    @Test
    @DisplayName("Musician name cannot be null")
    public void albumsCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musician.setAlbums(null));
    }

    @Test
    @DisplayName("Musician name cannot be null")
    public void musicianNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new Musician(null));
    }

//another team's code.

    @Test
    @DisplayName("Musician name can not be null")
    public void muscicanNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musician.setName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Musician name can not be blank or empty")
    public void musicianNameCannotBeBlank(String arg)
    {
        assertThrows(IllegalArgumentException.class, () -> musician.setName(arg));
    }
    @Test
    @DisplayName("name must start at uppercase")
    public void musicianNameMustStartAtUpperCase()
    {
        assertThrows(IllegalArgumentException.class, () -> musician.setName("frank"));
    }

    @Test
    @DisplayName("name can not more than 30 characters")
    public void musicianNameCannotMoreThan30()
    {
        assertThrows(IllegalArgumentException.class, () -> musician.setName("qweqhahdhqwehqklyheyqoweqwehiuqyewqiweqiwe"));
    }
    @Test
    @DisplayName("url can be null")
    public void urlCanBeNull()
    {
        assertNull(musician.getMusicianUrl());
    }
}