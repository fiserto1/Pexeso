import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by Tomas on 23-Jan-16.
 */
public class PexesoTestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(
                JUnitTestSuite.class,
                ParametrizedTestSuite.class,
                ProcessTestSuite.class,
                CRUDTestSuite.class
        );

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
