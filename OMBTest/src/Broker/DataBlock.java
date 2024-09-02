package Broker;

import Network.Useful.Constants;

import java.time.LocalDateTime;

public class DataBlock {

    private String partitionName;
    private String message;
    private byte dataType;
    private LocalDateTime createdDateTime;
    private final LocalDateTime addedDateTime;

    public DataBlock(String message, String partitionName) {
        this(message, partitionName, Constants.INFO_DATA_TYPE);
    }

    public DataBlock(String message, String partitionName, byte dataType) {
        this.message = message;
        this.partitionName = partitionName;
        this.dataType = dataType;
        this.createdDateTime = LocalDateTime.now();
        this.addedDateTime = LocalDateTime.now();
    }

    public String getPartitionName() {
        return partitionName;
    }

    public void setPartitionName(String partitionName) {
        this.partitionName = partitionName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte getDataType() {
        return dataType;
    }

    public void setDataType(byte dataType) {
        this.dataType = dataType;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }
    public LocalDateTime getAddedDateTime() {
        return addedDateTime;
    }
    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }


    @Override
    public String toString() {
        return "DataBlock{" +
                "partitionName='" + partitionName + '\'' +
                ", data='" + message + '\'' +
                ", dataType='" + dataType + '\'' +
                ", createdDateTime=" + createdDateTime +
                '}';
    }
}
