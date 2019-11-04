package kcleaner.delete;

import lombok.extern.slf4j.Slf4j;
import kcleaner.config.Configuration;
import org.apache.kafka.clients.admin.AdminClient;

import java.util.Set;

@Slf4j
public class TopicDeleterImpl implements TopicDeleter {
    private final AdminClient adminClient;
    private final Configuration configuration;

    public TopicDeleterImpl(AdminClient adminClient, Configuration configuration) {
        this.adminClient = adminClient;
        this.configuration = configuration;
    }

    @Override
    public void delete(Set<String> topicNames) {
        if (configuration.isDeleteEnabled()) {
            log.info("{} Topic(s) to be deleted: ", topicNames.size());
            topicNames.forEach(it -> log.info("Delete topic: {}", it));
            adminClient.deleteTopics(topicNames);
        } else {
            log.info("Fake run and will not delete anything.");
            topicNames.forEach(it -> log.info("Topic: {} is considered useless", it));
        }
    }
}
