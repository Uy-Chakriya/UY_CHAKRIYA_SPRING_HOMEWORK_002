package com.example.demo4.Model;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@NoArgsConstructor
public class Response<T> {
    private String message;
    private T payload;
    private Object status;
    private LocalDateTime time;

    public Response(String message, T payload, Object status, LocalDateTime time) {
        this.message = message;
        this.payload = payload;
        this.status = status;
        this.time = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
