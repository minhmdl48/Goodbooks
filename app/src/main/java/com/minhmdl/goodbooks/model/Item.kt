package com.minhmdl.goodbooks.model

data class Item(
    val accessInfo: AccessInfo?,
    val etag: String?,
    val id: String?,
    val kind: String?,
    val saleInfo: SaleInfo?,
    val searchInfo: SearchInfo?,
    val selfLink: String?,
    val volumeInfo: VolumeInfo?
)

data class SaleInfo(
    val country: String?,
    val isEbook: Boolean?,
    val saleability: String?
)

data class SearchInfo(
    val textSnippet: String?
)

data class AccessInfo(
    val accessViewStatus: String?,
    val country: String?,
    val embeddable: Boolean?,
    val epub: Epub?,
    val pdf: Pdf?,
    val publicDomain: Boolean?,
    val quoteSharingAllowed: Boolean?,
    val textToSpeechPermission: String?,
    val viewability: String?,
    val webReaderLink: String?
)

data class Epub(
    val acsTokenLink: String?,
    val isAvailable: Boolean?
)

data class Pdf(
    val acsTokenLink: String?,
    val isAvailable: Boolean?
)