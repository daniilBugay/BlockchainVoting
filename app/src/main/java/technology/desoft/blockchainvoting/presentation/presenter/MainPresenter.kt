package technology.desoft.blockchainvoting.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import technology.desoft.blockchainvoting.model.network.user.UserRepository
import technology.desoft.blockchainvoting.model.network.user.UserTokenProvider
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.presentation.view.MainView

@InjectViewState
class MainPresenter(
    private val router: Router<MainView>,
    private val userProvider: UserTokenProvider,
    private val userRepository: UserRepository
) : MvpPresenter<MainView>() {
    private val jobs = mutableListOf<Job>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.setView(viewState)

        val email = userProvider.getSavedEmail()
        val password = userProvider.getSavedPassword()
        if (email != null && password != null) {
            trySignIn(email, password)
        } else {
            showSignInScreen()
        }
    }

    private fun trySignIn(email: String, password: String){
        val job = GlobalScope.launch {
            val token= userRepository.login(email, password).await()
            if (token != null){
                userProvider.token = token
                userRepository.setToken(token)
                val users = userRepository.getUsers().await()
                if (users == null)
                    showSignInScreen()
                else {
                    userProvider.userId = users.find { it.email == email }?.id
                    viewState.showAllPolls()
                }
            } else {
                showSignInScreen()
            }
        }
        jobs.add(job)
        job.start()
    }

    private fun showSignInScreen() {
        viewState.showSignInScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }

}