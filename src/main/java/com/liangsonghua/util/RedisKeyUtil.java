package com.liangsonghua.util;

/**
 * Created by liangsonghua on 16-7-30
 */
public class RedisKeyUtil {
        private static String SPLIT = ":";
        private static String BIZ_LIKE = "like";
        private static String BIZ_DISLIKE = "dislike";
        private static String EVENT_QUEUE = "event_queue";

        public static String getLikeKey(int entityType, int entityID) {
          return BIZ_LIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityID) ;
        }

        public static String getDisLikeKey(int entityType, int entityID) {
                return BIZ_DISLIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityID) ;
        }

        public static String getEventQueueKey() {
                return EVENT_QUEUE+SPLIT;
        }
}
