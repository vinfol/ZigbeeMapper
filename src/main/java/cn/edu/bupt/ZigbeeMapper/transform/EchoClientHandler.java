package cn.edu.bupt.ZigbeeMapper.transform;

import cn.edu.bupt.ZigbeeMapper.method.GatewayMethodImpl;
import cn.edu.bupt.ZigbeeMapper.service.DataService;
import cn.edu.bupt.ZigbeeMapper.service.DeviceTokenRelationService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


import static cn.edu.bupt.ZigbeeMapper.transform.Tool.getRemoteAddress;


@ChannelHandler.Sharable
public class EchoClientHandler
        extends SimpleChannelInboundHandler<byte[]> {

    public static byte response = 0x00 ;
    byte[] b = new byte[] { 4, 0, 31, 0 };
    byte[] bt = new byte[1024];
    public static GatewayMethodImpl gatewayMethod = new GatewayMethodImpl();
    private DataService dataService = new DataService();
    private DeviceTokenRelationService deviceTokenRelationService;

    public EchoClientHandler(DeviceTokenRelationService deviceTokenRelationService) {
        this.deviceTokenRelationService = deviceTokenRelationService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Tool.getChannelMap().put(Tool.getIPString(ctx), ctx.channel());
//        if(!Tool.getMqttMap().containsKey("node1")) {
//            RpcMqttClient rpcMqttClient = new RpcMqttClient();
//            rpcMqttClient.init();
//        }
//        gatewayMethod.getGatewayInfo(Tool.getIPString(ctx));
        gatewayMethod.getAllDevice(Tool.getIPString(ctx));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        String ip = getRemoteAddress(ctx).substring(1);
        dataService.resolution(msg,ip,deviceTokenRelationService);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // ɾ��Channel Map�е�ʧЧClient
        String ip =getRemoteAddress(ctx);
        System.out.println("设备下线:"+ip.substring(1));
        Tool.getMqttMap().get("node1").disconnect();
        Tool.getMqttMap().get("node1").close();
        Tool.getMqttMap().remove("node1");
        ctx.close().sync();
    }

}
