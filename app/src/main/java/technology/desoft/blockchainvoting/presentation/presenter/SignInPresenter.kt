package technology.desoft.blockchainvoting.presentation.presenter

import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.*
import retrofit2.HttpException
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.network.user.Token
import technology.desoft.blockchainvoting.model.network.user.UserRepository
import technology.desoft.blockchainvoting.model.network.user.UserTokenProvider
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.navigation.navigations.AllPollsNavigation
import technology.desoft.blockchainvoting.navigation.navigations.SignUpNavigation
import technology.desoft.blockchainvoting.presentation.view.MainView
import technology.desoft.blockchainvoting.presentation.view.SignView

@InjectViewState
class SignInPresenter(
    private val coroutineScope: CoroutineScope,
    private val router: Router<MainView>,
    private val userRepository: UserRepository,
    private val userTokenProvider: UserTokenProvider,
    private val resource: Resources
): MvpPresenter<SignView>(), CoroutineScope by coroutineScope {
    private val jobs: MutableList<Job> = mutableListOf()

    fun login(email: String, password: String){
        viewState.loading()
        val job = launch(Dispatchers.IO) {
            try {
                val token = userRepository.login(email, password).await()
                if (token != null) launch(Dispatchers.Main) { onSuccess(email, password, token) }.start()
                else launch(Dispatchers.Main) { onError(resource.getString(R.string.error)) }.start()
            } catch (e: HttpException){
                launch(Dispatchers.Main) { onError(resource.getString(R.string.network_error)) }
            }
        }
        jobs.add(job)
        job.start()
    }

    fun transitionToSignUp(){
        router.postNavigation(SignUpNavigation())
    }

    private fun onSuccess(email: String, password: String, token: Token){
        viewState.showSuccess(resource.getString(R.string.success))
        userRepository.setToken(token)
        userTokenProvider.saveEmail(email)
        userTokenProvider.savePassword(password)
        userTokenProvider.token = token
        userTokenProvider.setUserId(token)
        router.postNavigation(AllPollsNavigation())
    }

    private fun onError(error: String){
        viewState.showError(error)
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }
}