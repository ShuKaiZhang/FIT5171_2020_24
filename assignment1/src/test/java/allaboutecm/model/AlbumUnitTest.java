package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    @DisplayName("Record Number should start with ECM")
    public void recordNumberStartWithECM() {
        assertThrows(IllegalArgumentException.class, () -> album.setRecordNumber("QWR 1231"));
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
    public void sameNameAndNumberMeansSameAlbum() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");

        assertEquals(album, album1);
    }
}