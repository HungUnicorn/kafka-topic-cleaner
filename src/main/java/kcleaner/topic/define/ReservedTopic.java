package kcleaner.topic.define;

import java.util.Set;

/**
 * Define what topics should be reserved and should not be deleted
 *
 * */
public interface ReservedTopic {
    Set<String> getNames();
}
