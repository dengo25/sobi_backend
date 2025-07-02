package com.kosta.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class HtmlSanitizer {

    /* 기본적인 HTML 허용 */
    public static String sanitize(String html){
        return Jsoup.clean(html, Safelist.basic());
    }

    /* 모든 html 제거, 텍스트만 */
    public static String strictSanitize(String html){
        return Jsoup.clean(html, Safelist.none());
    }

    /* 이미지, 링크까지 허용 */
    public static String extendedSanitize(String html){
        return Jsoup.clean(html, Safelist.relaxed());
    }
}
