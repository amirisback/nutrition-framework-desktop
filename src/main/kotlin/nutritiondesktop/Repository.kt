package nutritiondesktop

import com.frogobox.nutritioncore.model.news.ArticleResponse
import com.frogobox.nutritioncore.model.news.SourceResponse
import com.frogobox.nutritioncore.sources.NutriApiClient
import com.frogobox.nutritioncore.sources.NutriApiObserver
import com.frogobox.nutritioncore.sources.NutriResponse
import com.frogobox.nutritioncore.sources.news.NewsApiService
import com.frogobox.nutritioncore.sources.news.NewsDataSource
import com.frogobox.nutritioncore.util.news.NewsUrl

/*
 * Created by faisalamir on 29/12/21
 * nutrition-framework-desktop
 * -----------------------------------------
 * Name     : Muhammad Faisal Amir
 * E-mail   : faisalamircs@gmail.com
 * Github   : github.com/amirisback
 * -----------------------------------------
 * Copyright (C) 2021 FrogoBox Inc.      
 * All rights reserved
 *
 */

object Repository : NewsDataSource {

    private val TAG = Repository::class.java.simpleName
    private var newsApiService = NutriApiClient.create<NewsApiService>(NewsUrl.BASE_URL, true)

    override fun getTopHeadline(
        apiKey: String,
        q: String?,
        sources: String?,
        category: String?,
        country: String?,
        pageSize: Int?,
        page: Int?,
        callback: NutriResponse.DataResponse<ArticleResponse>
    ) {
        newsApiService.getTopHeadline(apiKey, q, sources, category, country, pageSize, page)
            .doOnSubscribe { callback.onShowProgress() }
            .doOnTerminate { callback.onHideProgress() }
            .subscribe(object : NutriApiObserver<ArticleResponse>() {
                override fun onSuccess(data: ArticleResponse) {
                    if (data.articles?.size == 0) {
                        callback.onEmpty()
                    } else {
                        callback.onSuccess(data)
                    }
                }

                override fun onFailure(code: Int, errorMessage: String) {
                    callback.onFailed(code, errorMessage)
                }
            })
    }

    override fun getEverythings(
        apiKey: String,
        q: String?,
        from: String?,
        to: String?,
        qInTitle: String?,
        sources: String?,
        domains: String?,
        excludeDomains: String?,
        language: String?,
        sortBy: String?,
        pageSize: Int?,
        page: Int?,
        callback: NutriResponse.DataResponse<ArticleResponse>
    ) {
        newsApiService.getEverythings(
            apiKey,
            q,
            from,
            to,
            qInTitle,
            sources,
            domains,
            excludeDomains,
            language,
            sortBy,
            pageSize,
            page
        )
            .doOnSubscribe { callback.onShowProgress() }
            .doOnTerminate { callback.onHideProgress() }
            .subscribe(object : NutriApiObserver<ArticleResponse>() {
                override fun onSuccess(data: ArticleResponse) {
                    if (data.articles?.size == 0) {
                        callback.onEmpty()
                    } else {
                        callback.onSuccess(data)
                    }
                }

                override fun onFailure(code: Int, errorMessage: String) {
                    callback.onFailed(code, errorMessage)
                }
            })
    }

    override fun getSources(
        apiKey: String,
        language: String,
        country: String,
        category: String,
        callback: NutriResponse.DataResponse<SourceResponse>
    ) {
        newsApiService.getSources(apiKey, language, country, category)
            .doOnSubscribe { callback.onShowProgress() }
            .doOnTerminate { callback.onHideProgress() }
            .subscribe(object : NutriApiObserver<SourceResponse>() {
                override fun onSuccess(data: SourceResponse) {
                    if (data.sources?.size == 0) {
                        callback.onEmpty()
                    } else {
                        callback.onSuccess(data)
                    }
                }

                override fun onFailure(code: Int, errorMessage: String) {
                    callback.onFailed(code, errorMessage)
                }
            })
    }

    // Please Add Your Code Under This Line --------------------------------------------------------

}