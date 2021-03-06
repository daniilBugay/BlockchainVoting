package technology.desoft.blockchainvoting.presentation.presenter

import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.network.user.UserRepository
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.navigation.navigations.SignInNavigation
import technology.desoft.blockchainvoting.presentation.view.MainView
import technology.desoft.blockchainvoting.presentation.view.SignView

@InjectViewState
class SignUpPresenter(
    private val coroutineScope: CoroutineScope,
    private val router: Router<MainView>,
    private val userRepository: UserRepository,
    private val resources: Resources
) : MvpPresenter<SignView>(), CoroutineScope by coroutineScope {

    private val jobs: MutableList<Job> = mutableListOf()
    private var registration = false

    fun registration(rawEmail: String, rawPassword: String, rawConfirmPassword: String) {
        if (registration)
            return

        registration = true

        val email = rawEmail.trim()
        val password = rawPassword.trim()
        val confirmPassword = rawConfirmPassword.trim()
        if (rawPassword.length < 8) {
            onError(resources.getString(R.string.password_to_short_error))
            return
        }

        viewState.loading()
        val job = launch(Dispatchers.IO) {
            try {
                val user = userRepository.registration(email, password, confirmPassword).await()
                if (user != null) launch(Dispatchers.Main) { onSuccess() }.start()
                else launch(Dispatchers.Main) { onError(resources.getString(R.string.error)) }.start()
            } catch (e: HttpException) {
                launch(Dispatchers.Main) { onError(resources.getString(R.string.network_error)) }.start()
            } catch (e: JsonSyntaxException) {
                launch(Dispatchers.Main) { onError(resources.getString(R.string.user_exists_error)) }.start()
            }
        }
        jobs.add(job)
        job.start()
    }

    fun transitionToSignIn() {
        router.postNavigation(SignInNavigation())
    }

    private fun onSuccess() {
        registration = false
        router.postNavigation(SignInNavigation())
    }

    private fun onError(error: String) {
        registration = false
        viewState.showError(error)
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }
}