package com.example.newsy.util

import java.util.Locale

object Constants {
    val ALLOWED_SECTIONS = setOf(
        "animals-farmed", "artanddesign", "australia-news", "books", "business",
        "community", "culture", "education", "environment",
        "fashion", "film", "food", "football", "games", "global-development", 
        "inequality", "jobsadvice", "law", "lifeandstyle", "media", "money", "music",
        "politics", "science", "society", "sport", "technology", "travel", 
        "television-and-radio", "uk-news", "us-news", "world",
        "wellness", "tv-and-radio", "cities", "stage"
    )

    val CATEGORY_DISPLAY_NAMES = mapOf(
        "animals-farmed" to "ANIMALS FARMED",
        "artanddesign" to "ART & DESIGN",
        "australia-news" to "AUSTRALIA NEWS",
        "books" to "BOOKS",
        "business" to "BUSINESS",
        "community" to "COMMUNITY",
        "culture" to "CULTURE",
        "education" to "EDUCATION",
        "environment" to "ENVIRONMENT",
        "fashion" to "FASHION",
        "film" to "FILM",
        "food" to "FOOD",
        "football" to "FOOTBALL",
        "games" to "GAMES",
        "global-development" to "GLOBAL DEVELOPMENT",
        "inequality" to "INEQUALITY",
        "jobsadvice" to "JOBS & ADVICE",
        "law" to "LAW",
        "lifeandstyle" to "LIFE & STYLE",
        "media" to "MEDIA",
        "money" to "MONEY",
        "music" to "MUSIC",
        "politics" to "POLITICS",
        "science" to "SCIENCE",
        "society" to "SOCIETY",
        "sport" to "SPORT",
        "technology" to "TECHNOLOGY",
        "travel" to "TRAVEL",
        "television-and-radio" to "TELEVISION & RADIO",
        "uk-news" to "UK NEWS",
        "us-news" to "US NEWS",
        "world" to "WORLD NEWS",
        "wellness" to "WELLNESS",
        "tv-and-radio" to "TV & RADIO",
        "cities" to "CITIES",
        "stage" to "STAGE"
    )

    fun getCategoryDisplayName(id: String): String {
        return CATEGORY_DISPLAY_NAMES[id] ?: id.replace("-", " ").replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
    }
}
