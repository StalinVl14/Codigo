package com.example.codigoqr.clases;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataQr {

    /**
     * {
     * "format": "JPEG",
     * "img": "Z",
     * "msg": "success",
     * "name": "toaquiza_casa_oscar_manuel_0504121344",
     * "size": [
     * 960,
     * 1280
     * ],
     * "time": "00:00:01"
     * }
     */
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("format")
    @Expose
    private String format;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
