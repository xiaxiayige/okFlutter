package com.xiaxiayige.plugin.utils

import com.xiaxiayige.plugin.Constant
import com.xiaxiayige.plugin.bean.PackagesItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException

object HtmlParseUtil {

    /***
     * <div class="packages-item">
    <div class="packages-header">
    <h3 class="packages-title">
    <a href="/packages/oktoast">oktoast</a>
    </h3>

    <a class="packages-scores" href="/packages/oktoast/score">
    <div class="packages-score packages-score-like">

    <div class="packages-score-value -has-value">
    <span class="packages-score-value-number">85</span>
    <span class="packages-score-value-sign"></span>
    </div>

    <div class="packages-score-label">likes</div>

    </div>
    <div class="packages-score packages-score-health">

    <div class="packages-score-value -has-value">
    <span class="packages-score-value-number">100</span><span class="packages-score-value-sign"></span>
    </div>

    <div class="packages-score-label">pub points</div>

    </div>
    <div class="packages-score packages-score-popularity">

    <div class="packages-score-value -has-value">
    <span class="packages-score-value-number">96</span><span class="packages-score-value-sign">%</span>
    </div>
    <div class="packages-score-label">popularity</div>

    </div>
    </a>

    </div>
    <p class="packages-description">A pure flutter toast library  Support custom style/widget. Easy to use. You can use this library to achieve the same effect as Android toast.</p>
    <p class="packages-metadata">
    <span class="packages-metadata-block">
    //跳转链接和版本号
    v <a href="/packages/oktoast">2.3.2</a>
    //更新时间
    • Published: <span>Apr 14, 2020</span>
    </span>

    ......省略
     */
    fun getDataForSingleItem(element: Element): PackagesItem {
        //搜索到的名称 packages-title
        val nameElement = element.getElementsByClass("packages-title")
        val name = nameElement.text()
        println("name = $name")
        //获取like 喜欢次数
        val like = element.getElementsByClass("packages-score packages-score-like").first().children().first().text()
        println("like = $like")
        //获取pub points(官方给的评分)
        val pubPoints =
                element.getElementsByClass("packages-score packages-score-health").first().children().first().text()
        //获取popularity 人气值
        val popularity =
                element.getElementsByClass("packages-score packages-score-popularity").first().children().first().text()
        val packagesMetadataBlock = element.getElementsByClass("packages-metadata-block")
        val childs = packagesMetadataBlock.first().children()
        //版本号
        val versionName = childs.first().text()
        println("版本号:^" + versionName)
        //更新时间
        val updateTime = childs[1].text()
        println("更新时间:" + updateTime)
        //跳转链接 详情页
        val targetDetailUrl = Constant.BASEURL + childs[0].attr("href")
        val dependencies = "$name: ^$versionName"
//        println("dependencies:$dependencies")
        return PackagesItem(
                name,
                targetDetailUrl,
                like,
                pubPoints,
                popularity,
                versionName,
                updateTime,
                dependencies
        )

    }


    fun getRequestUrl(queryString: String, sort: String) = Constant.SEARCH_URL + queryString + sort


    fun getBodyElement(requestUrl: String) = Jsoup.connect(requestUrl)
            .header(Constant.HEADER_USER_AGENT, Constant.HEADER_USER_AGENT_VALUE)
            .timeout(Constant.TIME_OUT)
            .get().body()

    fun getPackagesItem(bodyElement: Element) = bodyElement.getElementsByClass("packages-item")
}