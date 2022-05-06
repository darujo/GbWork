package com.darujo.event;

public class Event {
    private final Object data;
    private final EventType eventType;

    public Event(EventType eventType, Object data) {
        this.data = data;
        this.eventType = eventType;
    }

    public Object getData() {
        return data;
    }

    public EventType getEventType() {
        return eventType;
    }
}
