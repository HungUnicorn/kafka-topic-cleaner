package kcleaner.topic.define;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class InternalTopicsTest {

    @Test
    public void shouldGetEqualNames() {
        final Set<String> allTopics = new HashSet<>(asList("_consumer_offsets", "payments", "__confluent_ksql",
                "payment-repartition", "payment--changelog", "payment-rekey", "stream.connect-offsets"));

        InternalTopic internalTopics = new InternalTopic(allTopics);

        final Set<String> expected = new HashSet<>(asList("_consumer_offsets", "__confluent_ksql",
                "payment-repartition", "payment--changelog", "payment-rekey", "stream.connect-offsets"));

        assertEquals(expected, internalTopics.getNames());
    }
}
