// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.frogobox.nutritioncore.compose.ui.nutri_dimen_16dp
import com.frogobox.nutritioncore.compose.widget.NutriLazyColumn
import com.frogobox.nutritioncore.compose.widget.NutriListType1
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

@Composable
@Preview
fun App() {
    DesktopMaterialTheme {
        RecyclerView()
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
fun RecyclerView() {

    var newsState: List<Article> by remember { mutableStateOf(emptyList()) }

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
                    newsState = data.articles!!
                }

                override fun onFailed(statusCode: Int, errorMessage: String?) {
                    // Your failed to do
                }

                override fun onShowProgress() {
                    // Your Progress Show
                    println("Show Progress")
                }

                override fun onHideProgress() {
                    // Your Progress Hide
                    println("Hide Progress")
                }

                override fun onEmpty() {
                }

            })
    }

    NutriLazyColumn(
        data = newsState,
        contentPadding = PaddingValues(bottom = nutri_dimen_16dp)
    ) {
        it.title?.let { it1 ->
            NutriListType1(
                it1
            )
        }
    }

}