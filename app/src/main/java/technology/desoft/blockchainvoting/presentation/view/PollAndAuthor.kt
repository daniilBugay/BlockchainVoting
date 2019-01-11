package technology.desoft.blockchainvoting.presentation.view

import technology.desoft.blockchainvoting.model.network.polls.Poll
import technology.desoft.blockchainvoting.model.network.user.User

data class PollAndAuthor(val poll: Poll, val author: User)