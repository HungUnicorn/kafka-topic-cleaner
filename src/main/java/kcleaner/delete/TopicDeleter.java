package kcleaner.delete;

import java.util.Set;

/**
 * Delete topics handler
 * */
public interface TopicDeleter {
    void delete(Set<String> topicNames);
}
