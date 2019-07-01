package cn.edu.bupt.ZigbeeMapper.transform;

import cn.edu.bupt.ZigbeeMapper.mqtt.RpcMqttClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Tool {

    private static Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();
    public static Map<String, MqttClient> mqttMap = new ConcurrentHashMap<String,MqttClient>();
    public static Map<String, String> snidMap = new ConcurrentHashMap<String,String>();
    public static MqttClient  rpcMqttClient= null;


    public static Map<String, Channel> getChannelMap() {
        return channelMap;
    }
    public static void setChannelMap(Map<String, Channel> map) {
        Tool.channelMap = map;
    }


    public static Map<String, MqttClient> getMqttMap() {
        return mqttMap;
    }
    public static void setMqttMap(Map<String, MqttClient> map) {
        Tool.mqttMap = map;
    }

    public static Map<String, String> getSnidMap() {
        return snidMap;
    }
    public static void setSnidMap(Map<String, String> map) {
        Tool.snidMap = map;
    }

//    public static void setRpcMqttClient(MqttClient rpcMqttClient) {
//        Tool.rpcMqttClient = rpcMqttClient;
//    }
//    public static MqttClient getRpcMqttClient() { return rpcMqttClient; };



    public String bytesToHexFun3(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }

        return buf.toString();
    }

    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    public static String getIPString(ChannelHandlerContext ctx) {
        String ipString = "";
        String socketString = ctx.channel().remoteAddress().toString();
        ipString = socketString.substring(1);
        return ipString;
    }

    public static String getRemoteAddress(ChannelHandlerContext ctx) {
        String socketString = "";
        socketString = ctx.channel().remoteAddress().toString();
        return socketString;
    }

    public static byte[] getSendContent(int type, byte[] message) {
        int messageLength = message.length;
        int contentLength = message.length + 6;

        byte message_low = (byte) (messageLength & 0x00ff); // �����Ϣ��λ�ֽ�
        byte message_high = (byte) ((messageLength >> 8) & 0xff);// �����Ϣ��λ�ֽ�

        byte type_low = (byte) (type & 0x00ff); // ������͵�λ�ֽ�
        byte type_high = (byte) ((type >> 8) & 0xff);// ������͸�λ�ֽ�

        byte content_low = (byte) (contentLength & 0x00ff); // ������ݳ��ȵ�λ�ֽ�
        byte content_high = (byte) ((contentLength >> 8) & 0xff);// ������ݳ��ȸ�λ�ֽ�

        byte[] headMessage = new byte[6];
        headMessage[0] = content_low;
        headMessage[1] = content_high;
        headMessage[2] = type_low;
        headMessage[3] = type_high;
        headMessage[4] = message_low;
        headMessage[5] = message_high;

        byte[] sendContent = new byte[contentLength];
        System.arraycopy(headMessage, 0, sendContent, 0, 6);
        System.arraycopy(message, 0, sendContent, 6, messageLength);
        return sendContent;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append(' ');
        }
        return stringBuilder.toString();
    }

    public static String deviceId2Type(String deviceId){
        String type = null;
        switch (deviceId){
            case "0000":
                break;
            case "0100":
                break;
            case "0200":
                type = "switch";
                break;
            case "0300":
                break;
            case "0400":
                type = "sceneSelector";
                break;
            case "0500":
                break;
            case "0600":
                break;
            case "0700":
                break;
            case "0800":
                break;
            case "0900":
                type = "outlet";
                break;
            case "0A00":
                type = "lock";
                break;
            case "0101":
                type = "dimmableLight";
                break;
            case "0201":
                type = "colourDimmableLight";
                break;
            case "0601":
                type = "lightSensor";
                break;
            case "6101":
                type = "infrared";
                break;
            case "6301":
                type = "newInfrared";
                break;
            case "0202":
                type = "curtain";
                break;
            case "0203":
                type = "temperature";
                break;
            case "0903":
                type = "PM2.5";
                break;
            case "0204":
                type = "IASZone";
                break;
            case "1003":
                type = "Acousto-optic";
                break;
            default:
                type = "unknown";
                break;
        }
        return type;
    }
}
