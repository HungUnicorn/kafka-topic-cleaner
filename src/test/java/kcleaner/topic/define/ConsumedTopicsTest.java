package kcleaner.topic.define;

import org.apache.kafka.clients.NodeApiVersions;
import org.apache.kafka.clients.admin.AdminClientUnitTestEnv;
import org.apache.kafka.clients.consumer.internals.ConsumerProtocol;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.protocol.Errors;
import org.apache.kafka.common.requests.FindCoordinatorResponse;
import org.apache.kafka.common.requests.ListGroupsResponse;
import org.apache.kafka.common.requests.MetadataResponse;
import org.apache.kafka.common.requests.OffsetFetchResponse;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


public class ConsumedTopicsTest {

    @Test
    public void shouldGetEqualNames_givenConsumedTopic() throws ExecutionException, InterruptedException {
        final HashMap<Integer, Node> nodes = new HashMap<>();
        nodes.put(0, new Node(0, "localhost", 8121));

        final Cluster cluster =
                new Cluster(
                        "mockClusterId",
                        nodes.values(),
                        Collections.emptyList(),
                        Collections.emptySet(),
                        Collections.emptySet(), nodes.get(0));

        try (AdminClientUnitTestEnv env = new AdminClientUnitTestEnv(cluster)) {
            env.kafkaClient().setNodeApiVersions(NodeApiVersions.create());

            env.kafkaClient().prepareResponse(
                    MetadataResponse.prepareResponse(
                            env.cluster().nodes(),
                            env.cluster().clusterResource().clusterId(),
                            env.cluster().controller().id(),
                            Collections.emptyList()));

            env.kafkaClient().prepareResponseFrom(
                    new ListGroupsResponse(
                            Errors.NONE,
                            Collections.singletonList(new ListGroupsResponse.Group("group-1",
                                    ConsumerProtocol.PROTOCOL_TYPE)
                            )),
                    env.cluster().nodes().stream().findFirst().orElse(null));

            env.kafkaClient().prepareResponse(FindCoordinatorResponse.prepareResponse(Errors.NONE, env.cluster().controller()));

            TopicPartition consumedTopicPartition = new TopicPartition("consumed", 0);

            final Map<TopicPartition, OffsetFetchResponse.PartitionData> responseData = new HashMap<>();
            responseData.put(consumedTopicPartition, new OffsetFetchResponse.PartitionData(10,
                    Optional.empty(), "", Errors.NONE));
            env.kafkaClient().prepareResponse(new OffsetFetchResponse(Errors.NONE, responseData));


            final ConsumedTopic consumedTopics = new ConsumedTopic(env.adminClient());

            final Set<String> expected = Stream.of("consumed").collect(Collectors.toSet());
            assertEquals(expected, consumedTopics.getNames());
        }
    }
}
