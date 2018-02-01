import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import parametrized.SaveToPlayerMemoryParametrizedTest;
import parametrized.ComputerPlayerParametrizedTest;
import parametrized.HumanPlayerParametrizedTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SaveToPlayerMemoryParametrizedTest.class,
        HumanPlayerParametrizedTest.class,
        ComputerPlayerParametrizedTest.class
})
public class ParametrizedTestSuite {
}
