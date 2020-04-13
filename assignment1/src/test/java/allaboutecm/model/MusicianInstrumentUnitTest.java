package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
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


}