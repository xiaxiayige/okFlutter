package com.xiaxiayige.okflutter.respostory

import com.xiaxiayige.okflutter.bean.PackagesItem
import com.xiaxiayige.okflutter.bean.Resonse
import com.xiaxiayige.okflutter.utils.HtmlParseUtil
import io.reactivex.*

//Flutter 依赖搜索
object SearchFlutterRespostory {
    private val  isDebug = false
    /**
     * 搜索关键字
     */
    fun search(keyWorld: String, sort: String? = ""): Observable<MutableList<PackagesItem>> {
        val create = Observable.create<Resonse> { emitter ->
            if(isDebug){
                val elements = PackagesItem(
                    "oktoast","https://www.baidu.com","18","20","100%","2.2.0",
                        "2020.2.10","oktoast: ^2.2.0"
                )
                emitter.onNext(Resonse(0,"success", mutableListOf(elements,elements,elements,elements,elements,elements,elements,elements)))
//                emitter.onNext(Resonse(0,"success", mutableListOf()))
            }else{
                val requestUrl = HtmlParseUtil.getRequestUrl(keyWorld, sort ?: "")
                println("requestUrl = $requestUrl")
                val bodyElement = HtmlParseUtil.getBodyElement(requestUrl)
                val packagesItem = HtmlParseUtil.getPackagesItem(bodyElement)
                val resonse = Resonse(0, "success", packagesItem.map { element ->
                    HtmlParseUtil.getDataForSingleItem(element)
                }.toMutableList())
                emitter.onNext(resonse)
            }
        }
        return create.flatMap<MutableList<PackagesItem>> {
            val data = it.data
            if(data !=null && data.isNotEmpty()){
                Observable.just(data)
            }else{
                Observable.error(Exception(" Not found "))
            }
        }

    }

}

fun main() {
    val starttime = System.currentTimeMillis()
     SearchFlutterRespostory.search("dio").subscribe({
        println("$it")
    },{
         it.printStackTrace()
         print(System.currentTimeMillis()-starttime)
        println("$it")
    })

}