package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MusicianInstrumentUnitTest {
    private MusicianInstrument musicianInstrument;

    private Set<MusicalInstrument> musicalInstruments;

    @BeforeEach
    public void setUp() {
        MusicalInstrument piano = new MusicalInstrument("Piano");
        musicalInstruments = new HashSet<>();
        musicalInstruments.add(piano);
        musicianInstrument = new MusicianInstrument(new Musician("Keith Jarrett"), musicalInstruments);
    }

    @Test
    public void sameNameAndNumberMeansSameAlbum() {
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(new Musician("Keith Jarrett"), musicalInstruments);
        assertEquals(musicianInstrument, musicianInstrument1);
    }

    //boundary test ?
    @Test
    @DisplayName("Musician cannot be null")
    public void musicianCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new MusicianInstrument(null, musicalInstruments));
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
        assertThrows(NullPointerException.class, () -> musicianInstrument.setMusicalInstruments(null));
    }

}