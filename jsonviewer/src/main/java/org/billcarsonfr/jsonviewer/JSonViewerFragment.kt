package org.billcarsonfr.jsonviewer

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.mvrx.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class JSonViewerFragmentArgs(
    val jsonString: String,
    val defaultOpenDepth: Int,
    val wrap: Boolean,
    val styleProvider: JSonViewerStyleProvider?
) : Parcelable


class JSonViewerFragment : Fragment(), MavericksView {

    private val viewModel: JSonViewerViewModel by fragmentViewModel()

    private val epoxyController by lazy {
        JSonViewerEpoxyController(requireContext())
    }

    private lateinit var recyclerView: EpoxyRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: JSonViewerFragmentArgs? = arguments?.getParcelable(Mavericks.KEY_ARG)
        val inflate =
            if (args?.wrap == true) {
                inflater.inflate(R.layout.fragment_jv_recycler_view_wrap, container, false)
            } else {
                inflater.inflate(R.layout.fragment_jv_recycler_view, container, false)
            }
        recyclerView = inflate.findViewById(R.id.jvRecyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setController(epoxyController)
        epoxyController.setStyle(args?.styleProvider)
        registerForContextMenu(recyclerView)
        return inflate
    }

    fun showJson(jsonString: String, initialOpenDepth: Int) {
        viewModel.setJsonSource(jsonString, initialOpenDepth)
    }

    override fun invalidate() = withState(viewModel) { state ->
        epoxyController.setData(state)
    }

    companion object {
        fun newInstance(
            jsonString: String,
            initialOpenDepth: Int = -1,
            wrap: Boolean = false,
            styleProvider: JSonViewerStyleProvider? = null
        ): JSonViewerFragment {
            return JSonViewerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        Mavericks.KEY_ARG,
                        JSonViewerFragmentArgs(
                            jsonString,
                            initialOpenDepth,
                            wrap,
                            styleProvider
                        )
                    )
                }
            }
        }
    }
}