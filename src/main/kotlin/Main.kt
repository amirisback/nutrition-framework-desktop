// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.frogobox.nutritioncore.compose.ui.nutri_dimen_0dp
import com.frogobox.nutritioncore.compose.ui.nutri_dimen_16dp
import com.frogobox.nutritioncore.compose.widget.*
import com.frogobox.nutritioncore.model.news.Article
import com.frogobox.nutritioncore.model.news.ArticleResponse
import com.frogobox.nutritioncore.sources.NutriResponse
import com.frogobox.nutritioncore.util.news.NewsConstant.COUNTRY_ID
import com.frogobox.nutritioncore.util.news.NewsUrl

@Composable
@Preview
fun App() {
    MaterialTheme {
        SetupUI()
    }
}

fun main() = application {
    Window(
        title = "Nutrition Framework",
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            width = 900.dp,
            height = 1000.dp,
            position = WindowPosition(alignment = Alignment.Center)
        )
    ) {
        App()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetupRvComponent(dataState: List<Article>) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
        NutriLazyColumn(
            data = dataState,
            modifier = Modifier.width(300.dp).fillMaxHeight(),
            contentPadding = PaddingValues(bottom = nutri_dimen_16dp)
        ) {
            it.title?.let { it1 ->
                it.author?.let { it2 ->
                    it.content?.let { it3 ->
                        NutriListType3(
                            it1,
                            it2,
                            it3
                        )
                    }
                }
            }
        }
        NutriLazyFixedGrid(
            data = dataState,
            spanCount = 2,
            contentPadding = PaddingValues(bottom = nutri_dimen_16dp)
        ) {
            it.title?.let { it1 ->
                it.author?.let { it2 ->
                    it.content?.let { it3 ->
                        it.urlToImage?.let { it4 ->
                            NutriGridType3(
                                it4,
                                it1,
                                it2,
                                it3
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun SetupUI() {

    var dataState: List<Article> by remember { mutableStateOf(emptyList()) }
    var progressState: Boolean by remember { mutableStateOf(false) }

    val consumeNewsApi = Consume(NewsUrl.API_KEY) // Your API_KEY
    consumeNewsApi.getEverythings( // Adding Base Parameter on main function
        "Nutrisi",
        null,
        null,
        null,
        null,
        null,
        null,
        COUNTRY_ID,
        null,
        null,
        null,
        object : NutriResponse.DataResponse<ArticleResponse> {
            override fun onSuccess(data: ArticleResponse) {
                for (i in data.articles?.indices!!) {
                    println("${i + 1}.\t ${data.articles?.get(i)?.title}")
                    println("${i + 1}.\t ${data.articles?.get(i)?.urlToImage}")
                }
                dataState = data.articles!!
            }

            override fun onFailed(statusCode: Int, errorMessage: String?) {
                // Your failed to do
            }

            override fun onShowProgress() {
                // Your Progress Show
                println("Show Progress")
                progressState = true
            }

            override fun onHideProgress() {
                // Your Progress Hide
                println("Hide Progress")
                progressState = false
            }

            override fun onEmpty() {
            }

        })


    Column {
        NutriSimpleTopAppBar("Nutrition Framework Development", nutri_dimen_0dp)

        if (progressState) {
            NutriCircularProgressIndicator()
        } else {
            SetupRvComponent(dataState)
        }

    }
}