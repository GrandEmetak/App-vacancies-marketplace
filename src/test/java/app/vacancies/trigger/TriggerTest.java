package app.vacancies.trigger;

import org.junit.Assert;
import org.junit.Test;

public class TriggerTest {
    @Test
    public void test() {
        Assert.assertEquals(1, new Trigger().someLogic());
    }
}
