package cn.edu.bupt.ZigbeeMapper.data;

import lombok.Data;

@Data
public class DeviceTokenRelation {
    private Integer id;
    private String IEEE;
    private Integer endPoint;
    private String type;
    private String shortAddress;
    private String gatewayIP;

    public DeviceTokenRelation(String IEEE, Integer endPoint, String type, String shortAddress, String gatewayIP){
        this.IEEE=IEEE;
        this.endPoint = endPoint;
        this.type = type;
        this.shortAddress = shortAddress;
        this.gatewayIP = gatewayIP;
    }

    public DeviceTokenRelation(Integer id, String IEEE, Integer endPoint, String type, String shortAddress, String gatewayIP){
        this.id = id;
        this.IEEE=IEEE;
        this.endPoint = endPoint;
        this.type = type;
        this.shortAddress = shortAddress;
        this.gatewayIP = gatewayIP;
    }

}
