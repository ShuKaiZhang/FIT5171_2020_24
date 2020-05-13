package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("Musical instrument name cannot blank")
    public void musicalInstrumentCannotBeBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> musicalInstrument.setName(arg));
    }

    @Test
    @DisplayName("Name cannot be null")
    public void nameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new MusicalInstrument(null ));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Name cannot be blank")
    public void nameCannotBeBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> new MusicalInstrument(arg ));
    }
}