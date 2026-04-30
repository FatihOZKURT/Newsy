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
        "animals-farmed" to "Animals Farmed",
        "artanddesign" to "Art & Design",
        "australia-news" to "Australia News",
        "books" to "Books",
        "business" to "Business",
        "community" to "Community",
        "culture" to "Culture",
        "education" to "Education",
        "environment" to "Environment",
        "fashion" to "Fashion",
        "film" to "Film",
        "food" to "Food",
        "football" to "Football",
        "games" to "Games",
        "global-development" to "Global Development",
        "inequality" to "Inequality",
        "jobsadvice" to "Jobs & Advice",
        "law" to "Law",
        "lifeandstyle" to "Life & Style",
        "media" to "Media",
        "money" to "Money",
        "music" to "Music",
        "politics" to "Politics",
        "science" to "Science",
        "society" to "Society",
        "sport" to "Sport",
        "technology" to "Technology",
        "travel" to "Travel",
        "television-and-radio" to "Television & Radio",
        "uk-news" to "UK News",
        "us-news" to "US News",
        "world" to "World News",
        "wellness" to "Wellness",
        "tv-and-radio" to "TV & Radio",
        "cities" to "Cities",
        "stage" to "Stage"
    )

    fun getCategoryDisplayName(id: String): String {
        return CATEGORY_DISPLAY_NAMES[id] ?: id.replace("-", " ").replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
    }
}
