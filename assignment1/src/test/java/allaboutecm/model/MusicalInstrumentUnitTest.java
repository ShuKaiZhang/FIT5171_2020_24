package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MusicalInstrumentUnitTest {
    private MusicalInstrument musicalInstrument;

    @BeforeEach
    public void setUp() {
        musicalInstrument = new MusicalInstrument("Piano");
    }

    @Test
    public void sameNameMeansSameMusicalInstrument() {
        MusicalInstrument musicalInstrument1 = new MusicalInstrument("Piano");
        assertEquals(musicalInstrument, musicalInstrument1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Musical instrument name cannot be null or blank")
    public void albumNameCannotBeNull(String arg) {
        assertThrows(IllegalArgumentException.class, () -> musicalInstrument.setName(arg));
    }
}