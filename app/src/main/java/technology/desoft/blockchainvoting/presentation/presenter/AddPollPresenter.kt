package technology.desoft.blockchainvoting.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import technology.desoft.blockchainvoting.model.PollRepository
import technology.desoft.blockchainvoting.model.UserTokenProvider
import technology.desoft.blockchainvoting.presentation.view.AddPollView

@InjectViewState
class AddPollPresenter(
    private val pollRepository: PollRepository,
    private val userTokenProvider: UserTokenProvider
): MvpPresenter<AddPollView>() {
    private val options: MutableList<String> = mutableListOf()

    override fun attachView(view: AddPollView?) {
        super.attachView(view)
        options.forEach { viewState.addOption(it) }
    }

    fun onAdd(contentString: String){
        if (!contentString.isBlank()) {
            options.add(contentString)
            viewState.addOption(contentString)
        }
    }

    fun removeOption(position: Int){
        options.removeAt(position)
    }

    fun moveOption(from: Int, to: Int) {
        val temp = options[from]
        options[from] = options[to]
        options[to] = temp
    }
}