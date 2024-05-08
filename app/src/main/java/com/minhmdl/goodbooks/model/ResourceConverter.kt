package com.minhmdl.goodbooks.model


/**
 * Class to convert a BooksResource object to a List of Book objects.
 */
class ResourceConverter {

    fun toBookList(resource: BooksResource): List<Book> {
        val listOfBooks = mutableListOf<Book>()
        resource.items
            ?.forEach { bookResource ->
                val book = bookResource?.let { parseBookResource(it) }
                if (book != null) {
                    listOfBooks.add(book)
                }
            }
        return listOfBooks
    }

    fun toBook(book: Item): Book {
        //Convert book(Item) fetched from API to Book
        return parseBookResource(book)
    }
}

/**
 * Parses an Item object that represents a book resource and returns a Book object with its properties populated.
 */
fun parseBookResource(bookResource: Item): Book {
    val id = bookResource.id ?: ""
    val authors = bookResource.volumeInfo?.authors?.toList() ?: listOf("")
    val averageRating = bookResource.volumeInfo?.averageRating ?: 0.0
    val categories = bookResource.volumeInfo?.categories?.toList() ?: listOf("")
    val description = bookResource.volumeInfo?.description ?: ""
    val imageLinks =
        bookResource.volumeInfo?.imageLinks ?: ImageLinks(smallThumbnail = "", thumbnail = "")
    val language = bookResource.volumeInfo?.language ?: ""
    val pageCount = bookResource.volumeInfo?.pageCount ?: 0
    val publishedDate = bookResource.volumeInfo?.publishedDate ?: ""
    val publisher = bookResource.volumeInfo?.publisher ?: ""
    val ratingsCount = bookResource.volumeInfo?.ratingsCount ?: 0
    val subtitle = bookResource.volumeInfo?.subtitle ?: ""
    val title = bookResource.volumeInfo?.title ?: ""
    val textSnippet = bookResource.searchInfo?.textSnippet ?: ""
    val industryIdentifiers = bookResource.volumeInfo?.industryIdentifiers?.toList() ?: listOf(
        IndustryIdentifier(
            identifier = "",
            type = ""
        )
    )

    return Book(
        bookID = id,
        authors = authors as List<String>,
        averageRating = averageRating,
        categories = categories as List<String>,
        description = description,
        imageLinks = imageLinks,
        language = language,
        pageCount = pageCount,
        industryIdentifiers = industryIdentifiers as List<IndustryIdentifier>,
        publishedDate = publishedDate,
        publisher = publisher,
        ratingsCount = ratingsCount,
        subtitle = subtitle,
        title = title,
        searchInfo = textSnippet
    )
}
