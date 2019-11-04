package kcleaner.topic.define;


import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Topic that is consumed.
 * <p>
 * If a consumer group have not consumed anything for a while,
 * the consumer group is automatically clean up by Kafka
 * based on  offset.retention setting , which is 7 days by default
 */

public class ConsumedTopic implements ReservedTopic {
    private final Set<String> names;

    private final AdminClient client;

    public ConsumedTopic(AdminClient client) throws ExecutionException, InterruptedException {
        names = new HashSet<>();
        this.client = client;

        final Collection<ConsumerGroupListing> consumerGroupListings = client.listConsumerGroups().all().get();
        consumerGroupListings.forEach(group -> {
            try {
                final Set<String> topics = getTopics(group.groupId());
                names.addAll(topics);
            } catch (ExecutionException | InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public Set<String> getNames() {
        return Collections.unmodifiableSet(names);
    }

    private Set<String> getTopics(String groupId) throws ExecutionException, InterruptedException {
        final ListConsumerGroupOffsetsResult listConsumerGroupOffsetsResult = client.listConsumerGroupOffsets(groupId);
        final Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadata = listConsumerGroupOffsetsResult
                .partitionsToOffsetAndMetadata().get();

        return topicPartitionOffsetAndMetadata.keySet().stream().map(
                TopicPartition::topic).collect(Collectors.toSet());

    }
}
