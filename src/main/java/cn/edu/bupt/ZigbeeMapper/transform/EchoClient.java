package cn.edu.bupt.ZigbeeMapper.transform;

import cn.edu.bupt.ZigbeeMapper.service.DeviceTokenRelationService;
import io.netty.bootstrap.Bootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;


public class EchoClient {

    private final String host;
    private final int port;
    private DeviceTokenRelationService deviceTokenRelationService;

    public EchoClient(String host, int port,DeviceTokenRelationService deviceTokenRelationService) {
//        super();
        this.host = host;
        this.port = port;
        this.deviceTokenRelationService = deviceTokenRelationService;
    }

    //引导函数
    @PostConstruct
    public void start()
            throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast("bytesDecoder", new ByteArrayDecoder());
                            ch.pipeline().addLast("bytesEncoder", new ByteArrayEncoder());
                            ch.pipeline().addLast(
                                    new EchoClientHandler(deviceTokenRelationService));
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }



}
