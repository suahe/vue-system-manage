package com.example.manageSystem.admin.enums;

public enum  LogType {

    ACCESS_TYPE((short)1,"接入日志"),
    EXCEPTION_TYPE((short)2,"错误日志");


    private Short value;
    private String name;

    private LogType(Short value, String name) {
        this.value = value;
        this.name = name;
    }

    public Short getValue() {
        return value;
    }
    public String getName() {
        return name;
    }
    public static String getNameByValue(Short value) {
        for (LogType ownerType: LogType.values()){
            if (ownerType.value==value){
                return ownerType.getName();
            }
        }
        return "";
    }

    public boolean isType(Short value) {
        if (this.value==value){
            return true;
        }
        return false;
    }

}
