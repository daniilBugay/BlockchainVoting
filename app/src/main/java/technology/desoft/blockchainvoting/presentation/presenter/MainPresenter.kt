package technology.desoft.blockchainvoting.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import technology.desoft.blockchainvoting.model.network.polls.PollRepository
import technology.desoft.blockchainvoting.model.network.user.Token
import technology.desoft.blockchainvoting.model.network.user.UserRepository
import technology.desoft.blockchainvoting.model.network.user.UserTokenProvider
import technology.desoft.blockchainvoting.model.network.vote.VoteRepository
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.presentation.view.MainView

@InjectViewState
class MainPresenter(
    private val router: Router<MainView>,
    private val userProvider: UserTokenProvider,
    private val userRepository: UserRepository,
    private val pollRepository: PollRepository,
    private val voteRepository: VoteRepository
) : MvpPresenter<MainView>() {
    private val jobs = mutableListOf<Job>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.setView(viewState)

        val email = userProvider.getSavedEmail()
        val password = userProvider.getSavedPassword()
        if (email != null && password != null) {
            try {
                trySignIn(email, password)
            } catch (e: Exception){
                showSignInScreen()
            }
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
                    setToken(token)
                    viewState.showAllPolls()
                }
            } else {
                showSignInScreen()
            }
        }
        jobs.add(job)
        job.start()
    }

    private fun setToken(token: Token) {
        userRepository.setToken(token)
        pollRepository.setToken(token)
        voteRepository.setToken(token)
    }

    private fun showSignInScreen() {
        viewState.showSignInScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }

}