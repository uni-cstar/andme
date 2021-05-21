package andme.tv.leanback.widget

import androidx.leanback.widget.ItemBridgeAdapter
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowPresenter
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Created by Lucio on 2021/5/20.
 * 只能用于ListRow情况
 */
class ListRowSharedViewPoolHelper : ItemBridgeAdapter.AdapterListener() {

    private var mRecycledViewPool: RecyclerView.RecycledViewPool? = null
    private var mPresenterMapper: ArrayList<Presenter>? = null

    var chainAdapterListener:ItemBridgeAdapter.AdapterListener? = null

    override fun onAddPresenter(presenter: Presenter?, type: Int) {
        super.onAddPresenter(presenter, type)
        chainAdapterListener?.onAddPresenter(presenter, type)
    }

    override fun onBind(viewHolder: ItemBridgeAdapter.ViewHolder?) {
        super.onBind(viewHolder)
        chainAdapterListener?.onBind(viewHolder)
    }

    override fun onBind(viewHolder: ItemBridgeAdapter.ViewHolder?, payloads: MutableList<Any?>?) {
        super.onBind(viewHolder, payloads)
        chainAdapterListener?.onBind(viewHolder, payloads)
    }

    override fun onUnbind(viewHolder: ItemBridgeAdapter.ViewHolder?) {
        super.onUnbind(viewHolder)
        chainAdapterListener?.onUnbind(viewHolder)
    }

    override fun onAttachedToWindow(viewHolder: ItemBridgeAdapter.ViewHolder?) {
        super.onAttachedToWindow(viewHolder)
        chainAdapterListener?.onAttachedToWindow(viewHolder)
    }

    override fun onDetachedFromWindow(viewHolder: ItemBridgeAdapter.ViewHolder?) {
        super.onDetachedFromWindow(viewHolder)
        chainAdapterListener?.onDetachedFromWindow(viewHolder)
    }

    override fun onCreate(viewHolder: ItemBridgeAdapter.ViewHolder) {
        super.onCreate(viewHolder)
        setupSharedViewPool(viewHolder)
        chainAdapterListener?.onCreate(viewHolder)
    }

    private fun setupSharedViewPool(bridgeVh: ItemBridgeAdapter.ViewHolder) {
        val rowPresenter = bridgeVh.presenter as? RowPresenter ?: return
        val rowVh = rowPresenter.getRowViewHolder(bridgeVh.viewHolder)
        if (rowVh is ListRowPresenter.ViewHolder) {
            val view = rowVh.gridView
            // Recycled view pool is shared between all list rows
            if (mRecycledViewPool == null) {
                mRecycledViewPool = view.recycledViewPool
            } else {
                view.setRecycledViewPool(mRecycledViewPool)
            }
            val bridgeAdapter = rowVh.bridgeAdapter
            if (mPresenterMapper == null) {
                mPresenterMapper = bridgeAdapter.presenterMapper
            } else {
                bridgeAdapter.presenterMapper = mPresenterMapper
            }
        }
    }


}