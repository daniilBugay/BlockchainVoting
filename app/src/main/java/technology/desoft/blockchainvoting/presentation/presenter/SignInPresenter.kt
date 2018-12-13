package technology.desoft.blockchainvoting.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import technology.desoft.blockchainvoting.model.UserRepository
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.presentation.view.MainView
import technology.desoft.blockchainvoting.presentation.view.SignView

@InjectViewState
class SignInPresenter(
    private val coroutineScope: CoroutineScope,
    private val router: Router<MainView>,
    private val userRepository: UserRepository
): MvpPresenter<SignView>(), CoroutineScope by coroutineScope {
    private val jobs: MutableList<Job> = mutableListOf()

    fun login(email: String, password: String){
        viewState.loading()
        val job = launch(Dispatchers.IO) {
            val token = userRepository.login(email, password).await()
            if (token != null) launch(Dispatchers.Main) { viewState.showSuccess("Success") }.start()
            else launch(Dispatchers.Main) { viewState.showError("Error") }.start()
        }
        jobs.add(job)
        job.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }
}