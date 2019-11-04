package kcleaner.topic.define;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeLogDirsResult;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.requests.DescribeLogDirsResponse;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


/**
 * Topic that contains data
 *
 * No test exists as it requires Integration Test, or has to fake a lot
 * */

public class NonEmptyTopic implements ReservedTopic {
    private final AdminClient adminClient;

    private final Set<String> topics;

    public NonEmptyTopic(final AdminClient adminClient) throws ExecutionException, InterruptedException {
        this.adminClient = adminClient;
        topics = new HashSet<>();
        setNonEmptyTopics();
    }

    @Override
    public Set<String> getNames() {
        return Collections.unmodifiableSet(this.topics);
    }

    private void setNonEmptyTopics() throws ExecutionException, InterruptedException {
        final DescribeLogDirsResult logDirsResult = getLogDirsResult();

        for (Map.Entry<Integer, Map<String, DescribeLogDirsResponse.LogDirInfo>> nodes :
                logDirsResult.all().get().entrySet()) {
            for (Map.Entry<String, DescribeLogDirsResponse.LogDirInfo> node : nodes.getValue().entrySet()) {
                for (Map.Entry<TopicPartition, DescribeLogDirsResponse.ReplicaInfo> log :
                        node.getValue().replicaInfos.entrySet()) {
                    if (log.getValue().size > 0) {
                        topics.add(log.getKey().topic());
                    }
                }
            }
        }
    }

    private DescribeLogDirsResult getLogDirsResult() throws ExecutionException, InterruptedException {
        return adminClient.describeLogDirs(adminClient.describeCluster()
                .nodes().get().stream().map(Node::id).collect(Collectors.toList()));
    }
}
