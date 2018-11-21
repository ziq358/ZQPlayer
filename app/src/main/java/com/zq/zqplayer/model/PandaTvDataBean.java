package com.zq.zqplayer.model;

import java.util.List;

/**
 * Created by ywl on 2016/12/10.
 */

public class PandaTvDataBean {

    private List<PandaTvListItemBean> items;
    private String total;
    private Type type;


    public List<PandaTvListItemBean> getItems() {
        return items;
    }

    public void setItems(List<PandaTvListItemBean> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public class Type
    {
        String ename;
        String cname;

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }
    }

}
