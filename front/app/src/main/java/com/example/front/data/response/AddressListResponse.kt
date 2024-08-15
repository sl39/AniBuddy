package com.example.front.data.response

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "NewAddressListResponse")
data class NewAddressListResponse(
    @Element(name = "cmmMsgHeader")
    val cmmMsgHeader: CmmMsgHeader,

    @Element(name = "newAddressListAreaCdSearchAll")
    val newAddressListAreaCdSearchAll: List<NewAddressListAreaCdSearchAll>
)

@Xml(name = "cmmMsgHeader")
data class CmmMsgHeader(
    @PropertyElement(name = "requestMsgId")
    val requestMsgId: String?,

    @PropertyElement(name = "responseMsgId")
    val responseMsgId: String?,

    @PropertyElement(name = "responseTime")
    val responseTime: String,

    @PropertyElement(name = "successYN")
    val successYN: String,

    @PropertyElement(name = "returnCode")
    val returnCode: String,

    @PropertyElement(name = "errMsg")
    val errMsg: String?,

    @PropertyElement(name = "totalCount")
    val totalCount: Int,

    @PropertyElement(name = "countPerPage")
    val countPerPage: Int,

    @PropertyElement(name = "totalPage")
    val totalPage: Int,

    @PropertyElement(name = "currentPage")
    val currentPage: Int
)

@Xml(name = "newAddressListAreaCdSearchAll")
data class NewAddressListAreaCdSearchAll(
    @PropertyElement(name = "zipNo")
    val zipNo: String,

    @PropertyElement(name = "lnmAdres")
    val lnmAdres: String,

    @PropertyElement(name = "rnAdres")
    val rnAdres: String
)
