package com.zq.zqplayer.model.request;

public class ZQPlayerVideoUrlRequest {
    private String roomid;
    private int slaveflag;
    private String type;
    private String __plat;
    private String __version;


    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public int getSlaveflag() {
        return slaveflag;
    }

    public void setSlaveflag(int slaveflag) {
        this.slaveflag = slaveflag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String get__plat() {
        return __plat;
    }

    public void set__plat(String __plat) {
        this.__plat = __plat;
    }

    public String get__version() {
        return __version;
    }

    public void set__version(String __version) {
        this.__version = __version;
    }
}
