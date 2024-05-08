package com.minhmdl.goodbooks.model


data class Book(
    var bookID: String,
    val authors: List<String>,
    val averageRating: Double,
    val categories: List<String>,
    val description: String,
    val imageLinks: ImageLinks,
    val language: String,
    val pageCount: Int,
    val industryIdentifiers: List<IndustryIdentifier>,
    val publishedDate: String,
    val publisher: String,
    val ratingsCount: Int,
    val subtitle: String,
    val title: String,
    val searchInfo: String
) {
    // No-argument constructor required for Firestore deserialization
    constructor() : this(
        "",
        emptyList(),
        0.0,
        emptyList(),
        "",
        ImageLinks("", ""),
        "",
        0,
        emptyList(),
        "",
        "",
        0,
        "",
        "",
        ""
    )
}
