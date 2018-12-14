package technology.desoft.blockchainvoting.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import technology.desoft.blockchainvoting.model.Token
import technology.desoft.blockchainvoting.model.UserRepository
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.navigation.navigations.AllPollsNavigation
import technology.desoft.blockchainvoting.presentation.view.MainView
import technology.desoft.blockchainvoting.presentation.view.SignView

@InjectViewState
class SignUpPresenter(
    private val coroutineScope: CoroutineScope,
    private val router: Router<MainView>,
    private val userRepository: UserRepository
    ): MvpPresenter<SignView>(), CoroutineScope by coroutineScope {

    private val jobs: MutableList<Job> = mutableListOf()

    fun registration(email: String, password: String, confirmPassword: String){
        viewState.loading()
        val job = launch(Dispatchers.IO) {
            val user = userRepository.registration(email, password, confirmPassword).await()
            if (user != null) launch(Dispatchers.Main) { onSuccess() }.start()
            else launch(Dispatchers.Main) { onError("Error") }.start()
        }
        jobs.add(job)
        job.start()
    }

    private fun onSuccess(){
        viewState.showSuccess("Success")
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