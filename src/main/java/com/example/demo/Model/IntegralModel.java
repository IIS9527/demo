package com.example.demo.Model;


import com.example.demo.Data.ExchangeIntegral;
import com.example.demo.Data.User;
import com.example.demo.Mapper.IntegralMapper;
import com.example.demo.Mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Component
public class IntegralModel {


    @Autowired
    IntegralMapper integralMapper;

    @Autowired
    UserMapper userMapper;
    /*
    *
    * 用户积分申请
    *
    * */
    @Transactional
    public Boolean applyExchange(User user, Long integral){

        //创建一条申请积分数据 申请账号信息,申请积分数, 申请时间，
        ExchangeIntegral exchangeIntegral =new ExchangeIntegral();
        exchangeIntegral.setCardNo(user.getCardNo());
        exchangeIntegral.setQrUrl(user.getQrUrl());
        exchangeIntegral.setQrUrlZFB(user.getQrUrlZFB());
        exchangeIntegral.setQrUrlOY(user.getQrUrlOY());
        exchangeIntegral.setIntegral(integral);
        exchangeIntegral.setRealName(user.getRealName());

       integralMapper.addExchangeIntegral(exchangeIntegral);

        //数据库减操作  减去用户可用积分 操作失败就回滚
        userMapper.exchangeIntegral(user.getCardNo(),integral);

       return  true;

    }

    @Transactional
    public boolean agreeExchangeIntegral( BigInteger id) {

       return integralMapper.agreeExchangeIntegral(id);

    }

    @Transactional
    public boolean disAgreeExchangeIntegral( BigInteger id) {

      //取出积分
      ExchangeIntegral exchangeIntegral = integralMapper.selectExchangeIntegralById(id);

      //加入用户可用积分
      userMapper.addAvailableIntegral(exchangeIntegral.getCardNo(),exchangeIntegral.getIntegral());

      return integralMapper.disAgreeExchangeIntegral(id);

    }


    @Transactional
    public boolean changeTempIntegral(String cardNo,Long tempIntegral) {

        if (userMapper.addChangeTempIntegral(cardNo,tempIntegral)){

            return  userMapper.changeTempIntegral(cardNo,tempIntegral);

        }

        return false;
    }

    @Transactional
    public boolean changeIntegral(String cardNo,Long tempIntegral) {

        if (userMapper.addChangeTempIntegral(cardNo,tempIntegral)){

            return  userMapper.changeIntegral(cardNo,tempIntegral);

        }

        return false;


    }


    @Transactional
    public boolean changePassword(String cardNo, String password) {
        return userMapper.changePassword(cardNo,password);

    }
}
