package com.xiaxiayige.plugin.respostory

import com.xiaxiayige.plugin.bean.PackagesItem
import com.xiaxiayige.plugin.bean.Resonse
import com.xiaxiayige.plugin.utils.HtmlParseUtil
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeoutException

//Flutter 依赖搜索
object SearchFlutterRespostory {
    /**
     * 搜索关键字
     */
    fun search(keyWorld: String, sort: String? = ""): Observable<MutableList<PackagesItem>> {
        val create = Observable.create<Resonse> { emitter ->
            val requestUrl = HtmlParseUtil.getRequestUrl(keyWorld, sort ?: "")
            println("requestUrl = $requestUrl")
            val bodyElement = HtmlParseUtil.getBodyElement(requestUrl)
            val packagesItem = HtmlParseUtil.getPackagesItem(bodyElement)
            val resonse = Resonse(0, "success", packagesItem.map { element ->
                HtmlParseUtil.getDataForSingleItem(element)
            }.toMutableList())
            emitter.onNext(resonse)
        }
        return create.flatMap<MutableList<PackagesItem>> {
            val data = it.data
            if(data !=null && data.isNotEmpty()){
                Observable.just(data)
            }else{
                Observable.error(Exception(" not found "))
            }
        }

    }

}

fun main() {
    val search = SearchFlutterRespostory.search("oktoast")
    search.subscribe({
        println("$it")
    },{
        println("$it")
    })

}