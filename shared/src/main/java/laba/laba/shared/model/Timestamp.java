package laba.laba.shared.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Timestamp {

    @JsonProperty("second")
    private long second;

    @JsonProperty("microSecond")
    private int microSecond;

    public Timestamp() {
        //
    }

    public Timestamp(long second, int microSecond) {
        this.second = second;
        this.microSecond = microSecond;
    }

    public long getSecond() {
        return second;
    }

    public int getMicroSecond() {
        return microSecond;
    }

    @Override
    public String toString() {
        return "Timestamp{" +
                "second=" + second +
                ", microSecond=" + microSecond +
                '}';
    }

}
