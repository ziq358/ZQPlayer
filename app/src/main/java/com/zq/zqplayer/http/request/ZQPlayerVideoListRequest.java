package com.zq.zqplayer.http.request;

public class ZQPlayerVideoListRequest {
    private String offset;//
    private String limit;//
    private String live_type;//平台类型
    private String game_type;//

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getLive_type() {
        return live_type;
    }

    public void setLive_type(String live_type) {
        this.live_type = live_type;
    }

    public String getGame_type() {
        return game_type;
    }

    public void setGame_type(String game_type) {
        this.game_type = game_type;
    }
}
