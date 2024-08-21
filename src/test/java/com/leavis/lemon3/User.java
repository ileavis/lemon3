package com.leavis.lemon3;

/**
 * @Author: paynejlli
 * @Description: 一个包装类
 * @Date: 2024/8/21 16:51
 */
public class User {

    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
