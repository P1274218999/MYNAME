package com.dhht.sld.base.http.retorfit;


public class Result<T> {
    public int code;
    public String msg;
    public T data;
    public int total;  //条目总数
}
