package technology.desoft.blockchainvoting.ui.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_add_poll.*
import kotlinx.android.synthetic.main.fragment_add_poll.view.*
import technology.desoft.blockchainvoting.App
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.AddPollPresenter
import technology.desoft.blockchainvoting.presentation.view.AddPollView
import technology.desoft.blockchainvoting.ui.CircularAnimationProvider
import technology.desoft.blockchainvoting.ui.adapter.AddOptionAdapter
import technology.desoft.blockchainvoting.ui.adapter.ItemTouchCallback
import kotlin.math.hypot

class AddPollFragment : MvpAppCompatFragment(), CircularAnimationProvider.Dismissible, AddPollView {
    companion object {
        private const val X_KEY = "x"
        private const val Y_KEY = "y"

        fun withCircularAnimation(x: Float, y: Float): AddPollFragment {
            val fragment = AddPollFragment()
            val bundle = Bundle()
            bundle.apply {
                putFloat(X_KEY, x)
                putFloat(Y_KEY, y)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var addPollPresenter: AddPollPresenter

    @ProvidePresenter
    fun providePresenter(): AddPollPresenter{
        val app = activity?.application as App
        return AddPollPresenter(app.pollRepository, app.userProvider)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_poll, container, false)
        val x = arguments?.getFloat(X_KEY)
        val y = arguments?.getFloat(Y_KEY)
        if (x != null && y != null && context != null) {
            circularAnimation(view, x.toInt(), y.toInt())
        }
        return view
    }

    private fun circularAnimation(view: View, x: Int, y: Int){
        view.addOnLayoutChangeListener(
            object : View.OnLayoutChangeListener {
                override fun onLayoutChange(
                    v: View?,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
                ) {
                    v?.removeOnLayoutChangeListener(this)
                    val width = view.width
                    val height = view.height
                    val duration = context!!.resources.getInteger(R.integer.circular_animation_duration)
                    val radius = hypot(width.toFloat(), height.toFloat())
                    val anim = ViewAnimationUtils.createCircularReveal(v, x, y, 0f, radius)
                    anim.interpolator = FastOutSlowInInterpolator()
                    anim.duration = duration.toLong()
                    anim.start()
                    view.visibility = View.VISIBLE
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(context, R.anim.move_up)
        animation.startOffset += 200
        view.addDataCard.startAnimation(animation)
        animation.startOffset += 200
        view.addOptionCard.startAnimation(animation)
        view.addOptionButton.setOnClickListener {
            addPollPresenter.onAdd(view.addOptionContent.text.toString())
        }
        view.addOptionRecycler.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        val adapter = AddOptionAdapter(mutableListOf())
        val touchHelper = ItemTouchHelper(ItemTouchCallback(adapter, addPollPresenter))
        adapter.setOnDragStartListener { touchHelper.startDrag(it) }
        view.addOptionRecycler.adapter = adapter
        touchHelper.attachToRecyclerView(view.addOptionRecycler)


    }

    private fun getAnimationSettings(view: View, x: Float, y: Float): CircularAnimationProvider.Settings {
        return CircularAnimationProvider.Settings(
            x.toInt(),
            y.toInt(),
            view.width,
            view.height
        )
    }

    override fun dismiss(onEnd: () -> Unit) {
        val x = arguments?.getFloat(X_KEY)
        val y = arguments?.getFloat(Y_KEY)
        if (x != null && y != null) {
            view?.let {
                CircularAnimationProvider.startExitAnimation(
                    it.context,
                    it,
                    getAnimationSettings(it, x, y),
                    ContextCompat.getColor(it.context, android.R.color.white),
                    ContextCompat.getColor(it.context, R.color.colorAccent),
                    onEnd
                )
            }
        }
    }

    override fun addOption(optionContent: String) {
        (view?.addOptionRecycler?.adapter as AddOptionAdapter).addOption(optionContent)
        view?.addOptionRecycler?.scrollToPosition(0)
        view?.addOptionContent?.text?.clear()
    }
}