package kcleaner;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import kcleaner.config.Configuration;
import kcleaner.delete.TopicDeleter;
import kcleaner.delete.TopicDeleterImpl;
import kcleaner.find.UnusedTopicsFinder;
import kcleaner.find.UnusedTopicsFinderImpl;
import kcleaner.topic.define.ConsumedTopic;
import kcleaner.topic.define.InternalTopic;
import kcleaner.topic.define.NonEmptyTopic;
import kcleaner.topic.define.ReservedTopic;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static java.util.Arrays.asList;

@Slf4j
public class KafkaTopicCleanerApplication {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final Configuration configuration = new Configuration(System.getenv("BOOTSTRAP_SERVERS"),
                System.getenv("IS_DELETE_ENABLED"));

        final Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getBootStrapServers());

        final AdminClient adminClient = KafkaAdminClient.create(properties);
        final Set<String> allTopicNames = adminClient.listTopics().names().get();

        final ReservedTopic consumedTopic = new ConsumedTopic(adminClient);
        final ReservedTopic internalTopic = new InternalTopic(allTopicNames);
        final ReservedTopic nonEmptyTopic = new NonEmptyTopic(adminClient);

        final UnusedTopicsFinder unusedTopicsFinder = new UnusedTopicsFinderImpl(allTopicNames,
                new HashSet<>(asList(consumedTopic, internalTopic, nonEmptyTopic)));

        final Set<String> unusedTopics = unusedTopicsFinder.getUnusedTopics();

        final TopicDeleter topicDeleter = new TopicDeleterImpl(adminClient, configuration);

        topicDeleter.delete(unusedTopics);

        adminClient.close();
        log.info("Client closed");
    }
}
