package gui;

import org.junit.Test;
import pexeso.gui.HeadFrame;
import util.GUITestUtil;

import javax.swing.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GUISinglePlayerGameTest {

    @Test
    public void testRunGUI() {
        HeadFrame hFrame = new HeadFrame();
        hFrame.setVisible(true);


        //Find single player button
        JButton singlePlayerButton = (JButton) GUITestUtil.getChildNamed(hFrame, "singlePlayerButton");
        assertNotNull(singlePlayerButton);
        assertTrue(singlePlayerButton.isVisible());
        assertEquals("Single player", singlePlayerButton.getText());

        //Click on single player button
        singlePlayerButton.doClick();
        singlePlayerButton = (JButton) GUITestUtil.getChildNamed(hFrame, "singlePlayerButton");
        assertNull(singlePlayerButton);

        //Find start game button
        JButton startGameButton = (JButton) GUITestUtil.getChildNamed(hFrame, "startGameButton");
        assertNotNull(startGameButton);
        assertTrue(startGameButton.isVisible());
        assertEquals("Start game", startGameButton.getText());

        //Click on start game button
        startGameButton.doClick();
        startGameButton = (JButton) GUITestUtil.getChildNamed(hFrame, "startGameButton");
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
