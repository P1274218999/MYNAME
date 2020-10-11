package com.dhht.sld.main.choose.article.Bean;

import com.dhht.sld.base.BaseHttpResBean;

import java.util.List;

public class ArticleDataBean extends BaseHttpResBean {

    public List<ListArticle> data;

    public static class ListArticle {

        public int id;

        public String name;

        public String icon;

        public String icon_current;

    }

}
