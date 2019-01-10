package technology.desoft.blockchainvoting.presentation.view

import technology.desoft.blockchainvoting.model.network.polls.PollOption

data class OptionResult(val option: PollOption, val votesCount: Int)