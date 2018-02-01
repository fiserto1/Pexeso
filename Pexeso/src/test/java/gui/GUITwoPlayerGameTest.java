package gui;

import org.junit.Test;
import pexeso.gui.HeadFrame;
import util.GUITestUtil;

import javax.swing.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GUITwoPlayerGameTest {

    @Test
    public void testRunGUI() {
        HeadFrame hFrame = new HeadFrame();
        hFrame.setVisible(true);


        //Find single player button
        JButton twoPlayersButton = (JButton) GUITestUtil.getChildNamed(hFrame, "twoPlayersButton");
        assertNotNull(twoPlayersButton);
        assertTrue(twoPlayersButton.isVisible());
        assertEquals("Two players", twoPlayersButton.getText());

        //Click on single player button
        twoPlayersButton.doClick();
        twoPlayersButton = (JButton) GUITestUtil.getChildNamed(hFrame, "twoPlayersButton");
        assertNull(twoPlayersButton);

        //Find start game button
        JButton startGameButton = (JButton) GUITestUtil.getChildNamed(hFrame, "startTwoPlayerGameBut");
        assertNotNull(startGameButton);
        assertTrue(startGameButton.isVisible());
        assertEquals("Start game", startGameButton.getText());

        //Click on start two player game button
        startGameButton.doClick();
        startGameButton = (JButton) GUITestUtil.getChildNamed(hFrame, "startTwoPlayerGameBut");
        assertNull(startGameButton);

        //Find panel with deck of cards
        JPanel centerCardDeckPanel = (JPanel) GUITestUtil.getChildNamed(hFrame, "centerCardDeckPanel");
        assertNotNull(centerCardDeckPanel);
        assertTrue(centerCardDeckPanel.isVisible());
        assertEquals(36, centerCardDeckPanel.getComponentCount());


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
