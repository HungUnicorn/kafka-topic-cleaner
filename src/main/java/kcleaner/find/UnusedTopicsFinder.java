package kcleaner.find;

import java.util.Set;

/**
 * Find unused topics
 *
* */

public interface UnusedTopicsFinder {
    Set<String> getUnusedTopics();
}
