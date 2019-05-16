package by.bsuir.luckymushroom.app

import android.app.Application
import by.bsuir.luckymushroom.app.dto.articles.Article
import by.bsuir.luckymushroom.app.dto.users.User
import by.bsuir.luckymushroom.app.services.api.articles.ArticlesService
import by.bsuir.luckymushroom.app.services.api.recognitionRequests.AddRecognitionRequestService
import by.bsuir.luckymushroom.app.services.api.users.LoginService
import by.bsuir.luckymushroom.app.services.api.users.LogoutService
import by.bsuir.luckymushroom.app.services.api.users.SignupService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    companion object {
        //        val BASE_URL = "http://192.168.0.106:58479"
        val BASE_URL = "http://165.22.88.25:5000"

        lateinit var loginService: LoginService
        lateinit var signupService: SignupService
        lateinit var logoutService: LogoutService

        lateinit var articlesService: ArticlesService

        lateinit var addRecognitionRequestService: AddRecognitionRequestService

        var user: User? = null
        var cookie: String? = null

        val defaultArticles: Array<Article> = arrayOf(
            Article(
                "Белый гриб (Boletus edulis)", "Шляпка:\n" +
                        "Цвет шляпки белого гриба, в зависимости от условий произрастания, варьируется от беловатого до темно-коричневого, иногда (особенно у сосновой и еловой разновидностей) с красноватым отливом. Форма шляпки вначале полушаровидная, позже подушковидная, выпуклая, очень мясистая, диаметр до 25 см. Поверхность шляпки гладкая, чуть бархатистая. Мякоть белая, плотная, толстая, не меняющая цвет на изломе, практически без запаха, с приятным ореховым вкусом.\n" +
                        "\n" +
                        "Ножка: \n" +
                        "У белого гриба очень массивная ножка , высота до 20см, толщина до 5 см, сплошная, цилиндрическая, у основания расширенная, белая или светло-коричневая, со светлым сетчатым рисунком в верхней части. Как правило, значительная часть ножки находится под землей, в подстилке.\n" +
                        "\n" +
                        "Распространение:\n" +
                        "Различные разновидности белого гриба произрастают в лиственных, хвойных и смешанных лесах с начала лета до октября (с перерывами), образуя микоризу с различными видами деревьев. \n"
            ),
            Article(
                "Грибы Лисички (лат. Cantharellus)",
                "Лисичка (лат. Cantharellus) — род грибов семейства Лисичковые (лат. Cantharellaceae), входящего в порядок Кантарелловые (Cantharellales).\n" +
                        "\n" +
                        "Общее описание:\n" +
                        "Плодовые тела шляпконожечные, небольшие или крупные, мясистые, более или менее воронковидной формы, у большинства видов жёлтого или красноватого цвета, реже беловатые.\n" +
                        "Шляпка мясистая, с довольно толстым, тупым краем.\n" +
                        "Гименофор у большинства видов складчатый, не отделяемый от шляпки и ножки. Складки толстые, у большинства видов разветвлённые, у некоторых видов образуют «сеточку». У некоторых видов гименофор гладкий, как и у видов близкородственного рода Вороночник (Craterellus). Складчатый или гладкий гименофор является характерной особенностью грибов рода.\n" +
                        "Ножка довольно толстая, мясистая, короткая.\n" +
                        "Мякоть белого или жёлтого цвета, на разрезе у многих видов синеет, реже — краснеет или остаётся неокрашенной.\n" +
                        "Покрывало отсутствует.\n" +
                        "Споровый порошок у всех видов белого цвета.\n"
            ),
            Article("Лисичка обыкновенная (Cantharellus cibarius)", "Лиси́чка обыкнове́нная, или Лисичка настоя́щая, или Петушóк(лат. Cantharēllus cibārius) — вид грибов семейства лисичковых.\n" +
                    "Описание\n" +
                    "Шляпка: \n" +
                    "У лисички шляпка цвета яично- или оранжево-желтого (иногда выцветающего до очень светлого, почти белого); очертаниями шляпка сначала слабо-выпуклая, почти плоская, затем воронковидная, зачастую неправильной формы. Диаметр 4-6 см (до 10), сама шляпка мясистая, гладкая, с волнистым складчатым краем.\n" +
                    "Мякоть плотная, упругая, того-же цвета, что и шляпка или светлее, со слабым фруктовым запахом и чуть островатым вкусом.\n" +
                    "Распространение:\n" +
                    "Этот весьма распространенный гриб растет с начала лета до поздней осени в смешанных, лиственных и хвойных лесах, временами (особенно в июле) в огромных количествах. Особенно часто встречается во мхах, в хвойных лесах.\n")
        )

    }

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    override fun onCreate() {
        super.onCreate()

        loginService = retrofit.create(LoginService::class.java)
        signupService = retrofit.create(SignupService::class.java)
        logoutService = retrofit.create(LogoutService::class.java)

        articlesService = retrofit.create(ArticlesService::class.java)

        addRecognitionRequestService =
            retrofit.create(AddRecognitionRequestService::class.java)
    }


}