package pexeso.players;

import org.junit.Before;
import org.junit.Test;
import pexeso.games.OneMove;

import static org.junit.Assert.*;

public class HumanPlayerTest {


    HumanPlayer player;
    OneMove oneMove;

    @Before
    public void setUp() throws Exception {
        player = new HumanPlayer("Tomas", null, 1);
        oneMove = new OneMove(1, 2);
    }

    @Test
    public void testMove() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    fail();
                }
                player.setMyMove(oneMove);
            }
        });
        thread.start();

        OneMove move = player.move(null, null, 0);

        assertEquals(oneMove, move);

    }

    @Test
    public void testInterruptedMove() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OneMove move = player.move(null, null, 0);

                assertNull(move);
            }
        });

        thread.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }

        thread.interrupt();
    }
}