package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

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

    @Test
    @DisplayName("Album URL cannot be null")
    public void albumURLCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setAlbumURL(null));
    }

    @Test
    @DisplayName("Album name length cannot be more than 100")
    public void albumNameCannotTooLong() {
        StringBuilder str = new StringBuilder(101);
        assertThrows(IllegalArgumentException.class, () -> album.setAlbumName(str.toString()));
    }


    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Album name cannot be empty or blank")
    public void albumNameConnotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setAlbumName(arg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Record number cannot be empty or blank")
    public void recordNumberConnotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setRecordNumber(arg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"QWE 1231", "ECM1231"})
    @DisplayName("Record Number should start with 'ECM '")
    public void recordNumberStartWithECM(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setRecordNumber(arg));
    }

    @ParameterizedTest
    @ValueSource(ints = {2021, 2300, 1899, 1})
    @DisplayName("Release year must be four digital number")
    public void releaseYearMustBeFourDigitalNumber(int arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setReleaseYear(arg));
    }


    @Test
    @DisplayName("Tracks cannot be null")
    public void TracksCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setTracks(null));
    }

    @Test
    @DisplayName("Featured musicians cannot be blank")
    public void FeaturedMusiciansCannotBeNull() {
        List<Musician> arg = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> album.setFeaturedMusicians(arg));
    }

    @Test
    @DisplayName("Instruments cannot be blank")
    public void InstrumentsCannotBeNull() {
        Set<MusicianInstrument> arg = new HashSet();
        assertThrows(IllegalArgumentException.class, () -> album.setInstruments(arg));
    }



    @Test
    public void sameNameAndNumberMeansSameAlbum() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        assertEquals(album, album1);
    }

    //boundary test ?
    @Test
    @DisplayName("Record number cannot be null")
    public void recordNumberCannotBeNull_init() {
        assertThrows(NullPointerException.class, () -> new Album(1975, null,"The Köln Concert" ));
    }

    @Test
    @DisplayName("Record number cannot be null")
    public void nameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new Album(1975, "ECM 1064/65",null ));
    }

    @Test
    @DisplayName("Album name and record number cannot be null")
    public void albumNameAndRecordNumberCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new Album(1975, null,null ));
    }
}