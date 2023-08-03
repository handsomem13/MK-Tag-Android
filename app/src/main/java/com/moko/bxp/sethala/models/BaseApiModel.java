package com.moko.bxp.sethala.models;

import java.util.List;

public class BaseApiModel<T>{
    public  String Message;
    public  boolean Success;
    public List<T> Data;
}
