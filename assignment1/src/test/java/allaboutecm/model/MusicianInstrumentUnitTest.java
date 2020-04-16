package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MusicianInstrumentUnitTest {
    private MusicianInstrument musicianInstrument;

    @BeforeEach
    public void setUp() {
        musicianInstrument = new MusicianInstrument(new Musician("Keith Jarrett"), new MusicalInstrument("Piano"));
    }

    @Test
    public void sameNameAndNumberMeansSameAlbum() {
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(new Musician("Keith Jarrett"), new MusicalInstrument("Piano"));
        assertEquals(musicianInstrument, musicianInstrument1);
    }

    //boundary test ?
    @Test
    @DisplayName("Musician cannot be null")
    public void musicianCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new MusicianInstrument(null, new MusicalInstrument("Piano")));
    }

    @Test
    @DisplayName("Musician instrument cannot be null")
    public void musicianInstrumentCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new MusicianInstrument(new Musician("Keith Jarrett"), null));
    }

    @Test
    @DisplayName("Musician and musician instrument cannot be null")
    public void musicianAndMusicianInstrumentCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new MusicianInstrument(null,null));
    }

    @Test
    @DisplayName("Musician cannot be null")
    public void MusicianCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musicianInstrument.setMusician(null));
    }

    @Test
    @DisplayName("Musical instrument cannot be null")
    public void musicalInstrumentCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musicianInstrument.setMusicalInstrument(null));
    }

}