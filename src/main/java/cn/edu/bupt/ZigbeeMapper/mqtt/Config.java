package cn.edu.bupt.ZigbeeMapper.mqtt;

public class Config {
    public static String HOST = "tcp://10.112.176.221:1883";
    //public static String RPC_DEVICE_ACCESSTOKEN = "ViWeZop4Jp4tvvEtwu6Z";


    public static String DeviceETPrefix            = "$hw/events/device/";
    public static String DeviceETStateUpdateSuffix = "/state/update";
    public static String TwinETUpdateSuffix        = "/twin/update";
    public static String TwinETCloudSyncSuffix     = "/twin/cloud_updated";
    public static String TwinETGetResultSuffix     = "/twin/get/result";
    public static String TwinETGetSuffix           = "/twin/get";

//    $hw/events/device/+/twin/#


    public static String RPC_TOPIC = "v1/devices/me/rpc/request/+";
    public static String datatopic = "v1/devices/me/telemetry";
    public static String attributetopic = "v1/devices/me/attributes";
    public static String RPC_RESPONSE_TOPIC = "v1/devices/me/rpc/response/";


}
