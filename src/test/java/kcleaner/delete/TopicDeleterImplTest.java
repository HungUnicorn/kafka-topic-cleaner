package kcleaner.delete;

import kcleaner.config.Configuration;
import org.apache.kafka.clients.admin.AdminClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TopicDeleterImplTest {

    @Mock
    private Configuration configuration;

    @Mock
    private AdminClient adminClient;

    @Test
    public void shouldDelete() {
        when(configuration.isDeleteEnabled()).thenReturn(true);

        Set<String> topicsToDelete = new HashSet<>(Collections.singletonList("test"));

        new TopicDeleterImpl(adminClient, configuration).delete(topicsToDelete);

        verify(adminClient, times(1)).deleteTopics(topicsToDelete);
    }

    @Test
    public void shouldNotDelete() {
        when(configuration.isDeleteEnabled()).thenReturn(false);

        Set<String> topicsToDelete = new HashSet<>(Collections.singletonList("test"));

        new TopicDeleterImpl(adminClient, configuration).delete(topicsToDelete);

        verify(adminClient, never()).deleteTopics(topicsToDelete);
    }
}
