package cn.edu.bupt.ZigbeeMapper.mqtt;

import cn.edu.bupt.ZigbeeMapper.transform.EchoClientHandler;
import cn.edu.bupt.ZigbeeMapper.transform.Tool;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.Charset;

/**
 * Created by zy on 2018/5/16.
 */

public class RpcMqttClient {


    private MqttClient rpcMqtt;

    public boolean init() {
        System.out.println("mqtt-init");
            try{
                if(rpcMqtt != null){
                    rpcMqtt.close();
                }
                rpcMqtt = null;
                rpcMqtt = new MqttClient(Config.HOST,"eventBus",new MemoryPersistence());
                Tool.getMqttMap().put("node1",rpcMqtt);
                MqttConnectOptions optionforRpcMqtt = new MqttConnectOptions();
                optionforRpcMqtt.setCleanSession(true);
                optionforRpcMqtt.setConnectionTimeout(5);
                optionforRpcMqtt.setKeepAliveInterval(20);
                rpcMqtt.setCallback(new RpcMessageCallBack(rpcMqtt));
                rpcMqtt.connect(optionforRpcMqtt);
                rpcMqtt.subscribe("$hw/events/device/+/twin/#",1);
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
        return true;
    }

    public void publicResponce(String topic,String data) throws Exception{
        MqttMessage msg = new MqttMessage();
        msg.setPayload(data.getBytes(Charset.forName("utf-8")));
        if(rpcMqtt.isConnected()){
            rpcMqtt.publish(topic, msg);
        }
    }

    public MqttClient getMqttClient(){
        return rpcMqtt;
    }
}
