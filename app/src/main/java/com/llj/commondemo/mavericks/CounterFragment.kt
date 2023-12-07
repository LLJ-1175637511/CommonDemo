package com.llj.commondemo.mavericks

import android.view.LayoutInflater
import android.view.ViewGroup
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.llj.commondemo.core.base.BaseFragment
import com.llj.commondemo.core.router.AttachedActivity
import com.llj.commondemo.databinding.FragmentCounterBinding

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 12-06-2023
 */
@AttachedActivity(CounterActivity::class)
class CounterFragment : BaseFragment<FragmentCounterBinding>(), MavericksView {

    private val viewModel: CounterMavericksVM by fragmentViewModel()

    override fun invalidate() = withState(viewModel) {
        binding.tvCount.text = it.count.toString()
    }

    override fun buildBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCounterBinding {
        return FragmentCounterBinding.inflate(inflater, container, false)
    }

    override fun onViewCreate() {
        binding.btAdd.setOnClickListener {
            viewModel.add()
        }
        binding.btMinus.setOnClickListener {
            viewModel.minus()
        }
    }

}