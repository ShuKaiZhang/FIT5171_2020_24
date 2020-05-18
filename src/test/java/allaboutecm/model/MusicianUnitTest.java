package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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



}