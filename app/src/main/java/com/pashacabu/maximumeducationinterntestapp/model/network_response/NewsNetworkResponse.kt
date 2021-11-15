package com.pashacabu.maximumeducationinterntestapp.model.network_response

import kotlinx.serialization.Serializable

@Serializable
data class NewsNetworkResponse(
	val pagination: Pagination? = null,
	val data: MutableList<DataItem?>? = null
)

@Serializable
data class Pagination(
	val total: Int? = null,
	val offset: Int? = null,
	val limit: Int? = null,
	val count: Int? = null
)

@Serializable
data class DataItem(
	var image: String? = null,
	var country: String? = null,
	var author: String? = null,
	var description: String? = null,
	var language: String? = null,
	var source: String? = null,
	var title: String? = null,
	var category: String? = null,
	var published_at: String? = null,
	var url: String? = null
)

