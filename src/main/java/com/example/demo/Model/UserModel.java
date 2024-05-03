package com.example.demo.Model;


import com.example.demo.Mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class UserModel {
   @Autowired
    UserMapper userMapper;

    @Transactional
    public Boolean setUserState(String cardNo,Integer state){

      return   userMapper.setUserState(cardNo,state);


    }






}
