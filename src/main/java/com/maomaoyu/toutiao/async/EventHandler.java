package com.maomaoyu.toutiao.async;

import java.util.List;

/**
 * maomaoyu    2018/12/9_20:40
 **/
public interface EventHandler {
    void doHandle(EventModel model);

    List<EventType> getSupportEventTypes();
}
