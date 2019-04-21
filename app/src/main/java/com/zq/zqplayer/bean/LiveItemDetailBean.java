package com.zq.zqplayer.bean;

import java.util.List;

public class LiveItemDetailBean {
    private String enable	    ;//Integer	1
    private String game_type	;//String	ow
    private String is_followed	;//Integer	0
    private String live_id	    ;//String	17956424
    private String live_img	    ;//String	http://live-cover.msstatic.com/huyalive/1446469499-1446469499-6212539192866504704-4713164762-10057-A-0-1/20190420224310.jpg?imageview/4/0/w/310/h/175/blur/1
    private String live_name	;//String	虎牙
    private String live_nickname;//	String	路人天
    private String live_online	;//Integer	42821
    private String live_title	;//String	虎牙2358166636的直播间
    private String live_type	;//String	yy
    private String live_userimg	;//String	https://huyaimg.msstatic.com/avatar/1039/8f/68c0f37e2b695e9f81be31b67a0521_180_135.jpg

    private List<Stream> stream_list;

    public String getEnable() {
        return enable;
    }

    public String getGame_type() {
        return game_type;
    }

    public String getIs_followed() {
        return is_followed;
    }

    public String getLive_id() {
        return live_id;
    }

    public String getLive_img() {
        return live_img;
    }

    public String getLive_name() {
        return live_name;
    }

    public String getLive_nickname() {
        return live_nickname;
    }

    public String getLive_online() {
        return live_online;
    }

    public String getLive_title() {
        return live_title;
    }

    public String getLive_type() {
        return live_type;
    }

    public String getLive_userimg() {
        return live_userimg;
    }

    public List<Stream> getStream_list() {
        return stream_list;
    }

    public class Stream {
        private String type	;//String	高清
        private String url	;//String	http://pull.v.cc.163.com/pushstation/09597a3602be93611d35137964763tc2.flv?wsSecret=ce0db8bbaf9f0f2b7bdd2aa81d0ceb06&wsTime=5cbb334e

        public String getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }
    }

}
