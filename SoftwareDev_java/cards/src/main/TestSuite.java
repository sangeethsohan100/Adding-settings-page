package main;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    TestCardGame.class,
    TestCard.class,
    TestCardDeck.class,
    TestPlayerThread.class
})

public class TestSuite {

}