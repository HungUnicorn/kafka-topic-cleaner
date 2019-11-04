package kcleaner.topic.definder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Topic that is internal, i.e., used by Kafka components, e.g. stream, connect, schema-registry
 */


public class InternalTopic implements ReservedTopic {
    private final Set<String> names;

    public InternalTopic(Set<String> allTopicNames) {
        names = new HashSet<>();
        allTopicNames.forEach(
                (topic) -> {
                    if (isInternal(topic)) {
                        names.add(topic);
                    }
                }
        );
    }

    public Set<String> getNames() {
        return Collections.unmodifiableSet(names);
    }

    private boolean isInternal(String name) {
        return isUsedByConnect(name) || isUsedInternal(name) || isUsedByStream(name);
    }

    private boolean isUsedInternal(String name) {
        return name.startsWith("_");
    }

    private boolean isUsedByConnect(String name) {
        return name.startsWith("stream.connect");
    }

    private boolean isUsedByStream(String name) {
        return name.endsWith("-changelog") || name.endsWith("-repartition") || name.endsWith("-rekey");
    }
}
