package com.xiaxiayige.okflutter


object Constant {

    const val BASEURL = "https://pub.dev"
    const val TITLE = "Ok,Flutter"

    //https://pub.dev/packages?q=okToast
    const  val SEARCH_URL = "$BASEURL/packages?q="
    val HEADER_USER_AGENT = "user-agent"
    val HEADER_USER_AGENT_VALUE =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36"
    val QUERY_SORT = "&sort="
    val TIME_OUT = 1000 * 30

    const val DEPENDENCY_COPIED_TITLE = "Copied!"
    const val DEPENDENCY_COPIED_MSG = "Dependency statements have been copied to your clipboard."

    val QUERY_SORT_TOP = "${QUERY_SORT}top"
    val QUERY_SORT_updated = "${QUERY_SORT}updated"
    val QUERY_SORT_created = "${QUERY_SORT}created"
    val QUERY_SORT_like = "${QUERY_SORT}like"
    val QUERY_SORT_points = "${QUERY_SORT}points"
    val QUERY_SORT_popularity = "${QUERY_SORT}popularity"
}