package kcleaner.find;


import kcleaner.topic.define.ReservedTopic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Find unused topics by diff of all topics and reserved topics
 */
public class UnusedTopicsFinderImpl implements UnusedTopicsFinder {
    private final Set<String> allTopics;
    private final Set<ReservedTopic> reservedTopics;

    public UnusedTopicsFinderImpl(Set<String> allTopics, Set<ReservedTopic> reservedTopics) {
        this.allTopics = allTopics;
        this.reservedTopics = reservedTopics;
    }

    public Set<String> getUnusedTopics() {
        final Set<String> copiedTopicNames = new HashSet<>(allTopics);

        final Set<String> allReservedTopics = reservedTopics.stream()
                .map(ReservedTopic::getNames)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        // Safer check so don't delete everything
        if (allReservedTopics.isEmpty()) {
            return Collections.emptySet();
        }

        copiedTopicNames.removeAll(allReservedTopics);
        return copiedTopicNames;
    }
}
