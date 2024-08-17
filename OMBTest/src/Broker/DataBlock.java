package Broker;

import java.time.LocalDateTime;

public class DataBlock {

    private String data;
    private LocalDateTime dateTime;

    public DataBlock(String data) {
        this.data = data;
        this.dateTime = LocalDateTime.now();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "DataBlock{" +
                "data='" + data + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
