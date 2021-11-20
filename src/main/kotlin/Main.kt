// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.annotation.DrawableRes
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
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
import com.frogobox.nutritioncore.core.NutriResponse
import com.frogobox.nutritioncore.method.function.ConsumeNewsApi
import com.frogobox.nutritioncore.model.news.Article
import com.frogobox.nutritioncore.model.news.ArticleResponse
import com.frogobox.nutritioncore.util.news.NewsConstant.CATEGORY_HEALTH
import com.frogobox.nutritioncore.util.news.NewsConstant.COUNTRY_ID
import com.frogobox.nutritioncore.util.news.NewsUrl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.skia.Bitmap

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


@OptIn(DelicateCoroutinesApi::class)
@Composable
fun SetupUI() {

    var dataState: List<Article> by remember { mutableStateOf(emptyList()) }
    var progressState: Boolean by remember { mutableStateOf(false) }

    val consumeNewsApi = ConsumeNewsApi(NewsUrl.API_KEY) // Your API_KEY
    GlobalScope.launch {
        consumeNewsApi.getTopHeadline( // Adding Base Parameter on main function
            null,
            null,
            CATEGORY_HEALTH,
            COUNTRY_ID,
            null,
            null,
            object : NutriResponse.DataResponse<ArticleResponse> {
                override fun onSuccess(data: ArticleResponse) {
                    for (i in data.articles?.indices!!) {
                        println("${i + 1}.\t ${data.articles?.get(i)?.title}")
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
    }

    Column {
        NutriSimpleTopAppBar("Nutrition Framework Development", nutri_dimen_0dp)
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
            NutriLazyColumn(
                data = dataState,
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

        }
    }


}
