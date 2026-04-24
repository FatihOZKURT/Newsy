package com.example.newsy.data.mapper

import com.example.newsy.data.remote.dto.ArticleDTO
import com.example.newsy.domain.model.Article

fun ArticleDTO.toDomain(): Article {
    return Article(
        id = id,
        title = webTitle,
        imageUrl = fields?.thumbnail,
        category = sectionName,
        time = webPublicationDate,
        webUrl = webUrl,
        description = fields?.trailText,
        body = fields?.body
    )
}
