package com.entity;

import com.chunk.ExternalSourceData;

public class TmUser implements ExternalSourceData {

    private String loginName;

    private int age;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TmUser{");
        sb.append("loginName='").append(loginName).append('\'');
        sb.append(", age=").append(age);
        sb.append('}');
        return sb.toString();
    }
}
