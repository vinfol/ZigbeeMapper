package cn.edu.bupt.ZigbeeMapper.mqtt;


import cn.edu.bupt.ZigbeeMapper.data.Device;
import cn.edu.bupt.ZigbeeMapper.method.GatewayMethod;
import cn.edu.bupt.ZigbeeMapper.method.GatewayMethodImpl;
import cn.edu.bupt.ZigbeeMapper.transform.Tool;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class RpcMessageCallBack implements MqttCallback {


	private RpcMqttClient rpcMqttClient;
	private GatewayMethod gatewayMethod = new GatewayMethodImpl();

	public RpcMessageCallBack(MqttClient rpcMqtt){

		this.rpcMqttClient = new RpcMqttClient();

	}

	@Override
	public void connectionLost(Throwable arg0) {
		System.out.println("进入mqtt断线回调");
		//SecondActivity.wrapper.init();
		while (!rpcMqttClient.init()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void messageArrived(String topic, MqttMessage msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("msg:" + msg);
		String id = topic.substring(topic.indexOf("device/") + 7, topic.lastIndexOf("/twin"));
		JsonObject message = new JsonParser().parse(new String(msg.getPayload())).getAsJsonObject();
		String operate = message.getAsJsonObject("twin").getAsJsonObject("power-status").getAsJsonObject("current").getAsJsonObject("expected").get("value").getAsString();

		byte state;
		if(operate.equals("ON")) {
			state = 0x01;
		}else {
			state = 0x00;
		}
		Device controlDevice = new Device();
		String IP = "";
		if(id.equals("led-light-instance-01")) {

			byte Endpoint = 0x10;
			controlDevice.setEndpoint(Endpoint);
			controlDevice.setShortAddress("CE4E");
			controlDevice.setSnid("");

			gatewayMethod.setDeviceState(controlDevice,state,IP);
		}

// [22, 0, -126, 101, 46, 17, -2, -126, 13, 2, -50, 78, 0, 0, 0, 0, 0, 0, 16, 0, 0, 1]


//		JsonObject jsonObject = new JsonParser().parse(new String(msg.getPayload())).getAsJsonObject();
//		Integer position = topic.lastIndexOf("/");
//		Integer requestId = Integer.parseInt(topic.substring(position+1));
//		System.out.println(requestId);
//
//
//		switch (jsonObject.get("serviceName").getAsString()){
//			case "control switch":
//				switch (jsonObject.get("methodName").getAsString()){
//					case "setstate":
//						try {
//							Device controlDevice = new Device();
//							controlDevice.setShortAddress(jsonObject.get("shortAddress").getAsString());
//							controlDevice.setEndpoint(jsonObject.get("Endpoint").getAsByte());
//
//							byte state;
//							if (jsonObject.get("status").getAsString().equals("true")) {
//								state = 0x01;
//							} else {
//								state = 0x00;
//							}
							//System.out.println("进入控制");

//							String ip = gatewayGroupService.getGatewayIp(controlDevice.getShortAddress(), Integer.parseInt(String.valueOf(controlDevice.getEndpoint())));
//							if (ip == null) {
//								System.out.println("Gateway offline");
//								//rpcMqttClient.publicResponce(Config.RPC_RESPONSE_TOPIC+requestId,"error");
//							}

//							System.out.println(ip);
//							gatewayMethod.setDeviceState(controlDevice, state, ip);
							//rpcMqttClient.publicResponce(Config.RPC_RESPONSE_TOPIC+requestId,"success");
//						}catch (Exception e){
//							System.out.println(e);
//						}
//
//						break;
//				}
//				break;
//		}
	}
}
