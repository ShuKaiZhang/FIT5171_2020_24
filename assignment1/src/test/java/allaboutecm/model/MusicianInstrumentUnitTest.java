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
        assertThrows(IllegalArgumentException.class, () -> new MusicianInstrument(new Musician(null), new MusicalInstrument("Piano")));
    }

    @Test
    @DisplayName("Musician instrument cannot be null")
    public void musicianInstrumentCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new MusicianInstrument(new Musician("Keith Jarrett"), new MusicalInstrument(null)));
    }

    @Test
    @DisplayName("Musician and musician instrument cannot be null")
    public void musicianAndMusicianInstrumentCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new MusicianInstrument(new Musician(null), new MusicalInstrument(null)));
    }


}