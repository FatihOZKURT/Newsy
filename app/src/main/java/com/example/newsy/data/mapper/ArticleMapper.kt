package com.example.newsy.data.mapper

import com.example.newsy.data.remote.dto.ArticleDTO
import com.example.newsy.data.remote.dto.SectionDTO
import com.example.newsy.domain.model.Article
import com.example.newsy.domain.model.Interest

fun ArticleDTO.toDomain(): Article {
    return Article(
        id = id,
        title = webTitle,
        imageUrl = fields?.thumbnail,
        category = sectionName ?: "General",
        time = webPublicationDate ?: "",
        webUrl = webUrl,
        description = fields?.trailText,
        body = fields?.body
    )
}

fun SectionDTO.toDomain(): Interest {
    return Interest(
        id = id,
        name = webTitle,
        isSelected = false
    )
}
