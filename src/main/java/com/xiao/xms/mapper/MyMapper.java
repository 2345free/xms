package com.xiao.xms.mapper;


import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 继承Mapper插件的单表CRUD
 *
 * @author luoxiaoxiao
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
