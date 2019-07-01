package cn.edu.bupt.ZigbeeMapper.mapper;

import cn.edu.bupt.ZigbeeMapper.data.DeviceTokenRelation;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DeviceTokenRelationMapper {

    @Insert("INSERT INTO deviceTokenRelation(IEEE, endPoint,type,shortAddress, gatewayIP) VALUES (#{IEEE}, #{endPoint}, #{type}, #{shortAddress}, #{gatewayIP})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer addARelation(DeviceTokenRelation deviceTokenRelation);

    @Select("SELECT * FROM deviceTokenRelation WHERE IEEE = #{IEEE} AND endPoint = #{endPoint}")
    DeviceTokenRelation getRelotionByIEEEAndEndPoint(@Param("IEEE") String IEEE, @Param("endPoint") Integer endPoint);

//    @Select("SELECT * FROM deviceTokenRelation WHERE gatewayIP = #{gatewayIP} AND endPoint = #{endPoint}")
//    DeviceTokenRelation getRelotionByGatewayNameAndEndPoint(@Param("gatewayIP") String gatewayIP, @Param("endPoint") Integer endPoint);
//
//    @Select("SELECT * FROM deviceTokenRelation WHERE shortAddress = #{shortAddress} AND endPoint = #{endPoint}")
//    DeviceTokenRelation getRelotionBySAAndEndPoint(@Param("shortAddress") String shortAddress, @Param("endPoint") Integer endPoint);
//
//    @Select("SELECT * FROM deviceTokenRelation WHERE gatewayIP = #{gatewayIP} AND type = 'Gateway'")
//    DeviceTokenRelation getGateway(@Param("gatewayIP") String gatewayIP);
//
//    @Select("SELECT * FROM deviceTokenRelation WHERE IEEE = #{IEEE}")
//    List<DeviceTokenRelation> getRelationByIEEE(@Param("IEEE") String IEEE);
//
//    @Select("SELECT auto_increment FROM information_schema.tables where table_schema='BUPT_IOT' and table_name='deviceTokenRelation'")
//    Integer getdeviceNumber();
//
//    @Update("UPDATE deviceTokenRelation SET shortAddress = #{shortAddress} WHERE IEEE = #{IEEE}")
//    Integer updateShortAddress(@Param("shortAddress") String shortAddress, @Param("IEEE") String IEEE);
//
//    @Update("UPDATE deviceTokenRelation SET gatewayIP = #{gatewayIP} WHERE IEEE = #{IEEE}")
//    Integer updateGatewayName(@Param("gatewayIP") String gatewayIP, @Param("IEEE") String IEEE);
//
//    @Delete("DELETE FROM deviceTokenRelation WHERE IEEE = #{IEEE}")
//    Integer deleteDeviceByIEEE(@Param("IEEE") String IEEE);
}
