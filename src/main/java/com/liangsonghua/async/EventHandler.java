package com.liangsonghua.async;

import com.liangsonghua.async.EventModel;
import com.liangsonghua.async.EventType;

import java.util.List;

/**
 * Created by liangsonghua on 16-8-4.
 */
public interface EventHandler {
        void doHandle(EventModel eventModel);
        List<EventType> getSupportEventType();
}
