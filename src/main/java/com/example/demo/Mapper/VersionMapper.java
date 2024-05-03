package com.example.demo.Mapper;


import com.example.demo.Data.EC;
import org.apache.ibatis.annotations.*;

@Mapper
public interface VersionMapper {


    @Insert({"insert into ECVersion(download_url,version,dialog,msg,`force`) values(#{download_url},#{version},#{dialog},#{msg},#{force})"})
    public Boolean setVersion(EC ec) ;

    @Select({"select * from ECVersion  order by id desc limit 1; "})
    public EC getNewVersion();
//    @Delete({"delete ECVersion where  "})
//    public boolean deleteAllVersion();
}
