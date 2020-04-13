package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlbumUnitTest {
       private Album album;

    @BeforeEach
    public void setUp() {
        album = new Album(1975, "ECM 1064/65", "The Köln Concert");
    }

    @Test
    @DisplayName("Album name cannot be null")
    public void albumNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setAlbumName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Album name cannot be empty or blank")
    public void albumNameConnotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setAlbumName(arg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"QWE 1231", "ECM1231"})
    @DisplayName("Record Number should start with 'ECM '")
    public void recordNumberStartWithECM(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setRecordNumber(arg));
    }

    @ParameterizedTest
    @ValueSource(ints = {2022, 1})
    @DisplayName("Release year must be four digital number")
    public void releaseYearMustBeFourDigitalNumber(int arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setReleaseYear(arg));
    }

    @Test
    @DisplayName("Tracks cannot be blank")
    public void TracksCannotBeBlank() {
        List<String> arg = new ArrayList<>();
        arg.add("");
        arg.add("");
        assertThrows(IllegalArgumentException.class, () -> album.setTracks(arg));
    }

    @Test
    @DisplayName("Tracks cannot be null")
    public void TracksCannotBeNull() {
        List<String> arg = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> album.setTracks(arg));
    }

    @Test
    @DisplayName("Featured musicians cannot be null")
    public void FeaturedMusiciansCannotBeNull() {
        Set<Musician> arg = new HashSet();
        assertThrows(IllegalArgumentException.class, () -> album.setFeaturedMusicians(arg));
    }

    @Test
    @DisplayName("Instruments cannot be null")
    public void InstrumentsCannotBeNull() {
        Set<MusicianInstrument> arg = new HashSet();
        assertThrows(IllegalArgumentException.class, () -> album.setInstruments(arg));
    }

    @Test
    public void sameNameAndNumberMeansSameAlbum() {
        Album album1 = new Album(1975, null, "The Köln Concert");
        assertEquals(album, album1);
    }

}