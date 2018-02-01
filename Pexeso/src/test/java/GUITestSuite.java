import gui.GUITwoPlayerGameTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import gui.GUISinglePlayerGameTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GUISinglePlayerGameTest.class,
        GUITwoPlayerGameTest.class
})
public class GUITestSuite {
}
