package com.anvipo.angram.presentationLayer.common.baseClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anvipo.angram.presentationLayer.common.interfaces.Presentable

abstract class BaseFragment : Fragment(), Presentable {

    abstract val layoutRes: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    open fun onBackPressed() {}

}