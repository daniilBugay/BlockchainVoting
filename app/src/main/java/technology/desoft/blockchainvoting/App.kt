package technology.desoft.blockchainvoting

import android.app.Application
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.navigation.SimpleRouter
import technology.desoft.blockchainvoting.presentation.view.MainView

class App: Application() {
    lateinit var mainRouter: Router<MainView>

    override fun onCreate() {
        super.onCreate()
        mainRouter = SimpleRouter()
    }
}