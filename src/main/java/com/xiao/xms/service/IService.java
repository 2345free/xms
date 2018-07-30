/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.xiao.xms.service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通用接口
 *
 * @author luoxiaoxiao
 */
@Service
public interface IService<T> {

    /**
     * 通过主键获取一条数据
     *
     * @param key
     * @return
     */
    T selectByKey(Object key);

    /**
     * 通过参数实体对象中的非空属性作为条件查询一条数据
     *
     * @param entity
     * @return
     */
    T selectOne(T entity);

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    int insertSelective(T entity);

    /**
     * 删除
     *
     * @param key
     * @return
     */
    int deleteByPrimaryKey(Object key);

    /**
     * 更新数据,参数对象属性为空则使用null覆盖数据库字段原来的值
     *
     * @param entity
     * @return
     */
    int updateByPrimaryKey(T entity);

    /**
     * 更新参数实体对象中的非空属性
     *
     * @param entity
     * @return
     */
    int updateByPrimaryKeySelective(T entity);

    /**
     * 使用Example查询条件
     *
     * @param example
     * @return
     */
    List<T> selectByExample(Object example);

    //...
}
