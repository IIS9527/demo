package com.example.demo.Mapper;


import com.example.demo.Data.ExchangeIntegral;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface IntegralMapper {





    @Insert({"insert into exchangeIntegral (exchangeTime, integral, cardNo, realName, qrUrl,qrUrlZFB,qrUrlOY)" +
            "values (now(), #{ei.integral}, #{ei.cardNo}, #{ei.realName}, #{ei.qrUrl},#{ei.qrUrlZFB},#{ei.qrUrlOY})"})
    Boolean addExchangeIntegral(@Param("ei") ExchangeIntegral exchangeIntegral);


    List<ExchangeIntegral> selectExchangeIntegralList(@Param("ei") ExchangeIntegral exchangeIntegral);

    Integer selectExchangeIntegralListTotal(@Param("ei") ExchangeIntegral exchangeIntegral);

    @Update({"update exchangeIntegral set allowState = 1 , allowTime = now() where id = #{id} "})
    boolean agreeExchangeIntegral(@Param("id") BigInteger id);


    @Update({"update exchangeIntegral set allowState = 3 , where id = #{id} "})
    boolean disAgreeExchangeIntegral(@Param("id") BigInteger id);


    HashMap<String, BigDecimal> selectTodayExchangeAndAlreadExchangeIntegral();

    @Select({"select * from  exchangeIntegral where id=#{id}"})
    ExchangeIntegral selectExchangeIntegralById(@Param("id")BigInteger id);




    @Delete({"delete from exchangeIntegral  where STR_TO_DATE(allowTime,'%Y-%m-%d %H:%i:%s') < #{date}"})
    boolean deleteExchangeIntegralbyAllowTime(@Param("date")String date);
}
