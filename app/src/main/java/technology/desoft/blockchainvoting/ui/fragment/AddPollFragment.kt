package technology.desoft.blockchainvoting.ui.fragment

import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_add_poll.view.*
import technology.desoft.blockchainvoting.App
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.AddPollPresenter
import technology.desoft.blockchainvoting.presentation.view.AddPollView
import technology.desoft.blockchainvoting.ui.CircularAnimationProvider
import technology.desoft.blockchainvoting.ui.adapter.AddOptionAdapter
import technology.desoft.blockchainvoting.ui.adapter.PollOptionTouchCallback
import technology.desoft.blockchainvoting.ui.changeElevation
import java.util.*
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
    fun providePresenter(): AddPollPresenter {
        val app = activity?.application as App
        return AddPollPresenter(resources, app.pollRepository)
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

    private fun circularAnimation(view: View, x: Int, y: Int) {
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
                    v?.removeOnLayoutChangeListener(this) ?: return
                    view.addFinishButton.visibility = View.INVISIBLE
                    val width = view.width
                    val height = view.height
                    val duration = v.context.resources.getInteger(R.integer.circular_animation_duration)
                    val radius = hypot(width.toFloat(), height.toFloat())
                    val anim = ViewAnimationUtils.createCircularReveal(view, x, y, 0f, radius)

                    anim.interpolator = FastOutSlowInInterpolator()
                    anim.duration = duration.toLong()
                    anim.start()
                    CircularAnimationProvider.startBackgroundAnimation(
                        view,
                        ContextCompat.getColor(context!!, R.color.colorAccent),
                        ContextCompat.getColor(context!!, android.R.color.background_light),
                        resources.getInteger(R.integer.circular_animation_duration)
                    )
                    view.setBackgroundColor(android.R.color.background_light)
                    Handler().postDelayed({ view.addFinishButton.show() }, 300)
                }

            }
        )
    }


    private fun showDatePicker(listener: (Int, Int, Int) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(context!!, { _, year, month, dayOfMonth ->
            listener(year, month, dayOfMonth)
        }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addOptionButton.setOnClickListener {
            addPollPresenter.onAdd(view.addOptionContent.text.toString())
        }
        view.addOptionRecycler.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        initDatePick(view)
        initRecycler(view)
        initCards(view)
        view.addPollLayout.setOnClickListener { view.clearFocus() }
        view.addFinishButton.setOnClickListener { finish() }
    }

    private fun initCards(view: View) {
        val defaultElevation = resources.getDimension(R.dimen.elevation_default)
        val activeElevation = resources.getDimension(R.dimen.elevation_active)
        fun getCardElevationListener(cardView: CardView) = { _: View, focus: Boolean ->
            if (focus)
                cardView.changeElevation(defaultElevation, activeElevation)
            else
                cardView.changeElevation(activeElevation, defaultElevation)
        }
        val dataCardListener = getCardElevationListener(view.addDataCard)
        view.addThemeEditText.setOnFocusChangeListener(dataCardListener)
        view.addDescriptionEditText.setOnFocusChangeListener(dataCardListener)
        view.endsAtText.setOnFocusChangeListener(dataCardListener)
        view.addOptionContent.setOnFocusChangeListener(getCardElevationListener(view.addOptionCard))
    }

    private fun initDatePick(view: View) {
        view.endsAtText.setOnClickListener {
            it.requestFocus()
            showDatePicker { year, month, day ->
                val date = createCalendar(year, month, day)
                if (Calendar.getInstance().lessThan(date)) {
                    view.endsAtText.text = DateFormat.getDateFormat(context).format(date.time)
                    addPollPresenter.setEndsDate(date)
                } else
                    showDatePickError()
            }
        }
    }

    private fun Calendar.lessThan(other: Calendar): Boolean {
        if (this[Calendar.YEAR] < other[Calendar.YEAR]) return true

        if (this[Calendar.YEAR] == other[Calendar.YEAR]) {
            if (this[Calendar.MONTH] < other[Calendar.MONTH])
                return true
            if (
                this[Calendar.MONTH] == other[Calendar.MONTH] &&
                this[Calendar.DAY_OF_MONTH] <= other[Calendar.DAY_OF_MONTH]
            ) return true
        }
        return false
    }

    private fun createCalendar(year: Int, month: Int, day: Int): Calendar {
        return Calendar.getInstance().apply {
            set(year, month, day, 23, 59, 59)
        }
    }

    private fun showDatePickError() {
        Toast.makeText(context!!, R.string.date_error, Toast.LENGTH_LONG).show()
    }

    private fun initRecycler(view: View) {
        val adapter = AddOptionAdapter(mutableListOf())
        val callback = PollOptionTouchCallback(adapter, addPollPresenter)
        val touchHelper = ItemTouchHelper(callback)
        callback.setFocusCallBack {
            view.clearFocus()
        }
        adapter.setOnDragStartListener { touchHelper.startDrag(it) }
        view.addOptionRecycler.adapter = adapter
        touchHelper.attachToRecyclerView(view.addOptionRecycler)
    }

    override fun onResume() {
        super.onResume()

        val view = this.view ?: return
        if (view.addOptionRecycler.adapter?.itemCount == 0)
            addPollPresenter.setOptions()
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
                    onEnd
                )
                CircularAnimationProvider.startBackgroundAnimation(
                    it,
                    ContextCompat.getColor(context!!, android.R.color.background_light),
                    ContextCompat.getColor(context!!, R.color.colorAccent),
                    resources.getInteger(R.integer.circular_animation_duration)
                )
            }
        }
    }

    override fun addOption(optionContent: String) {
        (view?.addOptionRecycler?.adapter as? AddOptionAdapter)?.addOption(optionContent)
        view?.addOptionRecycler?.scrollToPosition(0)
        view?.addOptionContent?.text?.clear()
    }

    private fun finish() {
        val view = this.view ?: return

        addPollPresenter.finishAdding(
            view.addThemeEditText.text.toString(),
            view.addDescriptionEditText.text.toString()
        )
    }

    override fun error(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun finishAdding() {
        activity?.onBackPressed()
    }

    override fun hideButton() {
        view?.addFinishButton?.hide()
    }

    override fun showButton() {
        view?.addFinishButton?.show()
    }
}