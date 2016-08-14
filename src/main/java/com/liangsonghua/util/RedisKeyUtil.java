package com.liangsonghua.util;

/**
 * Created by liangsonghua on 16-7-30
 */
public class RedisKeyUtil {
        private static String SPLIT = ":";
        private static String BIZ_LIKE = "like";
        private static String BIZ_DISLIKE = "dislike";
        private static String EVENT_QUEUE = "event_queue";
        // 获取粉丝
        private static String BIZ_FOLLOWER = "FOLLOWER";
        // 关注对象
        private static String BIZ_FOLLOWEE = "FOLLOWEE";

        //我关注的人或者问题的动态
        private static String BIZ_TIMELINE = "TIMELINE";

        public static String getLikeKey(int entityType, int entityID) {
          return BIZ_LIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityID) ;
        }

        public static String getDisLikeKey(int entityType, int entityID) {
                return BIZ_DISLIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityID) ;
        }

        public static String getEventQueueKey() {
                return EVENT_QUEUE+SPLIT;
        }

        // 某个实体的粉丝key
        public static String getFollowerKey(int entityType, int entityId) {
                return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
        }

        // 每个用户对某类实体的关注key
        public static String getFolloweeKey(int userId, int entityType) {
                return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
        }

        public static String getTimelineKey(int userId) {
            return BIZ_TIMELINE+SPLIT+String.valueOf(userId);
        }
}
