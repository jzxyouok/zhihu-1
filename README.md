- 体验：[http://139.129.40.210:8080/](http://139.129.40.210:8080/)
- 用户：admin/admin liangsonghua/liangsonghua

## 注册登陆： ##

![](http://i.imgur.com/A1aQZcA.png)

**拦截器**：

	@Component
	public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }

            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
其中/Model/HostHolder负责管理user信息

**访问跳转**

 	 @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (hostHolder.getUser() == null) {
            httpServletResponse.sendRedirect("/reglogin?next=" + httpServletRequest.getRequestURI());
        }
        return true;
    }
## 评论中心部内信 ##

**评论表：**
统一的评论服务，覆盖所有的实体评论

     DROP TABLE IF EXISTS `comment`;
      CREATE TABLE `comment` (
      `id` INT NOT NULL AUTO_INCREMENT,
      `content` TEXT NOT NULL,
      `user_id` INT NOT NULL,
      `entity_id` INT NOT NULL,
      `entity_type` INT NOT NULL,
      `created_date` DATETIME NOT NULL,
      `status` INT NOT NULL DEFAULT 0,
      PRIMARY KEY (`id`),
      INDEX `entity_index` (`entity_id` ASC, `entity_type` ASC)
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
- entity_id可以是questionId/commentId
- entity_type可以是question/comment


**redis**

- List(双向列表，适用于最新列表关注列表):lpush/lpop/blpop/lindex/lrange/lrem/linsert/lset/rpush
- set(适用于无顺序的集合，点赞点踩抽奖已读共同好友) sdiff/smembers/sinter/scard
- SortedSet(排行榜优先队列) zadd/zscore/zrange/zcount/zrank/zrevrank
- Hash(对象属性不定长属性数)hset/hget/hgetAll/hexists/hkeys/hvals
- KV(单一数据验证码PV缓存)set/setex/incr

**关注服务**

- 特点：多对多服务 ID与ID的关联，有序
- 存储结构：redis:zset/list
- Service:通用关注接口
- Controller:首页问题关注数，详情问题关注列表，粉丝关注人列表，关注异步事件

**队列实现异步**
![](http://i.imgur.com/UG7giTq.png)

/async/EventProducer.java


  	public boolean fireEvent(EventModel eventModel) {
            try {
                    String json = JSONObject.toJSONString(eventModel);
                    String key = RedisKeyUtil.getEventQueueKey();
                    jedisAdapter.lpush(key, json);
                    return true;
            } catch (Exception e) {
                    return false;
            }
    }

/async/EventConsumer.java

	Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
                if(beans!=null) {
                        for(Map.Entry<String,EventHandler> entry:beans.entrySet()) {
                                List<EventType> eventTypes = entry.getValue().getSupportEventType();
                                for(EventType type:eventTypes) {
                                        if(!config.containsKey(type)) {
                                                config.put(type,new ArrayList<EventHandler>());
                                        }
                                        config.get(type).add(entry.getValue());
                                }
                        }
                }
                Thread thread  = new Thread(new Runnable() {
                        @Override
                        public void run() {
                                while(true) {
                                        String key = RedisKeyUtil.getEventQueueKey();
                                        List<String> events = jedisAdapter.brpop(0, key);
                                        for(String message:events) {
                                                if(message.equals(key)) {
                                                        continue;
                                                }
                                                EventModel eventModel = JSON.parseObject(message, EventModel.class);
                                                if(!config.containsKey(eventModel.getType())) {
                                                        logger.error("内部错误");
                                                        continue;
                                                }
                                                for(EventHandler handler:config.get(eventModel.getType())) {
                                                        handler.doHandle(eventModel);
                                                }
                                        }

                                }
                        }
                });thread.start();
        }

在具体的事件中只要 implements EventHandler实现doHandle方法发送站内信即可

