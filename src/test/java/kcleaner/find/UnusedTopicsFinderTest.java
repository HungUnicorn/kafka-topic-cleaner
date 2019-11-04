package kcleaner.find;

import kcleaner.topic.define.ReservedTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UnusedTopicsFinderTest {

    @Mock
    private ReservedTopic reservedTopics1;

    @Mock
    private ReservedTopic reservedTopics2;

    @Test
    public void shouldGetUnusedTopics() {
        when(reservedTopics1.getNames()).thenReturn(new HashSet<>(asList("payment-refunds", "payment-attempts")));

        when(reservedTopics2.getNames()).thenReturn(new HashSet<>(asList("__consumer.offsets",
                "streams.connect.offsets")));

        Set<String> allTopics = new HashSet<>(asList("--from-beginning", "test123", "__consumer.offsets",
                "streams.connect.offsets",
                "payment-refunds", "payment-attempts"));

        final UnusedTopicsFinderImpl unusedTopicsFinder = new UnusedTopicsFinderImpl(allTopics,
                new HashSet<>(asList(reservedTopics1, reservedTopics2)));

        assertEquals(new HashSet<>(asList("--from-beginning", "test123")), unusedTopicsFinder.getUnusedTopics());
    }

    @Test
    public void shouldGetNoUnusedTopics() {
        when(reservedTopics1.getNames()).thenReturn(Collections.emptySet());

        when(reservedTopics2.getNames()).thenReturn(Collections.emptySet());

        Set<String> allTopics = new HashSet<>(asList("--from-beginning", "test123", "__consumer.offsets",
                "streams.connect.offsets",
                "payment-refunds", "payment-attempts"));

        final UnusedTopicsFinderImpl unusedTopicsFinder = new UnusedTopicsFinderImpl(allTopics,
                new HashSet<>(asList(reservedTopics1, reservedTopics2)));

        assertTrue(unusedTopicsFinder.getUnusedTopics().isEmpty());
    }
}
