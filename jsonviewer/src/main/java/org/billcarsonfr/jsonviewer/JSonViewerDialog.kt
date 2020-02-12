package org.billcarsonfr.jsonviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.airbnb.mvrx.MvRx


class JSonViewerDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_jv, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args: JSonViewerFragmentArgs = arguments?.getParcelable(MvRx.KEY_ARG) ?: return
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer, JSonViewerFragment.newInstance(
                        args.jsonString,
                        args.defaultOpenDepth,
                        true,
                        args.styleProvider
                    )
                )
                .commitNow()
        }
    }

    override fun onResume() {
        super.onResume()
        // Get existing layout params for the window
        val params = dialog?.window?.attributes
        // Assign window properties to fill the parent
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = params
    }

    companion object {
        fun newInstance(
            jsonString: String,
            initialOpenDepth: Int = -1,
            wrap: Boolean = false,
            styleProvider: JSonViewerStyleProvider? = null
        ): JSonViewerDialog {
            val args = Bundle()
            val parcelableArgs =
                JSonViewerFragmentArgs(jsonString, initialOpenDepth, wrap, styleProvider)
            args.putParcelable(MvRx.KEY_ARG, parcelableArgs)
            return JSonViewerDialog().apply { arguments = args }
        }
    }

}