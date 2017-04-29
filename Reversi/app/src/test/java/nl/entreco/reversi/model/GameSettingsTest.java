package nl.entreco.reversi.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GameSettingsTest {

    @InjectMocks GameSettings subject;

    @Test
    public void itShouldHaveCorrectDefaults() throws Exception {
        assertEquals(8, subject.getBoardSize());
        assertEquals(0, subject.getStartIndex());
        assertEquals(4_000L, subject.getTimeout());

    }
}